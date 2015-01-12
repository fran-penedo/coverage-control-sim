/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.event.Event;
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

	public MovingActor(Director director, Point p, Point size, double v,
			double heading) {
		super(director, p, size);
		this.v = v;
		this.heading = heading;
	}

	/**
	 * @param id
	 */
	public MovingActor(Director director) {
		this(director, new Point(100, 100), new Point(50, 50), 0, 0);
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

	public double getV() {
		return v;
	}

	@Override
	protected void updateEvent(Event e) {
		double last = this.last_update;

		super.updateEvent(e);
		this.move(e.due - last);
	}

}
