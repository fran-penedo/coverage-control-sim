/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

/**
 * @author fran
 *
 */
public class DeadlineDiscount implements Discount {

	public final double alpha; // reward lost at deadline
	public final double beta; // exponential rate after deadline
	public final double d; // deadline

	/**
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

	@Override
	public String toCode() {
		return "Deadline " + alpha + " " + beta + " " + d;
	}
}
