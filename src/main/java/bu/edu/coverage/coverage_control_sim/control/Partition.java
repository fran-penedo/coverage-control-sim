/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.Target;

/**
 * Creates a partition of the targets and associates each group to an agent.
 * Instead of strict partitioning (closest to target), we follow a fuzzy scheme.
 * A neighbor set from target i of size b at time t, \( \beta ^b (i, t) \), is
 * defined as the closest agents to the target (using the standard euclidean
 * distance). We then define a relative distance for all target j:
 * 
 * \[
 * \delta_{ij}(t) = \left\{\begin{matrix}
 * \frac{c_{ij}(t)}{\sum_{k \in \beta^b(i, t)}{c_{ik}(t)}} & \mathrm{if} \ j \in
 * \beta^b(i, t)\\
 * 1 & \mathrm{otherwise}
 * \end{matrix}\right.
 * \]
 * 
 * Finally, we define a relative proximity function with parameter \(\Delta \in
 * [0, \frac{1}{2}) \):
 * 
 * \[
 * p(\delta_{ij}(t)) = \left\{\begin{matrix}
 * 1 & \mathrm{if} \ \delta \leq \Delta \\
 * \frac{1-\Delta-\delta}{1 - 2 \Delta} & \mathrm{if} \ \Delta < \delta \leq 1 -
 * \Delta \\
 * 0 & \mathrm{if} \ \delta > 1 - \Delta \
 * \end{matrix}\right.
 * \]
 * 
 * Partitions are done following a least proximity rule.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class Partition {
	protected HashMap<Agent, ArrayList<Target>> map;
	protected double delta;
	protected int b;

	/**
	 * Creates a partition of the targets associated with the agents with a
	 * diffusion parameter delta and neighbor size b.
	 * 
	 * @param agents
	 * @param targets
	 * @param delta
	 * @param b
	 */
	public Partition(List<Agent> agents, List<Target> targets, double delta,
			int b) {
		this.delta = delta;
		this.b = b;
		partitions(agents, targets);
	}

	/**
	 * Gets the group of targets associated to an agent in the partition.
	 * 
	 * @param a
	 *            The agent
	 * @return A group of targets
	 */
	public ArrayList<Target> getPartition(Agent a) {
		return map.get(a);
	}

	protected void partitions(List<Agent> agents, List<Target> targets) {
		map = new HashMap<Agent, ArrayList<Target>>();

		for (Agent a : agents) {
			ArrayList<Target> partition = new ArrayList<Target>();
			for (Target t : targets) {
				if (t.isActive()) {
					// Get neighbor set
					List<Agent> closeA = closestAgents(t, agents, b);
					double sum = sumDist(t, closeA);
					// Proximity of current agent
					double p = proximity(a, t, closeA, sum);
					boolean add = true;
					// Check if current is the nearest
					for (Iterator<Agent> it = closeA.iterator(); it.hasNext()
							&& add;) {
						Agent a2 = it.next();
						double p2 = proximity(a2, t, closeA, sum);
						if (p2 > p) {
							add = false;
						}
					}
					if (add) {
						partition.add(t);
					}
				}
			}
			map.put(a, partition);
		}
	}

	protected double sumDist(Target t, List<Agent> closeA) {
		double sum = 0;
		for (Agent a : closeA) {
			sum += a.getPos().dist(t.getPos());
		}
		return sum;
	}

	protected List<Agent> closestAgents(Target t, List<Agent> agents, int b) {
		ArrayList<Agent> ret = new ArrayList<Agent>(agents);

		final class Cmp implements Comparator<Agent> {
			Target t;

			Cmp(Target t) {
				this.t = t;
			}

			public int compare(Agent o1, Agent o2) {
				if (o1.getPos().dist(t.getPos()) < o2.getPos().dist(t.getPos())) {
					return -1;
				} else {
					return 1;
				}
			}
		}

		java.util.Collections.sort(ret, new Cmp(t));

		if (ret.size() > b) {
			return ret.subList(0, b);
		} else {
			return ret;
		}
	}

	// Proximity function
	protected double proximity(Agent a, Target t, List<Agent> closeA, double sum) {
		double dist = relDist(a, t, closeA, sum);

		if (dist <= delta) {
			return 1;
		} else if (dist <= 1 - delta) {
			return (1 - delta - dist) / (1 - 2 * delta);
		} else {
			return 0;
		}
	}

	// Relative distance
	protected double relDist(Agent a, Target t, List<Agent> closeA, double sum) {
		return closeA.indexOf(a) == -1 ? 1 : a.getPos().dist(t.getPos()) / sum;
	}

}
