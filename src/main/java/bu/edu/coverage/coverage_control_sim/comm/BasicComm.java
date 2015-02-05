/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.comm;

import bu.edu.coverage.coverage_control_sim.actor.Actor;
import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.Agent.VisitedMsgInfo;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.Event.EType;
import bu.edu.coverage.coverage_control_sim.util.Debug;

/**
 * Simple Communication scheme. No relay.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class BasicComm extends Communication {

	@Override
	public void init() {
		super.init();
		double now = agent.getDirector().getCurrentTime();
		for (Actor a : agent.getDirector().getActors()) {
			agent.postEvent(new Event(now, now, a, EType.AGENT, agent));
		}
	}

	public void send(Message msg) {
		if (msg.to == Message.BC) {
			for (Agent a : known.values()) {
				sendSingle(new Message(msg.from, a.getId(), msg.type,
						msg.payload));
			}
		} else if (known.containsKey(msg.to)) {
			sendSingle(msg);
		}
	}

	// Send to one
	protected void sendSingle(Message msg) {
		double time = agent.getDirector().getCurrentTime();
		Event event = new Event(time, time, known.get(msg.to), EType.MESSAGE,
				msg);
		agent.postEvent(event);
	}

	public void receive(Message msg) {
		if (msg.to == Message.BC || msg.to == agent.getId()) {
			process(msg);
		}
	}

	protected void process(Message msg) {
		switch (msg.type) {
		case PING: {
			processPing(msg);
			break;
		}
		case VISITED: {
			processVisited(msg);
			break;
		}
		case CONTROL: {
			processControl(msg);
			break;
		}
		case JOIN_CONTROL: {
			processJoinControl(msg);
			break;
		}
		default: {
			Debug.debug("Processing unkown message " + msg);
			break;
		}
		}
	}

	protected void processPing(Message msg) {
		Agent a = (Agent) msg.payload;
		known.put(a.getId(), a);
	}

	protected void processVisited(Message msg) {
		if (agent.getSense() != null) {
			VisitedMsgInfo info = (VisitedMsgInfo) msg.payload;
			agent.getSense().visited(info.target, info.reward);
		}
	}

	protected void processControl(Message msg) {
		if (agent.getControl() != null) {
			agent.getControl().setHeading((Double) msg.payload);
		}
	}

	protected void processJoinControl(Message msg) {
		if (agent.getControl() != null) {
			agent.getControl().addNeighbor((Agent) msg.payload);
		}
	}

	@Override
	public Communication deepCopy() {
		return new BasicComm();
	}

}
