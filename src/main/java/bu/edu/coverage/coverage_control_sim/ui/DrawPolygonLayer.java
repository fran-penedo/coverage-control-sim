/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class DrawPolygonLayer extends GlassLayer {

	protected ArrayList<Point> points;

	public DrawPolygonLayer() {
		points = new ArrayList<>();
	}

	public void addPoint(Point p) {
		points.add(p);
	}

	public List<Point> getPoints() {
		return points;
	}

	public void clear() {
		points.clear();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int[] xs = new int[points.size()];
		int[] ys = new int[points.size()];

		for (int i = 0; i < points.size(); i++) {
			xs[i] = (int) (points.get(i).x);
			ys[i] = (int) (points.get(i).y);
		}

		g.drawPolyline(xs, ys, points.size());
	}
}
