/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * Information panel for an agent.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class AgentInfo extends ActorInfo {
	private static final long serialVersionUID = 1L;

	// Labels
	protected static final String X = "x";
	protected static final String Y = "y";
	protected static final String V = "v";
	protected static final String HEAD = "head";

	// Singleton instance
	static protected AgentInfo instance = new AgentInfo();

	// Referring agent
	protected Agent a;

	protected AgentInfo() {
		addInfoPair(X);
		addInfoPair(Y);
		addInfoPair(V);
		addInfoPair(HEAD);
	}

	/**
	 * Obtains the info panel for the agent associated to the given tableau.
	 * Only one panel can exist at any given time.
	 * 
	 * @param a
	 *            The Agent
	 * @param t
	 *            The containing tableau
	 * @return The info panel associated to the agent
	 */
	static public AgentInfo getAgentInfo(Agent a, Tableau t) {
		instance.setAgent(a);
		instance.setTableau(t);
		return instance;
	}

	protected void setAgent(Agent a) {
		this.a = a;
	}

	public void update() {
		fields.get(ID).setText("" + a.getId());
		fields.get(X).setText("" + a.getPos().x);
		fields.get(Y).setText("" + a.getPos().y);
		fields.get(V).setText("" + a.getV());
		fields.get(HEAD).setText("" + a.getHeading());
	}

	@Override
	public void set() {
		double x = Double.parseDouble(fields.get(X).getText());
		double y = Double.parseDouble(fields.get(Y).getText());
		double v = Double.parseDouble(fields.get(V).getText());
		double head = Double.parseDouble(fields.get(HEAD).getText());
		a.setPos(new Point(x, y));
		a.setV(v);
		a.setHeading(head);
	}
}
