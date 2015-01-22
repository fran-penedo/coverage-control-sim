/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

import bu.edu.coverage.coverage_control_sim.actor.Agent;

/**
 * Base abstract class for control modules. This module will perform all control
 * actions (either executing an algorithm or passively accepting control signals
 * from others). Control actions are defined here (such as setHeading()).
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public abstract class Control {
	protected Agent agent; // owner

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
	public abstract void init();

	/**
	 * Performs a control action in response to a CONTROL event. Used for
	 * periodic actions (such as executing the algorithm periodically or due a
	 * given time).
	 */
	public abstract void control();

	/**
	 * Sets the heading of the owner agent. Used in response to control
	 * messages.
	 * 
	 * @param heading
	 *            The new heading
	 */
	public abstract void setHeading(double heading);

	/**
	 * Adds an agent as a neighbor.
	 * 
	 * NOTE: I'm justifying this as a common method for Control as a kind of
	 * asynchronous input (unlike other inputs that it can get from other
	 * modules)
	 * 
	 * @param agent
	 *            an agent neighbour
	 */
	public abstract void addNeighbor(Agent agent);

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
	public abstract Control deepCopy();

}
