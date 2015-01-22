/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

/**
 * A discount function of the form:
 * 
 * FIXME
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class DeadlineDiscount implements Discount {

	/**
	 * Alpha parameter. Controls the reward lost at deadline.
	 */
	public final double alpha;
	/**
	 * Beta parameter. Defines the exponential rate of discount after the
	 * deadline.
	 */
	public final double beta;
	/**
	 * Deadline time.
	 */
	public final double d;

	/**
	 * Creates a deadline discount function with the given parameters.
	 * 
	 * @param alpha
	 * @param beta
	 * @param d
	 */
	public DeadlineDiscount(double alpha, double beta, double d) {
		if (alpha < 0 || alpha > 1) {
			throw new IllegalArgumentException("Alpha should be in [0, 1]");
		}
		if (beta < 0) {
			throw new IllegalArgumentException("Beta should be > 0");
		}
		if (d < 0) {
			throw new IllegalArgumentException("Deadline should be > 0");
		}
		this.alpha = alpha;
		this.beta = beta;
		this.d = d;
	}

	public double eval(double t) {
		if (t <= d) {
			return (1 - alpha * t / d);
		} else {
			return ((1 - alpha) * Math.exp(-beta * (t - d)));
		}
	}

	public double getDeadline() {
		return d;
	}

	public String toCode() {
		return "Deadline " + alpha + " " + beta + " " + d;
	}
}
