/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.sense;

import java.util.ArrayList;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.Target;

/**
 * @author fran
 *
 */
public abstract class Sense {
	protected Agent agent;
	protected ArrayList<Target> targets;

	public void attach(Agent a) {
		this.agent = a;
		targets = new ArrayList<Target>();
	}

	public abstract void init();

	public abstract void sense();

	public void addTarget(Target target) {
		targets.add(target);
	}

	public List<Target> getTargets() {
		return targets;
	}

	public abstract void visited(Target target, double reward);
}
