/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Graphics;

import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;
import bu.edu.coverage.coverage_control_sim.ui.actorinfo.ActorInfo;
import bu.edu.coverage.coverage_control_sim.ui.actorinfo.MasterInfo;

/**
 * A hidden agent intended for simulation bookkeeping and taking the agent
 * leader role in simplified scenarios with no leader selection.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class MasterAgent extends Agent {

	/**
	 * Creates a master agent and adds it to the given director.
	 * 
	 * @param director
	 *            The director to add the master agent to
	 */
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
