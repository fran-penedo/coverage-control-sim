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
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class KLCRH extends Control {
	protected int K;
	protected double delta;
	protected int b;
	protected HashSet<Agent> neighbors;

	/**
	 * 
	 */
	public KLCRH(int K, double delta, int b) {
		this.K = K;
		this.delta = delta;
		this.b = b;
	}

	public KLCRH() {
		this(1, 0.5, 2);
	}

	@Override
	public void init() {
		this.neighbors = new HashSet<>();
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
		ArrayList<Agent> agents = copyAgents(d, neighbors);
		// FIXME no sense
		ArrayList<Target> targets = copyTargets(d, agent.getSense()
				.getTargets());

		if (agents.size() > 0 && targets.size() > 0) {
			Heading best = bestHeading(d, agents, targets, K);
			double action_h = actionH();
			action_h = best.plan_h; // FIXME remove

			broadcastControl(neighbors, best, action_h);

			agent.postEvent(new Event(now, now + action_h, agent, EType.CONTROL));
		}
	}

	protected void broadcastControl(Collection<Agent> agents, Heading best,
			double action_h) {
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

	// FIXME todo
	protected double actionH() {
		return 0;
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

	protected Heading bestHeading(Director d, ArrayList<Agent> agents,
			ArrayList<Target> targets, int remaining) {
		double ji = rewardI(agents, targets, d.getCurrentTime());

		if (remaining == 0) { // can't simulate, times depend on agent/target
			double ja = rewardA(agents, targets, d.getCurrentTime());
			return new Heading(null, ja + ji, 0);
		}

		double plan_h = getPlanH(agents, targets);
		if (plan_h == Double.POSITIVE_INFINITY) {
			return new Heading(null, ji, 0);
		}

		List<List<Target>> active_targets = activeTargets(agents, targets,
				plan_h);
		List<List<Double>> active_heads = activeHeadings(agents, active_targets);
		HeadIt it = new HeadIt(active_heads);

		Heading best = new Heading(null, Double.NEGATIVE_INFINITY, plan_h);

		while (it.hasNext()) {
			List<Integer> next = it.next();
			ArrayList<Double> heading = new ArrayList<Double>();
			Director next_d = new Director(d.getCurrentTime());
			ArrayList<Agent> next_agents = copyAgents(next_d, agents);
			ArrayList<Target> next_targets = copyTargets(next_d, targets);

			for (int j = 0; j < next_agents.size(); j++) {
				int index = next.get(j);
				List<Double> head_list = active_heads.get(j);
				heading.add(head_list.get(index));
				next_agents.get(j).setHeading(heading.get(j));
			}

			next_d.runFor(plan_h);

			Heading cur = bestHeading(next_d, next_agents, next_targets,
					remaining - 1);
			if (cur.reward > best.reward) {
				best = new Heading(heading, cur.reward, plan_h);
			}
		}
		return new Heading(best.heading, best.reward + ji, plan_h);
	}

	protected double getPlanH(ArrayList<Agent> agents, ArrayList<Target> targets) {
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
				// TODO 1 - What if two targets are in range? Not my problem?
				// TODO 2 - Condition could be more complex, move to agent?
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
				/ (t.getIReward() / t.getDiscount().getDeadline())
				+ targetCostFactor(t, targets);

	}

	protected double targetCostFactor(Target t, List<Target> targets) {
		double ret = 0;
		for (Target ts : targets) {
			if (ts.isActive()) {
				// FIXME gamma
				ret += t.getPos().dist(ts.getPos())
						/ (ts.getIReward() / ts.getDiscount().getDeadline());
			}
		}
		return ret;
	}

	/**
	 * @return the k
	 */
	public int getK() {
		return K;
	}

	/**
	 * @param k
	 *            the k to set
	 */
	public void setK(int k) {
		K = k;
	}

	/**
	 * @return the delta
	 */
	public double getDelta() {
		return delta;
	}

	/**
	 * @param delta
	 *            the delta to set
	 */
	public void setDelta(double delta) {
		this.delta = delta;
	}

	/**
	 * @return the b
	 */
	public int getB() {
		return b;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(int b) {
		this.b = b;
	}

	protected class Heading {
		public final ArrayList<Double> heading;
		public final double reward;
		public final double plan_h;

		/**
		 * @param heading
		 * @param reward
		 */
		public Heading(ArrayList<Double> heading, double reward, double plan_h) {
			this.heading = heading;
			this.reward = reward;
			this.plan_h = plan_h;
		}

	}

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

	protected class HeadIt implements Iterator<List<Integer>> {
		public final List<List<Double>> heads;
		protected ArrayList<Integer> cur;
		protected boolean next;

		// FIXME empty list
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
		return super.toCode() + " " + K + " " + delta + " " + b;
	}

	@Override
	public Control deepCopy() {
		return new KLCRH(K, delta, b);
	}

}
