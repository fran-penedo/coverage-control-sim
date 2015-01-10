/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Graphics;

/**
 * @author fran
 *
 */
public class MasterAgent extends Agent {

	public MasterAgent(Director director) {
		super(director);
		v = 0;
	}

	@Override
	public void paint(Graphics g) {
		// hidden
	}

	@Override
	public void init() {

	}
}
