/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.control;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.comm.Message;
import bu.edu.coverage.coverage_control_sim.comm.Message.MType;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.Event.EType;

/**
 * @author fran
 *
 */
public class KLCRHClient extends Control {

	@Override
	public void init() {
		double now = agent.getDirector().getCurrentTime();
		// FIXME init time
		agent.postEvent(new Event(now, now + 0.1, agent, EType.CONTROL));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.control.Control#control()
	 */
	@Override
	public void control() {
		if (agent.getCommunication() != null) {
			agent.getCommunication().send(
					new Message(agent.getId(), Message.BC, MType.JOIN_CONTROL,
							agent));
		}

	}

	@Override
	public void setHeading(double heading) {
		agent.setHeading(heading);

	}

	@Override
	public void addNeighbor(Agent agent) {
		// No need for neighbors

	}

}
