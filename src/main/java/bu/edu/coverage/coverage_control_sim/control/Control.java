/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

import bu.edu.coverage.coverage_control_sim.actor.Agent;

/**
 * @author fran
 *
 */
public abstract class Control {
	protected Agent agent;

	public void attach(Agent a) {
		this.agent = a;
	}

	public abstract void init();

	public abstract void control();

	public abstract void setHeading(double heading);

	/**
	 * 
	 * NOTE: I'm justifying this as a common method for Control as a kind of
	 * asynchronous input (unlike other inputs that it can get from other
	 * modules)
	 * 
	 * @param agent
	 *            an agent neighbour
	 */
	public abstract void addNeighbor(Agent agent);

	public String toCode() {
		return "";
	}

	public abstract Control deepCopy();

}
