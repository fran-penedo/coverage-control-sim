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
 * @author fran
 *
 */
public class Partition {
	protected HashMap<Agent, ArrayList<Target>> map;
	protected double delta;
	protected int b;

	/**
	 * 
	 */
	public Partition(List<Agent> agents, List<Target> targets, double delta,
			int b) {
		this.delta = delta;
		this.b = b;
		partitions(agents, targets);
	}

	public ArrayList<Target> getPartition(Agent a) {
		return map.get(a);
	}

	protected void partitions(List<Agent> agents, List<Target> targets) {
		map = new HashMap<Agent, ArrayList<Target>>();

		for (Agent a : agents) {
			ArrayList<Target> partition = new ArrayList<Target>();
			for (Target t : targets) {
				if (t.isActive()) {
					List<Agent> closeA = closestAgents(t, agents, b);
					double sum = sumDist(t, closeA);
					double p = proximity(a, t, closeA, sum);
					boolean add = true;
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

	protected double relDist(Agent a, Target t, List<Agent> closeA, double sum) {
		return closeA.indexOf(a) == -1 ? 1 : a.getPos().dist(t.getPos()) / sum;
	}

}
