/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.event;

import bu.edu.coverage.coverage_control_sim.actor.Actor;
import bu.edu.coverage.coverage_control_sim.control.Control;

/**
 * @author fran
 *
 */
public class ControlEvent extends Event {
	public final Control control;

	public ControlEvent(double start, double due, Actor actor, Control control) {
		super(start, due, actor);
		this.control = control;
	}

	@Override
	public void dispatch() {
		actor.fire(this);
	}

	@Override
	public String toString() {
		return super.toString() + "Control. ";
	}
}
