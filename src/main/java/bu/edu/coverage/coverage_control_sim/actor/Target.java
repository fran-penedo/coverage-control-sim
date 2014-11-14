/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import bu.edu.coverage.coverage_control_sim.control.Discount;
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

	/**
	 * @param id
	 * @param director
	 */
	public Target(int id, Director director, Discount discount, double ireward,
			double size) {
		super(id, director);
		this.discount = discount;
		this.ireward = ireward;
		this.size = new Point(size, size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see coverage.actor.Actor#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Color color = Painter.getMixedColor(ACTIVE_COLOR, INACTIVE_COLOR,
				getReward(last_update) / ireward);
		Painter.drawStar((Graphics2D) g, p, size.x, color);
	}

	public double getReward(double t) {
		return ireward * discount.eval(t);
	}

	public static class TargetInfo {
		public Point p;
		public Point size;
		public Discount discount;
		public double ireward;

		/**
		 * @param p
		 * @param size
		 * @param discount
		 * @param ireward
		 */
		public TargetInfo(Point p, Point size, Discount discount, double ireward) {
			this.p = p;
			this.size = size;
			this.discount = discount;
			this.ireward = ireward;
		}

	}
}
