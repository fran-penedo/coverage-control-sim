/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.util;

/**
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class Debug {
	public static boolean log = true;

	public static void debug(String s) {
		if (log) {
			System.err.println(s);
		}
	}
}
