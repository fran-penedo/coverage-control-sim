/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import bu.edu.coverage.coverage_control_sim.event.UpdateEvent;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public abstract class MovingActor extends Actor {
	/**
	 * Velocity in m/s
	 */
	protected double v;
	protected double heading;
	/**
	 * @param id
	 */
	public MovingActor(int id, Director director) {
		super(id, director);

		v = 0;
	}

	/**
	 * Moves t seconds
	 * 
	 * @param t
	 *            time
	 * 
	 *            FIXME boundaries, collisions?
	 */
	protected void move(double t) {
		Point vel = getVelocity();
		double x = this.p.x + t * vel.x;
		double y = this.p.y + t * vel.y;
		this.p = new Point(x, y);
	}

	protected Point getVelocity() {
		return new Point(v * Math.cos(heading), v * Math.sin(heading));
	}

	@Override
	public void fire(UpdateEvent e) {
		double last = this.last_update;

		super.fire(e);
		this.move(e.due - last);
	}

}
