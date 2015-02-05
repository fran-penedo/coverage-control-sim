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
import bu.edu.coverage.coverage_control_sim.ui.Tableau;
import bu.edu.coverage.coverage_control_sim.ui.actorinfo.ActorInfo;
import bu.edu.coverage.coverage_control_sim.ui.actorinfo.ObstacleInfo;
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

		setBounds(points);
		this.points = moveToOrigin(points);

	}

	protected ArrayList<Point> moveToOrigin(List<Point> points) {
		ArrayList<Point> npoints = new ArrayList<>();
		for (Point p : points) {
			npoints.add(p.diff(getPos()));
		}
		return npoints;
	}

	protected void setBounds(List<Point> points) {
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

	public List<Point> getPoints() {
		ArrayList<Point> rawpoints = new ArrayList<>();
		for (Point p : points) {
			rawpoints.add(p.add(getPos()));
		}
		return rawpoints;
	}

	public void setPoint(int i, Point p) {
		List<Point> raw = getPoints();
		if (i >= raw.size()) {
			raw.add(p);
		} else {
			raw.set(i, p);
		}
		setBounds(raw);
		this.points = moveToOrigin(raw);

	}

	public void removePoint(int i) {
		List<Point> raw = getPoints();
		raw.remove(i);
		setBounds(raw);
		this.points = moveToOrigin(raw);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.actor.Actor#init()
	 */
	@Override
	public void init() {
		// Ignore

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
		return new Obstacle(d, getPoints());
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
		return ObstacleInfo.getObstacleInfo(this, tableau);
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
		Painter.drawPolygon((Graphics2D) g, size, points, color);

	}

	@Override
	public Integer getLayer() {
		return Tableau.OBSTACLE_LAYER;
	}

	@Override
	public String toCode() {
		String ret = super.toCode();
		List<Point> raw = getPoints();
		for (Point p : raw) {
			ret += " " + p.toCode();
		}

		return ret;
	}

}
