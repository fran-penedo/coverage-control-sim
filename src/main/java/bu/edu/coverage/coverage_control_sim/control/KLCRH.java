/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.Target;
import bu.edu.coverage.coverage_control_sim.comm.Communication;
import bu.edu.coverage.coverage_control_sim.comm.Message;
import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.Event.EType;
import bu.edu.coverage.coverage_control_sim.util.Debug;
import bu.edu.coverage.coverage_control_sim.util.Linear;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * Implements the K-Lookahead CRH algorithm.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class KLCRH extends Control {
	protected int K; // Lookahead steps
	protected double delta; // Partition fuzzy coefficient
	protected int b; // Partition neighbor size
	protected double gamma; // Sparsity factor coefficient
	protected boolean actionh_enabled; // Use action H computation
	protected HashSet<Agent> neighbors; // Controlled agents

	/**
	 * Creates a control module implementing the K-Lookahead CRH algorithm. This
	 * module will broadcast the computed control signals to the follower
	 * agents.
	 * 
	 * @param K
	 *            The number of lookahead steps
	 * @param delta
	 *            The fuzzy coefficient for the partitions
	 * @param b
	 *            The neighbor size for the partition
	 * @param gamma
	 *            The weight of the sparsity factor in the travel cost
	 * @param actionHEnabled
	 *            Whether the computation of action H is enabled
	 */
	public KLCRH(int K, double delta, int b, double gamma,
			boolean actionHEnabled) {
		this.K = K;
		this.delta = delta;
		this.b = b;
		this.gamma = gamma;
		this.actionh_enabled = actionHEnabled;
	}

	/**
	 * Test constructor.
	 */
	public KLCRH() {
		this(1, 0.5, 2, 0, true);
	}

	@Override
	public void init() {
		this.neighbors = new HashSet<>();
		// This is ugly. I need to allow agents to join, but this is done with
		// messages, so the comm module has to be initialized, then join
		// messages have to be sent and then I can run the algorithm.
		agent.postEvent(new Event(agent.getDirector().getCurrentTime(), agent
				.getDirector().getCurrentTime() + 0.2, agent, EType.CONTROL));
	}

	@Override
	public void setHeading(double heading) {
		// ignore

	}

	@Override
	public void addNeighbor(Agent agent) {
		neighbors.add(agent);

	}

	@Override
	public void control() {
		double now = agent.getDirector().getCurrentTime();
		Director d = new Director(now);
		// Copy targets and agents to run simulations for the lookahead steps
		ArrayList<Agent> agents = copyAgents(d, neighbors);
		ArrayList<Target> targets = copyTargets(d, agent.getSense()
				.getTargets());

		if (agents.size() > 0 && targets.size() > 0) {
			Heading best = bestHeading(d, agents, targets, K);
			double action_h = best.plan_h;
			if (actionh_enabled) {
				action_h = actionH(agents, targets, best);
			}

			broadcastControl(neighbors, best);

			agent.postEvent(new Event(now, now + action_h, agent, EType.CONTROL));
		}
	}

	protected void broadcastControl(Collection<Agent> agents, Heading best) {
		Communication comm = agent.getCommunication();
		if (comm != null) {
			int i = 0;
			for (Agent a : agents) {
				comm.send(new Message(agent.getId(), a.getId(),
						Message.MType.CONTROL, best.heading.get(i)));
				i++;
			}
		}

	}

	protected double actionH(List<Agent> agents, List<Target> targets,
			Heading head) {
		double mtime = Double.POSITIVE_INFINITY;

		for (int j = 0; j < targets.size(); j++) {
			Target y1 = targets.get(j);
			for (int k = j + 1; k < targets.size(); k++) {
				Target y2 = targets.get(k);
				for (int i = 0; i < agents.size(); i++) {
					Agent a = agents.get(i);
					Point ag_dir = Point.fromPolar(a.getV(),
							head.heading.get(i));
					Point tar_mid = y2.getPos().add(y1.getPos()).scale(0.5);
					Point tar_med_dir = y2.getPos().diff(y1.getPos()).orth();
					double t = Linear.solve1(ag_dir, tar_med_dir,
							tar_mid.diff(a.getPos()));
					if (t > 0 && t < mtime) {
						mtime = t;
					}
				}
			}
		}
		return Math.min(mtime, head.plan_h);
	}

	protected ArrayList<Agent> copyAgents(Director d, Collection<Agent> agents) {
		ArrayList<Agent> copy = new ArrayList<Agent>(agents.size());
		for (Agent agent : agents) {
			copy.add(new Agent(agent, d));
		}

		return copy;
	}

	protected ArrayList<Target> copyTargets(Director d,
			Collection<Target> targets) {
		ArrayList<Target> copy = new ArrayList<Target>(targets.size());
		for (Target target : targets) {
			copy.add(new Target(target, d));
		}

		return copy;
	}

	// Main steps of the KLCRH algorithm
	protected Heading bestHeading(Director d, ArrayList<Agent> agents,
			ArrayList<Target> targets, int remaining) {
		// Get the inmediate reward for the lookahead step
		double ji = rewardI(agents, targets, d.getCurrentTime());

		// After last lookahead, compute the reward to go estimate
		if (remaining == 0) { // can't simulate, times depend on agent/target
			double ja = rewardA(agents, targets, d.getCurrentTime());
			return new Heading(null, ja + ji, 0);
		}

		// Compute planning horizon
		double plan_h = planH(agents, targets);
		if (plan_h == Double.POSITIVE_INFINITY) {
			return new Heading(null, ji, 0);
		}

		// Obtain the active targets and the respective headings
		List<List<Target>> active_targets = activeTargets(agents, targets,
				plan_h);
		List<List<Double>> active_heads = activeHeadings(agents, active_targets);

		HeadIt it = new HeadIt(active_heads);
		Heading best = new Heading(null, Double.NEGATIVE_INFINITY, plan_h);

		Debug.debug("Remaining " + remaining + ". Active "
				+ active_targets.get(0));
		// Lookahead for each possible heading set
		while (it.hasNext()) {
			List<Integer> next = it.next();
			ArrayList<Double> heading = new ArrayList<Double>();

			// Setup a new simulation
			Director next_d = new Director(d.getCurrentTime());
			ArrayList<Agent> next_agents = copyAgents(next_d, agents);
			ArrayList<Target> next_targets = copyTargets(next_d, targets);

			// Set headings for each agent in the new simulation
			for (int j = 0; j < next_agents.size(); j++) {
				int index = next.get(j);
				List<Double> head_list = active_heads.get(j);
				heading.add(head_list.get(index));
				next_agents.get(j).setHeading(heading.get(j));
			}

			// Run the simulation
			next_d.runFor(plan_h);

			// Do next lookahead step and compare rewards
			Heading cur = bestHeading(next_d, next_agents, next_targets,
					remaining - 1);
			if (cur.reward > best.reward) {
				best = new Heading(heading, cur.reward, plan_h);
			}
		}

		return new Heading(best.heading, best.reward + ji, plan_h);
	}

	protected double planH(ArrayList<Agent> agents, ArrayList<Target> targets) {
		double h = Double.POSITIVE_INFINITY;
		for (Agent a : agents) {
			for (Target t : targets) {
				if (t.isActive()) {
					double e = a.getPos().dist(t.getPos()) / a.getV();
					if (e < h) {
						h = e;
					}
				}
			}
		}
		return h;
	}

	protected List<List<Double>> activeHeadings(ArrayList<Agent> agents,
			List<List<Target>> active_targets) {
		ArrayList<List<Double>> active_set = new ArrayList<List<Double>>();

		for (int i = 0; i < agents.size(); i++) {
			ArrayList<Double> active = new ArrayList<Double>();
			Agent a = agents.get(i);
			for (Target t : active_targets.get(i)) {
				active.add(a.getPos().headTo(t.getPos()));
			}
			active_set.add(active);
		}
		return active_set;
	}

	protected List<List<Target>> activeTargets(ArrayList<Agent> agents,
			ArrayList<Target> targets, double plan_h) {
		ArrayList<List<Target>> active_set = new ArrayList<List<Target>>();
		for (Agent a : agents) {
			ArrayList<Target> active = new ArrayList<Target>();

			for (Target t : targets) {
				if (t.isActive()) {
					boolean is_active = true;
					Point r = reachableToTarget(a, t, plan_h);
					double cost = targetTravelCost(t, r, targets);
					Iterator<Target> it = targets.iterator();
					while (is_active && it.hasNext()) {
						Target t_cmp = it.next();
						if (t_cmp.isActive()) {
							is_active = cost <= targetTravelCost(t_cmp, r,
									targets);
						}
					}

					if (is_active) {
						active.add(t);
					}
				}
			}
			active_set.add(active);
		}
		return active_set;
	}

	protected Point reachableToTarget(Agent a, Target t, double plan_h) {
		return a.getPos().add(
				t.getPos().unitDiff(a.getPos()).scale(a.getV() * plan_h));
	}

	protected double rewardI(ArrayList<Agent> agents,
			ArrayList<Target> targets, double t) {
		double j = 0;
		for (Agent agent : agents) {
			for (Target target : targets) {
				if (target.isActive() && target.inRange(agent.getPos())) {
					j += target.getReward(t);
					target.setActive(false);
				}
			}
		}

		return j;
	}

	protected double rewardA(ArrayList<Agent> agents,
			ArrayList<Target> targets, double t) {
		double j = 0;
		Partition p = new Partition(agents, targets, delta, b);
		for (Agent agent : agents) {
			ArrayList<Target> partition = p.getPartition(agent);
			Point cur = agent.getPos();
			Comparator<Target> cmp = new Cmp(cur, targets);
			int size = partition.size();
			for (int i = 0; i < size; i++) {
				Collections.sort(partition, cmp);
				Target target = partition.remove(0);
				j += target.getReward(t + cur.dist(target.getPos())
						/ agent.getV());
				cur = target.getPos();
			}
		}

		return j;
	}

	protected double targetTravelCost(Target t, Point x, List<Target> targets) {
		return x.dist(t.getPos())
				/ (t.getIReward() / t.getDiscount().getDeadline()) + gamma
				* targetCostFactor(t, targets);

	}

	protected double targetCostFactor(Target t, List<Target> targets) {
		double ret = 0;
		for (Target ts : targets) {
			if (ts.isActive()) {
				ret += t.getPos().dist(ts.getPos())
						/ (ts.getIReward() / ts.getDiscount().getDeadline());
			}
		}
		return ret;
	}

	/**
	 * @return The parameter K
	 */
	public int getK() {
		return K;
	}

	/**
	 * @param k
	 *            The parameter K to set
	 */
	public void setK(int k) {
		K = k;
	}

	/**
	 * @return The parameter delta
	 */
	public double getDelta() {
		return delta;
	}

	/**
	 * @param delta
	 *            The parameter delta to set
	 */
	public void setDelta(double delta) {
		this.delta = delta;
	}

	/**
	 * @return The parameter b
	 */
	public int getB() {
		return b;
	}

	/**
	 * @param b
	 *            The parameter b to set
	 */
	public void setB(int b) {
		this.b = b;
	}

	/**
	 * @return The parameter gamma
	 */
	public double getGamma() {
		return gamma;
	}

	/**
	 * @param gamma
	 *            The parameter gamma to set
	 */
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	/**
	 * @return Whether the computation for action H is enabled or not
	 */
	public boolean getActionHEnabled() {
		return actionh_enabled;
	}

	/**
	 * @param actionHEnabled
	 *            Whether the computation for action H is enable or not
	 */
	public void setActionHEnabled(boolean actionHEnabled) {
		this.actionh_enabled = actionHEnabled;
	}

	// Result of each lookahead step
	protected class Heading {
		public final ArrayList<Double> heading;
		public final double reward;
		public final double plan_h;

		/**
		 * @param heading
		 * @param reward
		 * @param plan_h
		 */
		public Heading(ArrayList<Double> heading, double reward, double plan_h) {
			this.heading = heading;
			this.reward = reward;
			this.plan_h = plan_h;
		}

	}

	// Compare targets by travel cost from a point
	protected class Cmp implements Comparator<Target> {
		protected Point x;
		protected List<Target> targets;

		public Cmp(Point x, List<Target> targets) {
			this.x = x;
			this.targets = targets;
		}

		public int compare(Target o1, Target o2) {
			if (targetTravelCost(o1, x, targets) < targetTravelCost(o2, x,
					targets)) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	// Iterator for headings
	protected class HeadIt implements Iterator<List<Integer>> {
		public final List<List<Double>> heads;
		protected ArrayList<Integer> cur;
		protected boolean next;

		public HeadIt(List<List<Double>> heads) {
			this.heads = heads;
			cur = new ArrayList<Integer>();
			for (int i = 0; i < heads.size(); i++) {
				cur.add(0);
			}
			next = true;
		}

		public boolean hasNext() {
			if (!next) {
				next = inc(0);
			}
			return next;
		}

		protected boolean inc(int i) {
			if (i < cur.size()) {
				cur.set(i, (cur.get(i) + 1) % heads.get(i).size());
				if (cur.get(i) == 0) {
					return inc(i + 1);
				} else {
					return true;
				}
			}
			return false;
		}

		public List<Integer> next() {
			if (next) {
				next = false;
				return cur;
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	@Override
	public String toCode() {
		return super.toCode() + " " + K + " " + delta + " " + b + " " + gamma
				+ " " + (actionh_enabled ? 1 : 0);
	}

	@Override
	public Control deepCopy() {
		return new KLCRH(K, delta, b, gamma, actionh_enabled);
	}

}
