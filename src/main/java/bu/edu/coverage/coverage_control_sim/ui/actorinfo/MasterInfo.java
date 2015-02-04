/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui.actorinfo;

import bu.edu.coverage.coverage_control_sim.actor.MasterAgent;
import bu.edu.coverage.coverage_control_sim.control.KLCRH;
import bu.edu.coverage.coverage_control_sim.sense.MasterSense;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;

/**
 * Information panel for a master agent. Will pause the simulation when no more
 * targets are active.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class MasterInfo extends ActorInfo {
	private static final long serialVersionUID = 1L;

	// Labels
	protected static final String K = "K";
	protected static final String DELTA = "delta";
	protected static final String B = "b";
	protected static final String GAMMA = "gamma";
	protected static final String ACTIONH = "Enable Action H";
	protected static final String J = "J";
	protected static final String TIME = "Time";

	// Singleton instance
	static protected MasterInfo instance = new MasterInfo();

	// Referring master agent
	protected MasterAgent m;

	protected MasterInfo() {
		addInfoPair(K);
		addInfoPair(DELTA);
		addInfoPair(B);
		addInfoPair(GAMMA);
		addInfoPair(ACTIONH);
		addInfoPair(J);
		addInfoPair(TIME);
	}

	/**
	 * Obtains the info panel for the master agent associated to the given
	 * tableau. Only one panel can exist at any given time.
	 * 
	 * @param a
	 *            The master agent
	 * @param t
	 *            The containing tableau
	 * @return The info panel associated to the master agent
	 */
	static public MasterInfo getMasterInfo(MasterAgent a, Tableau t) {
		instance.setMaster(a);
		instance.setTableau(t);
		return instance;
	}

	protected void setMaster(MasterAgent a) {
		this.m = a;
	}

	public void update() {
		fields.get(ID).setText("" + m.getId());

		// FIXME generic
		KLCRH c = (KLCRH) m.getControl();
		fields.get(K).setText("" + c.getK());
		fields.get(DELTA).setText("" + c.getDelta());
		fields.get(B).setText("" + c.getB());
		fields.get(GAMMA).setText("" + c.getGamma());
		fields.get(ACTIONH).setText("" + c.getActionHEnabled());
		MasterSense s = (MasterSense) m.getSense();
		fields.get(J).setText("" + s.getReward());
		fields.get(TIME).setText("" + tableau.getDirector().getCurrentTime());

		// Pauses the simulation if no more targets are active.
		// NOTE: Not sure how I feel about this
		if (s.getTargets() != null && s.getTargets().isEmpty()) {
			tableau.pause();
		}

	}

	@Override
	public void set() {
		int k = Integer.parseInt(fields.get(K).getText());
		double delta = Double.parseDouble(fields.get(DELTA).getText());
		int b = Integer.parseInt(fields.get(B).getText());
		double gamma = Double.parseDouble(fields.get(GAMMA).getText());
		boolean actionh = Boolean.parseBoolean(fields.get(ACTIONH).getText());

		KLCRH c = (KLCRH) m.getControl();
		c.setK(k);
		c.setDelta(delta);
		c.setB(b);
		c.setGamma(gamma);
		c.setActionHEnabled(actionh);
	}

	@Override
	public boolean alwaysVisible() {
		return true;
	}
}
