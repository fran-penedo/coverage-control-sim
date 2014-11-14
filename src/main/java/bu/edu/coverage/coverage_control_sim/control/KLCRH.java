/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

import java.util.ArrayList;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.actor.Actor;
import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.Agent.AgentInfo;
import bu.edu.coverage.coverage_control_sim.actor.Director;
import bu.edu.coverage.coverage_control_sim.actor.Target;
import bu.edu.coverage.coverage_control_sim.actor.Target.TargetInfo;

/**
 * @author fran
 *
 */
public class KLCRH implements Control {
	public final int K;

	/**
	 * 
	 */
	public KLCRH(int K) {
		this.K = K;
	}

	public KLCRH() {
		this(1);
	}

	public void control(Actor a) {
		Director d = new Director(a.getDirector().getCurrentTime());
		List<AgentInfo> agent_info = a.getAgentInfo();
		List<TargetInfo> target_info = a.getTargetInfo();
		ArrayList<Agent> agents = createAgents(d, agent_info);
		ArrayList<Target> targets = createTargets(d, target_info);

		Heading best = bestHeading(d, agents, targets, K);
		actionH();

	}

	protected ArrayList<Agent> createAgents(Director d,
			List<AgentInfo> agent_info) {
		ArrayList<Agent> agents = new ArrayList<Agent>(agent_info.size());
		for (AgentInfo agentInfo : agent_info) {
			agents.add(new Agent(agentInfo, d));
		}

		return agents;
	}

	protected ArrayList<Target> createTargets(Director d,
			List<TargetInfo> target_info) {
		ArrayList<Target> targets = new ArrayList<Target>(target_info.size());
		for (TargetInfo targetInfo : target_info) {
			targets.add(new Target(targetInfo, d));
		}

		return targets;
	}

	protected ArrayList<Agent> copyAgents(Director d, List<Agent> agents) {
		ArrayList<Agent> copy = new ArrayList<Agent>(agents.size());
		for (Agent agent : agents) {
			copy.add(new Agent(agent, d));
		}

		return copy;
	}

	protected ArrayList<Target> copyTargets(Director d, List<Target> targets) {
		ArrayList<Target> copy = new ArrayList<Target>(targets.size());
		for (Target target : targets) {
			copy.add(new Target(target, d));
		}

		return copy;
	}

	protected Heading bestHeading(Director d, ArrayList<Agent> agents,
			ArrayList<Target> targets, int remaining) {
		double ji = rewardI(agents, targets);

		if (remaining == 0) { // can't simulate, times depend on agent/target
			double ja = rewardA(agents, targets);
			return new Heading(null, ja + ji);
		}

		double plan_h = getPlanH(agents, targets);
		List<List<Target>> active_targets = activeTargets(agents, targets,
				plan_h);
		List<List<Double>> active_heads = activeHeadings(agents, active_targets);
		int[] head_index = new int[active_heads.size()]; // FIXME an iterator
															// would be better
															// here

		Heading best = new Heading(null, Double.NEGATIVE_INFINITY);

		// while (iterator.hasMore()) {
		// int[] next = iterator.next();
		// build the next heading
		{
			Director next_d = new Director(d.getCurrentTime());
			ArrayList<Agent> next_agents = copyAgents(d, agents);
			ArrayList<Target> next_targets = copyTargets(d, targets);

			for (int j = 0; j < next_agents.size(); j++) {
				next_agents.get(j).setHeading(active_heads.get(j).get(next[j]));
			}

			next_d.runFor(plan_h);
			next_d.updateAll();

			Heading cur = bestHeading(next_d, next_agents, next_targets,
					remaining - 1);
			if (cur.reward > best.reward) {
				best = new Heading(heading, cur.reward);
			}
		}
		return new Heading(best.heading, best.reward + ji);
	}

	protected double rewardI(ArrayList<Agent> agents, ArrayList<Target> targets) {
		double j = 0;
		for (Agent agent : agents) {
			for (Target target : targets) {
				if (!target.isVisited() && agent.visits(target)) {
					j += target.getReward(); // FIXME do I need full targets?
				}
			}
		}

		return j;
	}

	protected double rewardA(ArrayList<Agent> agents, ArrayList<Target> targets) {
		double j = 0;
		for (Agent agent : agents) {
			for (Target target : targets) {
				if (!target.isVisited()) {
					j += target.getReward() * q; // how do I move the actor now?
													// I csn move them here
				}
			}
		}

		return j;
	}

	protected class Heading {
		public final ArrayList<Double> heading;
		public final double reward;

		/**
		 * @param heading
		 * @param reward
		 */
		public Heading(ArrayList<Double> heading, double reward) {
			this.heading = heading;
			this.reward = reward;
		}

	}
}
