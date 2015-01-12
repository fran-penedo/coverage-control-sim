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

	// PI correction for inverted Y axis
	public double headTo(Point b) {
		return Math.atan2((this.y - b.y), (this.x - b.x)) + Math.PI;
	}

	public Point unitary() {
		return scale(1 / dist(ZERO));
	}

	public Point scale(double s) {
		return new Point(x * s, y * s);
	}

	public Point diff(Point b) {
		return new Point(x - b.x, y - b.x);
	}

	public Point unitDiff(Point b) {
		return diff(b).unitary();
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public Point add(Point b) {
		return new Point(x + b.x, y + b.y);
	}
}
