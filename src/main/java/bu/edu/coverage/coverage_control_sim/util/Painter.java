/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * @author fran
 *
 */
public class Painter {

	public static final Stroke thickStroke = new BasicStroke(5.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static final Stroke thinStroke = new BasicStroke(1.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static final Stroke mediumStroke = new BasicStroke(3.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static final int DIRECTION_POINTER_SIZE = 6;

	/**
	 * 
	 */
	public Painter() {
		// TODO Auto-generated constructor stub
	}

	public static void drawStar(Graphics2D g, Point center, double size,
			Color color) {
		Stroke pstroke = g.getStroke();
		Color pcolor = g.getColor();
		g.setStroke(mediumStroke);
		g.setColor(color);

		double x = center.x - size / 3.0;  // make the center of the star the
											// event location. 3.0 is empirical.
											// I am too lazy to find the exact
											// value.
		double y = center.y + size / 3.0;
		double angle = Math.PI;
		double xNext;
		double yNext;

		for (int i = 0; i < 5; i++) {
			xNext = x + size * Math.cos(angle);
			yNext = y + size * Math.sin(angle);
			g.drawLine((int) x, (int) y, (int) xNext, (int) yNext);
			x = xNext;
			y = yNext;
			angle = angle + 0.8 * Math.PI;
			xNext = x + size * Math.cos(angle);
			yNext = y + size * Math.sin(angle);
			g.drawLine((int) x, (int) y, (int) xNext, (int) yNext);
			x = xNext;
			y = yNext;
			angle = angle - 0.4 * Math.PI;
		}

		g.setStroke(pstroke);
		g.setColor(pcolor);
	}

	public static void drawAgent(Graphics2D g, Point center, Point size,
			double heading, int id, Color color) {
		Color pcolor = g.getColor();
		g.setColor(color);

		int x = (int) center.x;
		int y = (int) center.y;
		int w = (int) size.x;
		int h = (int) size.y;

		g.fillOval(x - w / 2, y - h / 2, w, h);
		g.setColor(Color.black);
		g.drawOval(x - w / 2, y - h / 2, w, h);
		g.drawString("" + id, x - g.getFontMetrics().stringWidth("" + id) / 2,
				y + 4);

		g.setColor(Color.black);

		g.fillOval(x + (int) (Math.cos(heading) * w / 2)
				- DIRECTION_POINTER_SIZE / 2, y
				+ (int) (Math.sin(heading) * h / 2) - DIRECTION_POINTER_SIZE
				/ 2, DIRECTION_POINTER_SIZE, DIRECTION_POINTER_SIZE);

		g.setColor(pcolor);
	}

	public static Color getMixedColor(Color a, Color b, double rate) {
		if (rate < 0) {
			System.err.println("Rate < 0");
			return b;
		} else if (rate > 1) {
			System.err.println("Rate > 1");
			return a;
		} else {
			return new Color(
					(int) (rate * a.getRed() + (1 - rate) * b.getRed()),
					(int) (rate * a.getGreen() + (1 - rate) * b.getGreen()),
					(int) (rate * a.getBlue() + (1 - rate) * b.getBlue()));
		}
	}
}
