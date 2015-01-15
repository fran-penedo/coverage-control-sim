/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

/**
 * @author fran
 *
 */
public interface Discount {

	public double eval(double t);

	// FIXME min(D, T)
	public double getDeadline();

	public String toCode();
}
