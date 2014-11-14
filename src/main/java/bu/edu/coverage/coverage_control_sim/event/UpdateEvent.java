/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.event;

import bu.edu.coverage.coverage_control_sim.actor.Actor;

/**
 * @author fran
 *
 */
public class UpdateEvent extends Event {

	/**
	 * @param due
	 * @param actor
	 */
	public UpdateEvent(double start, double due, Actor actor) {
		super(start, due, actor);
	}

	@Override
	public String toString() {
		return super.toString() + "Update. ";
	}

	@Override
	public void dispatch() {
		actor.fire(this);
	}

}
