/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * A class with painting methods
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class Painter {

	/**
	 * Thick stroke.
	 */
	public static final Stroke thickStroke = new BasicStroke(5.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	/**
	 * Thin stroke.
	 */
	public static final Stroke thinStroke = new BasicStroke(1.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	/**
	 * Medium stroke.
	 */
	public static final Stroke mediumStroke = new BasicStroke(3.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	/**
	 * Size of the heading indicator.
	 */
	public static final int DIRECTION_POINTER_SIZE = 6;

	/**
	 * Draws a star centered in center inscribed in a square of side length
	 * size in the given color.
	 * 
	 * @param g
	 *            The graphics object
	 * @param size
	 *            The size of the square
	 * @param color
	 *            The color of the star
	 */
	public static void drawStar(Graphics2D g, double size, Color color) {
		Stroke pstroke = g.getStroke();
		Color pcolor = g.getColor();
		g.setStroke(mediumStroke);
		g.setColor(color);

		double x = size / 2 - size / 9.0;  // make the center of the star the
											// event location. 9.0 is empirical.
											// I am too lazy to find the exact
											// value.
		double y = size / 2 + size / 9.0;
		double angle = Math.PI;
		double xNext;
		double yNext;
		double side = size / 3;

		for (int i = 0; i < 5; i++) {
			xNext = x + side * Math.cos(angle);
			yNext = y + side * Math.sin(angle);
			g.drawLine((int) x, (int) y, (int) xNext, (int) yNext);
			x = xNext;
			y = yNext;
			angle = angle + 0.8 * Math.PI;
			xNext = x + side * Math.cos(angle);
			yNext = y + side * Math.sin(angle);
			g.drawLine((int) x, (int) y, (int) xNext, (int) yNext);
			x = xNext;
			y = yNext;
			angle = angle - 0.4 * Math.PI;
		}

		g.setStroke(pstroke);
		g.setColor(pcolor);
	}

	/**
	 * Draws a target: a star with the id inside.
	 * 
	 * @param g
	 *            The graphics object
	 * @param size
	 *            The size of the square
	 * @param id
	 *            The id of the target
	 * @param color
	 *            The color of the star
	 */
	public static void drawTarget(Graphics2D g, double size, int id, Color color) {
		Color pcolor = g.getColor();
		drawStar(g, size, color);
		g.setColor(Color.black);
		g.drawString("" + id,
				(int) size / 2 - g.getFontMetrics().stringWidth("" + id) / 2,
				(int) size / 2 + 3);
		g.setColor(pcolor);
	}

	/**
	 * Draws an agent: a circle with the id and an indicator of the heading.
	 * 
	 * @param g
	 *            The graphics object
	 * @param size
	 *            The size of the agent
	 * @param heading
	 *            The heading of the agent
	 * @param id
	 *            The id of the agent
	 * @param color
	 *            The color of the circle
	 */
	public static void drawAgent(Graphics2D g, Point size, double heading,
			int id, Color color) {
		Color pcolor = g.getColor();
		g.setColor(color);

		int w = (int) size.x;
		int h = (int) size.y;
		int x = w / 2;
		int y = h / 2;

		g.fillOval(0, 0, w, h);
		g.setColor(Color.black);
		g.drawOval(0, 0, w, h);
		g.drawString("" + id, x - g.getFontMetrics().stringWidth("" + id) / 2,
				y + 4);

		g.setColor(Color.black);

		g.fillOval(x + (int) (Math.cos(heading) * w / 2)
				- DIRECTION_POINTER_SIZE / 2, y
				+ (int) (Math.sin(heading) * h / 2) - DIRECTION_POINTER_SIZE
				/ 2, DIRECTION_POINTER_SIZE, DIRECTION_POINTER_SIZE);

		g.setColor(pcolor);
	}

	/**
	 * Creates a color mixing color a and b with the amount of a given by rate
	 * 
	 * @param a
	 * @param b
	 * @param rate
	 *            The amount of color a. Should be between 0 and 1
	 * @return A mixed color from a and b
	 */
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
