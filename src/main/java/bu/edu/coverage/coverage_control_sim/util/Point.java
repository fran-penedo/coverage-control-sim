/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.util;

/**
 * Probably no need for arithmetic
 * 
 * @author Francisco Penedo
 *
 */
public final class Point {
	public final double x;
	public final double y;

	public static final Point ZERO = new Point(0, 0);

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double dist2(Point b) {
		return (this.x - b.x) * (this.x - b.x) + (this.y - b.y)
				* (this.y - b.y);
	}

	public double dist(Point b) {
		return Math.sqrt(this.dist2(b));
	}

	// TODO check if this is what I want. - for inverted Y axis, PI correction
	// for tangent period
	public double headTo(Point b) {
		return -((this.x - b.x < 0 ? Math.PI : 0) + Math.atan2((this.y - b.y),
				(this.x - b.x)));
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}