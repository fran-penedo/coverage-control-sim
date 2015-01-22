/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

/**
 * A discount function. Analytic properties of the function are not enforced.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public interface Discount {

	/**
	 * Evaluates the discount function at time t.
	 * 
	 * @param t
	 *            The time
	 * @return The evaluation of the function
	 */
	public double eval(double t);

	/**
	 * Gets a deadline time.
	 * 
	 * FIXME what if the function has no deadline?
	 * FIXME min(D, T)
	 * 
	 * @return The deadline associated with this function
	 */
	public double getDeadline();

	/**
	 * Gets a text representation of the module.
	 * 
	 * @return A string representing the module
	 */
	public String toCode();
}
