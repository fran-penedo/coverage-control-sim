/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.comm;

import java.util.Collection;
import java.util.HashMap;

import bu.edu.coverage.coverage_control_sim.actor.Agent;

/**
 * @author fran
 * 
 *         NOTE: I feel this is not a good design. When extending someone is
 *         going to need something extending from agents and it will need casts.
 *
 */
public abstract class Communication {
	protected Agent agent;
	protected HashMap<Integer, Agent> known;

	public Communication() {
		this.known = new HashMap<Integer, Agent>();
	}

	public void attach(Agent a) {
		this.agent = a;
	}

	public abstract void init();

	public abstract void send(Message msg);

	public abstract void receive(Message msg);

	public void addAgent(Agent agent) {
		known.put(agent.getId(), agent);
	}

	public Collection<Agent> getAgents() {
		return known.values();
	}
}
