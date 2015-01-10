/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

/**
 * @author fran
 *
 */
public class KLCRHClient extends Control {

	@Override
	public void init() {
		// No need
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.control.Control#control()
	 */
	@Override
	public void control() {
		// No need for periodic control

	}

	@Override
	public void setHeading(double heading) {
		agent.setHeading(heading);

	}

}
