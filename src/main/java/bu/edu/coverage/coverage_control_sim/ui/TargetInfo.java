/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import bu.edu.coverage.coverage_control_sim.actor.Target;
import bu.edu.coverage.coverage_control_sim.control.DeadlineDiscount;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class TargetInfo extends ActorInfo {
	public static final String X = "x";
	public static final String Y = "y";
	public static final String IREWARD = "Lambda";
	public static final String CREWARD = "Reward";
	public static final String ALPHA = "Alpha";
	public static final String BETA = "Beta";
	public static final String D = "D";

	static protected TargetInfo instance = new TargetInfo();

	protected Target t;

	protected TargetInfo() {
		addInfoPair(X);
		addInfoPair(Y);
		addInfoPair(IREWARD);
		addInfoPair(CREWARD);
		addInfoPair(ALPHA);
		addInfoPair(BETA);
		addInfoPair(D);
	}

	static public TargetInfo getTargetInfo(Target a, Tableau t) {
		instance.setTarget(a);
		instance.setTableau(t);
		return instance;
	}

	protected void setTarget(Target t) {
		this.t = t;
	}

	public void update() {
		fields.get(ID).setText("" + t.getId());
		fields.get(X).setText("" + t.getPos().x);
		fields.get(Y).setText("" + t.getPos().y);
		fields.get(IREWARD).setText("" + t.getIReward());
		fields.get(CREWARD).setText(
				"" + t.getReward(tableau.getDirector().getCurrentTime()));

		// FIXME do generic
		DeadlineDiscount disc = (DeadlineDiscount) t.getDiscount();
		fields.get(ALPHA).setText("" + disc.alpha);
		fields.get(BETA).setText("" + disc.beta);
		fields.get(D).setText("" + disc.d);
	}

	@Override
	public void set() {
		double x = Double.parseDouble(fields.get(X).getText());
		double y = Double.parseDouble(fields.get(Y).getText());
		double ireward = Double.parseDouble(fields.get(IREWARD).getText());
		double alpha = Double.parseDouble(fields.get(ALPHA).getText());
		double beta = Double.parseDouble(fields.get(BETA).getText());
		double d = Double.parseDouble(fields.get(D).getText());
		t.setPos(new Point(x, y));
		t.setIReward(ireward);
		t.setDiscount(new DeadlineDiscount(alpha, beta, d));
	}
}
