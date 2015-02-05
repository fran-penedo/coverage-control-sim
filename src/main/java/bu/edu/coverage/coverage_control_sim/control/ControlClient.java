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
 * A control module for agents listening to the control signals obtained with
 * a centralized algorithm.
 * <p>
 * NOTE: Init time is now extremely ugly, not sure about a better way to do it.
 * Maybe ACK message from server/master so it can retry later.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class ControlClient extends Control {

	@Override
	public void init() {
		double now = agent.getDirector().getCurrentTime();
		agent.postEvent(new Event(now, now + 0.1, agent, EType.CONTROL));
	}

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

	@Override
	public Control deepCopy() {
		return new ControlClient();
	}

}
