/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.comm;

import java.util.HashSet;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.Agent.VisitedMsgInfo;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.Event.Type;

/**
 * Simple Communication scheme. No relay.
 * 
 * @author fran
 *
 */
public class BasicComm extends Communication {
	public BasicComm() {
		known = new HashSet<Agent>();
	}

	@Override
	public void init() {
		send(new Message(agent.getId(), Message.BC, Message.Type.PING, agent));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bu.edu.coverage.coverage_control_sim.comm.Communication#send(bu.edu.coverage
	 * .coverage_control_sim.comm.Message)
	 */
	public void send(Message msg) {
		double time = agent.getDirector().getCurrentTime();
		Event event = new Event(time, time, agent, Type.MESSAGE, msg);
		agent.postEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bu.edu.coverage.coverage_control_sim.comm.Communication#receive(bu.edu
	 * .coverage.coverage_control_sim.comm.Message)
	 */
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
		default: {
			System.err.println(); // FIXME
			break;
		}
		}
	}

	public void processPing(Message msg) {
		known.add((Agent) msg.payload);
	}

	public void processVisited(Message msg) {
		if (agent.getSense() != null) {
			VisitedMsgInfo info = (VisitedMsgInfo) msg.payload;
			agent.getSense().visited(info.target, info.reward);
		}
	}

	public void processControl(Message msg) {
		if (agent.getControl() != null) {
			agent.getControl().setHeading((Double) msg.payload);
		}
	}

}
