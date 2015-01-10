/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import bu.edu.coverage.coverage_control_sim.control.Discount;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.Event.Type;
import bu.edu.coverage.coverage_control_sim.util.Painter;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class Target extends MovingActor {
	public static final Color ACTIVE_COLOR = Color.red;
	public static final Color INACTIVE_COLOR = Color.gray;

	public Discount discount;
	public double ireward;

	protected boolean active;

	public Target(Director director, Point p, double size, double v,
			double heading, Discount discount, double ireward) {
		super(director, p, new Point(size, size), v, heading);
		this.discount = discount;
		this.ireward = ireward;
		this.active = true;
	}

	public Target(Target t, Director director) {
		this(director, t.p, t.size.x, t.v, t.heading, t.discount, t.ireward);
	}

	@Override
	public void init() {
		// Broadcast target existence. I think this is ugly...
		for (Actor a : director.getActors()) {
			postEvent(new Event(director.getCurrentTime(),
					director.getCurrentTime(), a, Type.TARGET, this));
		}
	}

	@Override
	public void fire(Event e) {
		switch (e.type) {
		case VISIT: {
			visitEvent(e);
			break;
		}
		default: {
			super.fire(e);
			break;
		}
		}

	}

	protected void visitEvent(Event e) {
		if (isActive()) {
			setActive(false);
			Agent a = (Agent) e.payload;
			VisitEventInfo info = new VisitEventInfo(this,
					getReward(director.getCurrentTime()));
			postEvent(new Event(director.getCurrentTime(),
					director.getCurrentTime(), a, Type.VISITED, info));
		}
	}

	public boolean inRange(Point point) {
		return this.p.dist(point) <= size.x / 2;
	}

	public double getReward(double t) {
		return ireward * discount.eval(t);
	}

	public boolean isActive() {
		return active;
	}

	public void visit() {
		active = false;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see coverage.actor.Actor#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Color color = isActive() ? Painter.getMixedColor(ACTIVE_COLOR,
				INACTIVE_COLOR, getReward(last_update) / ireward)
				: INACTIVE_COLOR;
		Painter.drawStar((Graphics2D) g, p, size.x, color);
	}

	public class VisitEventInfo {
		public final Target target;
		public final double reward;

		public VisitEventInfo(Target target, double reward) {
			this.target = target;
			this.reward = reward;
		}
	}

}
