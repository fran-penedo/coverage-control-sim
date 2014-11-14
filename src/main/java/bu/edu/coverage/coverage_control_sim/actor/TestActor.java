package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import bu.edu.coverage.coverage_control_sim.util.Point;

public class TestActor extends MovingActor {

	public TestActor(int id, Director director) {
		super(id, director);

		p = new Point(10, 10);
		v = 5;
		heading = Math.PI / 4;
		size = new Point(20, 20);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		/*
		 * g2d.translate(50, 50);
		 * g2d.rotate(Math.toRadians(45));
		 * g2d.drawString("X", 50, 50);
		 */
		Ellipse2D circle = new Ellipse2D.Double(0, 0, 20, 20);
		g2d.fill(circle);
		// g2d.drawString("X", 10, 10);
	}

	@Override
	public String toString() {
		return "Moving";
	}
}
