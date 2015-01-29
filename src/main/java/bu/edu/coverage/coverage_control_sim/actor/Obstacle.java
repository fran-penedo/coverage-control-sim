/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.ui.ActorInfo;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;
import bu.edu.coverage.coverage_control_sim.util.Painter;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class Obstacle extends Actor {
	ArrayList<Point> points;

	/**
	 * Creates a polygonal obstacle from a list of vertices.
	 * 
	 * @param director
	 *            The director to add the actor to
	 * @param points
	 *            The list of vertices of the polygon
	 */
	public Obstacle(Director director, List<Point> points) {
		super(director, null, null);

		this.points = new ArrayList<>(points);
		setBounds();
	}

	protected void setBounds() {
		double minx = Double.POSITIVE_INFINITY;
		double miny = Double.POSITIVE_INFINITY;
		double maxx = Double.NEGATIVE_INFINITY;
		double maxy = Double.NEGATIVE_INFINITY;

		for (Point p : points) {
			minx = p.x < minx ? p.x : minx;
			miny = p.y < miny ? p.y : miny;
			maxx = p.x > maxx ? p.x : maxx;
			maxy = p.y > maxy ? p.y : maxy;
		}

		this.size = new Point(maxx - minx, maxy - miny);
		this.p = new Point(minx + size.x / 2, miny + size.y / 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.actor.Actor#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bu.edu.coverage.coverage_control_sim.actor.Actor#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Color color = Color.green;
		Painter.drawPolygon((Graphics2D) g, p, size, points, color);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bu.edu.coverage.coverage_control_sim.actor.Actor#deepCopy(bu.edu.coverage
	 * .coverage_control_sim.event.Director)
	 */
	@Override
	public Actor deepCopy(Director d) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bu.edu.coverage.coverage_control_sim.actor.Actor#getInfoPanel(bu.edu.
	 * coverage.coverage_control_sim.ui.Tableau)
	 */
	@Override
	public ActorInfo getInfoPanel(Tableau tableau) {
		// TODO Auto-generated method stub
		return null;
	}

}
