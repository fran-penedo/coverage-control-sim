/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.sense;

import java.util.ArrayList;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.Target;

/**
 * Base abstract class for sensing modules. This module will obtain information
 * from the environment for the agent, for example: target position, target
 * visibility, obstacle detection... Instead of mimicking the real world
 * sensors, this module will obtain all posible information at startup and then
 * filter the information according to sensor rules (such as visibility or FOV).
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public abstract class Sense {
	protected Agent agent; // Owner
	protected ArrayList<Target> targets; // All targets in the simulation

	/**
	 * Sets the onwer of this module. Called from the agent setter
	 * 
	 * @param a
	 *            The owner agent
	 */
	public void attach(Agent a) {
		this.agent = a;
	}

	/**
	 * Initializes the module
	 */
	public void init() {
		targets = new ArrayList<Target>();
	}

	/**
	 * Performs a sensing action in response to a SENSE event. Used for periodic
	 * actions.
	 */
	public abstract void sense();

	/**
	 * Adds a target to the list of known targets (not necessarily known to the
	 * agent.
	 * 
	 * @param target
	 *            The target to add
	 */
	public void addTarget(Target target) {
		targets.add(target);
	}

	/**
	 * Gets the list of targets known to the agent. Override this method with
	 * rules for target discovery or visibility.
	 * 
	 * @return The targets known to the agent
	 */
	public List<Target> getTargets() {
		return targets;
	}

	/**
	 * Informs the module that a given target has been visited.
	 * <p>
	 * NOTE: Maybe not needed? Real agents can filter the known list (targets
	 * mark themselves as non active). However that's unnecesarily inefficient.
	 * On the other hand, the master agent needs to know so it can update the
	 * reward. Maybe this is a good example of the main problem the module
	 * system has: specialized modules may need more input than provided by the
	 * interface. I think the module system should be heavily rethought.
	 * 
	 * @param target
	 *            The visited target
	 * @param reward
	 *            The collected reward
	 */
	public abstract void visited(Target target, double reward);

	/**
	 * Gets a text representation of the module.
	 * 
	 * @return A string representing the module
	 */
	public String toCode() {
		return "";
	}

	/**
	 * Hard copies the module.
	 * 
	 * @return A copy
	 */
	public abstract Sense deepCopy();
}
