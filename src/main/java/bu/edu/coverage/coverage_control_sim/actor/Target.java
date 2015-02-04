/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import bu.edu.coverage.coverage_control_sim.control.Discount;
import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.Event.EType;
import bu.edu.coverage.coverage_control_sim.ui.ActorComponent;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;
import bu.edu.coverage.coverage_control_sim.ui.actorinfo.ActorInfo;
import bu.edu.coverage.coverage_control_sim.ui.actorinfo.TargetInfo;
import bu.edu.coverage.coverage_control_sim.util.Painter;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * A moving target with a one time reward.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class Target extends MovingActor {
	/**
	 * Color when full active.
	 */
	public static final Color ACTIVE_COLOR = Color.red;
	/**
	 * Color when full inactive.
	 */
	public static final Color INACTIVE_COLOR = Color.gray;

	/**
	 * The discount function of this target.
	 */
	protected Discount discount;
	/**
	 * The initial reward of this target.
	 */
	protected double ireward;

	protected boolean active;

	/**
	 * Creates a target in the given director, in the position p, with size
	 * size, speed v, heading heading, discount function discount, initial
	 * reward ireward and possibly inactive.
	 * 
	 * @param director
	 *            The director to add this target to
	 * @param p
	 *            The position of the target
	 * @param size
	 *            The size of the target. It will be a square
	 * @param v
	 *            The speed of the target
	 * @param heading
	 *            The heading of the target
	 * @param discount
	 *            The discount function of the target
	 * @param ireward
	 *            The initial reward of the target
	 * @param active
	 *            The initial state of the target
	 */
	public Target(Director director, Point p, double size, double v,
			double heading, Discount discount, double ireward, boolean active) {
		super(director, p, new Point(size, size), v, heading);
		this.discount = discount;
		this.ireward = ireward;
		this.active = active;
	}

	/**
	 * Copies the target and adds the copy to the given director.
	 * 
	 * @param t
	 *            The target to copy
	 * @param director
	 *            The director to add the copy to
	 */
	public Target(Target t, Director director) {
		this(director, t.getPos(), t.getSize().x, t.getV(), t.getHeading(),
				t.discount, t.ireward, t.active);
	}

	@Override
	public void init() {
		// Broadcast target existence. I think this is ugly...
		for (Actor a : director.getActors()) {
			postEvent(new Event(director.getCurrentTime(),
					director.getCurrentTime(), a, EType.TARGET, this));
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

	// Handles an agent visiting the target
	protected void visitEvent(Event e) {
		if (isActive()) { // Only if the agent visiting is the first
			setActive(false);
			Agent a = (Agent) e.payload;
			// Confirm to agent that he has visited the target
			VisitedEventInfo info = new VisitedEventInfo(this,
					getReward(director.getCurrentTime()));
			postEvent(new Event(director.getCurrentTime(),
					director.getCurrentTime(), a, EType.VISITED, info));
		}
	}

	/**
	 * Computes if a point is in range to visit the target.
	 * 
	 * @param point
	 *            A point
	 * @return If the point is in range
	 */
	public boolean inRange(Point point) {
		return this.getPos().dist(point) <= getSize().x / 2;
	}

	/**
	 * Gets the reward corresponding to the given time from this target. It will
	 * only return the reward, the target will not change its state.
	 * 
	 * @param t
	 *            The time for computing the reward
	 * @return The reward corresponding to the time
	 */
	public double getReward(double t) {
		return ireward * discount.eval(t);
	}

	/**
	 * Gets the active status.
	 * 
	 * @return The active status
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active status.
	 * 
	 * @param active
	 *            The new active status
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the initial reward.
	 * 
	 * @return The initial reward
	 */
	public double getIReward() {
		return ireward;
	}

	/**
	 * Sets the initial reward.
	 * 
	 * @param ireward
	 *            The initial reward
	 */
	public void setIReward(double ireward) {
		this.ireward = ireward;
	}

	/**
	 * Gets the discount function.
	 * 
	 * @return The discount function
	 */
	public Discount getDiscount() {
		return discount;
	}

	/**
	 * Sets the discount function.
	 * 
	 * @param discount
	 *            The discount function
	 */
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	/**
	 * Payload for visited events
	 * 
	 * @author Francisco Penedo (franp@bu.edu)
	 *
	 */
	public class VisitedEventInfo {
		/**
		 * The target visited.
		 */
		public final Target target;
		/**
		 * The reward collected.
		 */
		public final double reward;

		/**
		 * Creates the payload for a visited event.
		 * 
		 * @param target
		 *            The target visited
		 * @param reward
		 *            The reward collected
		 */
		public VisitedEventInfo(Target target, double reward) {
			this.target = target;
			this.reward = reward;
		}
	}

	@Override
	public String toString() {
		return getPos().toString();
	}

	@Override
	public String toCode() {
		return super.toCode() + " " + ireward + " " + discount.toCode();
	}

	@Override
	public Actor deepCopy(Director d) {
		return new Target(this, d);
	}

	@Override
	public ActorInfo getInfoPanel(Tableau tableau) {
		return TargetInfo.getTargetInfo(this, tableau);
	}

	@Override
	public void paint(Graphics g) {
		Color color = isActive() ? Painter.getMixedColor(ACTIVE_COLOR,
				INACTIVE_COLOR, getReward(last_update) / ireward)
				: INACTIVE_COLOR;
		Painter.drawTarget((Graphics2D) g, ActorComponent.drawSize(size.x), id,
				color);
	}

	@Override
	public Integer getLayer() {
		return Tableau.TARGET_LAYER;
	}

}
