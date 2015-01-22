/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * An actor that can move by itself. Always use getPos() to retrieve position,
 * as it performs the computation of the real position at the current time.
 * 
 * NOTE: This could be non abstract as well.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public abstract class MovingActor extends Actor {
	protected double v; // speed in m/s
	protected double heading; // heading in radians
	protected double last_update; // movement base position

	/**
	 * Creates a moving actor in the given director, with position p, size size,
	 * speed v, and heading heading.
	 * 
	 * @param director
	 *            The director to add the actor to
	 * @param p
	 *            The position of the actor
	 * @param size
	 *            The size of the actor
	 * @param v
	 *            The speed of the actor in m/s
	 * @param heading
	 *            The heading of the actor in radians
	 */
	public MovingActor(Director director, Point p, Point size, double v,
			double heading) {
		super(director, p, size);
		this.v = v;
		this.heading = heading;
		last_update = director.getCurrentTime();
	}

	/**
	 * Creates a copy of this actor and adds the copy to the given director.
	 * 
	 * @param director
	 *            The director to add the copy to
	 */
	public MovingActor(Director director) {
		this(director, new Point(100, 100), new Point(50, 50), 0, 0);
	}

	// Gets the velocity vector
	protected Point getVelocity() {
		return new Point(v * Math.cos(heading), v * Math.sin(heading));
	}

	@Override
	public Point getPos() {
		double t = getDirector().getCurrentTime() - last_update;
		Point vel = getVelocity();
		double x = this.p.x + t * vel.x;
		double y = this.p.y + t * vel.y;
		return new Point(x, y);
	}

	@Override
	public void setPos(Point npos) {
		super.setPos(npos);
		last_update = getDirector().getCurrentTime();
	}

	/**
	 * Gets the speed.
	 * 
	 * @return The speed
	 */
	public double getV() {
		return v;
	}

	/**
	 * Sets the speed.
	 * 
	 * @param v
	 *            The new speed.
	 */
	public void setV(double v) {
		setPos(getPos());
		this.v = v;
	}

	/**
	 * Gets the heading.
	 * 
	 * @return The heading.
	 */
	public double getHeading() {
		return heading;
	}

	/**
	 * Sets the heading.
	 * 
	 * @param heading
	 *            The new heading
	 */
	public void setHeading(double heading) {
		setPos(getPos());
		this.heading = heading;
	}

	@Override
	public String toCode() {
		return super.toCode() + " " + v + " " + heading;
	}

}
