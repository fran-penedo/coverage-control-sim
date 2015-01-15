/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import bu.edu.coverage.coverage_control_sim.actor.MasterAgent;
import bu.edu.coverage.coverage_control_sim.control.KLCRH;
import bu.edu.coverage.coverage_control_sim.sense.MasterSense;

/**
 * @author fran
 *
 */
public class MasterInfo extends ActorInfo {
	public static final String K = "K";
	public static final String DELTA = "delta";
	public static final String B = "b";
	public static final String J = "J";
	public static final String TIME = "Time";

	static protected MasterInfo instance = new MasterInfo();

	protected MasterAgent m;

	protected MasterInfo() {
		addInfoPair(K);
		addInfoPair(DELTA);
		addInfoPair(B);
		addInfoPair(J);
		addInfoPair(TIME);
	}

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
		MasterSense s = (MasterSense) m.getSense();
		fields.get(J).setText("" + s.getReward());
		fields.get(TIME).setText("" + tableau.getDirector().getCurrentTime());

	}

	@Override
	public void set() {
		int k = Integer.parseInt(fields.get(K).getText());
		double delta = Double.parseDouble(fields.get(DELTA).getText());
		int b = Integer.parseInt(fields.get(B).getText());

		KLCRH c = (KLCRH) m.getControl();
		c.setK(k);
		c.setDelta(delta);
		c.setB(b);
	}
}