/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import bu.edu.coverage.coverage_control_sim.control.KLCRH;
import bu.edu.coverage.coverage_control_sim.event.ControlEvent;
import bu.edu.coverage.coverage_control_sim.util.Painter;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class Agent extends MovingActor {

	protected KLCRH control;

	/**
	 * @param id
	 * @param director
	 */
	public Agent(int id, Director director) {
		super(id, director);
		p = new Point(10, 10);
		v = 5;
		heading = Math.PI / 4;
		size = new Point(20, 20);
	}

	public void fire(ControlEvent e) {
		e.control.control(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see coverage.actor.Actor#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Painter.drawAgent((Graphics2D) g, p, size, heading, id, Color.cyan);
	}

	public void setControl(KLCRH control) {
		this.control = control;
		// FIXME don't like how I setup times
		postEvent(new ControlEvent(last_update, last_update, this, control));
	}

	public static class AgentInfo {
		public final Point p;
		public final double v;

		/**
		 * @param p
		 * @param v
		 */
		public AgentInfo(Point p, double v) {
			this.p = p;
			this.v = v;
		}
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}
}
