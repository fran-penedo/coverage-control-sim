/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.comm;

import java.util.Collection;
import java.util.HashMap;

import bu.edu.coverage.coverage_control_sim.actor.Agent;

/**
 * Base abstract class for communication modules. Will handle sending and
 * receiving messages. This class maintains a list of known agents which will
 * serve as a routing table for protocols without relay. A more complex protocol
 * will probably need a channel actor to handle ad hoc wireless communications
 * (wired communications could be difficult to do in this simulator).
 * 
 * NOTE: I feel this is not a good design. When extending someone is
 * going to need something extending from agents and it will need casts.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 */
public abstract class Communication {
	protected Agent agent; // owner
	protected HashMap<Integer, Agent> known; // neighbors

	/**
	 * Sets the onwer of this module. Called from the agent setter
	 * 
	 * @param a
	 *            The owner agent
	 */
	public void attach(Agent a) {
		this.agent = a;
	}

	/**
	 * Initializes the module
	 */
	public void init() {
		this.known = new HashMap<Integer, Agent>();
	}

	/**
	 * Sends a message.
	 * 
	 * @param msg
	 *            The message
	 */
	public abstract void send(Message msg);

	/**
	 * Receives a message.
	 * 
	 * @param msg
	 *            The message
	 */
	public abstract void receive(Message msg);

	/**
	 * Adds a neighbor. Intended to handle agent discovery after init phase.
	 * 
	 * @param agent
	 *            The new neighbor
	 */
	public void addAgent(Agent agent) {
		known.put(agent.getId(), agent);
	}

	/**
	 * Gets a list of neighbors.
	 * 
	 * @return A list of neighbors
	 */
	public Collection<Agent> getAgents() {
		return known.values();
	}

	/**
	 * Gets a text representation of the module.
	 * 
	 * @return A string representing the module
	 */
	public String toCode() {
		return "";
	}

	/**
	 * Hard copies the module.
	 * 
	 * @return A copy
	 */
	public abstract Communication deepCopy();
}
