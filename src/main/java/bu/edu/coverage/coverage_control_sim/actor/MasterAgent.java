/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Graphics;

import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.ui.ActorInfo;
import bu.edu.coverage.coverage_control_sim.ui.MasterInfo;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;

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
	public Actor deepCopy(Director d) {
		MasterAgent master = new MasterAgent(d);
		if (sense != null) {
			master.setSense(sense.deepCopy());
		}
		if (comm != null) {
			master.setCommunication(comm.deepCopy());
		}
		if (control != null) {
			master.setControl(control.deepCopy());
		}
		return master;
	}

	@Override
	public ActorInfo getInfoPanel(Tableau tableau) {
		return MasterInfo.getMasterInfo(this, tableau);
	}
}
