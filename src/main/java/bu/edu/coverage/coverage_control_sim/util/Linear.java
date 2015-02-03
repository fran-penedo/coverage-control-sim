/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.util;

import java.security.InvalidParameterException;

/**
 * Linear algebra subroutines (very basic and ad hoc)
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class Linear {

	/**
	 * Solves a 2x2 linar system for the first variable
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return The first variable of the solution
	 */
	public static double solve1(Point a, Point b, Point c) {
		double det = a.x * b.y - b.x * a.y;
		if (det == 0) { // FIXME this is not good when dealing with double...
			throw new InvalidParameterException("System rank < 2");
		}
		return (c.x * b.y - b.x * c.y) / det;
	}

}
