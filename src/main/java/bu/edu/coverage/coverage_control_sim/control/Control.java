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

}
