/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.util;

/**
 * A 2D point.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public final class Point {
	/**
	 * X coordinate.
	 */
	public final double x;
	/**
	 * Y coordinate.
	 */
	public final double y;

	/**
	 * The (0,0) point.
	 */
	public static final Point ZERO = new Point(0, 0);

	/**
	 * Creates the (x,y) point.
	 * 
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Computes the squared euclidean distance between this point and b.
	 * 
	 * @param b
	 * @return The squared distance
	 */
	public double dist2(Point b) {
		return (this.x - b.x) * (this.x - b.x) + (this.y - b.y)
				* (this.y - b.y);
	}

	/**
	 * Computes the euclidean distance between this point and b.
	 * 
	 * @param b
	 * @return The distance
	 */
	public double dist(Point b) {
		return Math.sqrt(this.dist2(b));
	}

	/**
	 * Computes the heading needed to go from this to b.
	 * 
	 * @param b
	 * @return The heading from this to b
	 */
	public double headTo(Point b) {
		// PI correction for inverted Y axis? Not sure why do I need it.
		return Math.atan2((this.y - b.y), (this.x - b.x)) + Math.PI;
	}

	/**
	 * Computes the unitary vector in the direction of this.
	 * 
	 * @return The unitary vector from this
	 */
	public Point unitary() {
		return scale(1 / dist(ZERO));
	}

	/**
	 * Computes the product of this point by the scalar s.
	 * 
	 * @param s
	 * @return s times this
	 */
	public Point scale(double s) {
		return new Point(x * s, y * s);
	}

	/**
	 * Computes the difference between this and b.
	 * 
	 * @param b
	 * @return This minus b
	 */
	public Point diff(Point b) {
		return new Point(x - b.x, y - b.x);
	}

	/**
	 * Computes the unitary vector from b to this.
	 * 
	 * @param b
	 * @return Unitary this minus b
	 */
	public Point unitDiff(Point b) {
		return diff(b).unitary();
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	/**
	 * Computes the sum of this and b.
	 * 
	 * @param b
	 * @return This plus b
	 */
	public Point add(Point b) {
		return new Point(x + b.x, y + b.y);
	}

	/**
	 * Creates a text representation of this point.
	 * 
	 * @return A string with a representation of this.
	 */
	public String toCode() {
		return "(" + x + "," + y + ")";
	}
}
