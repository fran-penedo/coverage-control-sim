/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.util;

/**
 * Linear algebra subroutines (very basic and ad hoc)
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class Linear {

	/**
	 * Solves a 2x2 linar system for the first variable.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return The first variable of the solution. If the system has rank < 2,
	 *         returns positive infinity
	 */
	public static double solve1(Point a, Point b, Point c) {
		double det = a.x * b.y - b.x * a.y;
		if (det == 0) {
			return Double.POSITIVE_INFINITY;
		}
		return (c.x * b.y - b.x * c.y) / det;
	}

}
