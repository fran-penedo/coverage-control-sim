/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.comm;

import java.util.HashSet;

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
	protected HashSet<Agent> known;

	public void attach(Agent a) {
		this.agent = a;
	}

	public abstract void init();

	public abstract void send(Message msg);

	public abstract void receive(Message msg);

	/**
	 * @return the known
	 */
	public HashSet<Agent> getKnown() {
		return known;
	}
}
