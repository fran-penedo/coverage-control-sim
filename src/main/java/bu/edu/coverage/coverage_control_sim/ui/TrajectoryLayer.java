/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.HashMap;

import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * Displays the trajectories of the actors in the simulation.
 * 
 * <p>
 * TODO straight line updates should be optimized
 * 
 * @author Francisco Penedo (franp@bu.edu)
 */
public class TrajectoryLayer extends InfoLayer {
	private static final long serialVersionUID = 1L;

	// Path for each actor
	protected HashMap<ActorComponent, Path2D.Double> paths;
	protected Tableau tableau; // Containing tableau

	/**
	 * Creates a component displaying the trajectories of the actors contained
	 * in the tableau.
	 * 
	 * @param tableau
	 *            The containing tableau
	 */
	public TrajectoryLayer(Tableau tableau) {
		this.tableau = tableau;
		this.paths = new HashMap<>();
	}

	public void update() {
		for (ActorComponent ac : tableau.getActors()) {
			Path2D.Double path = getPath(ac);
			Point next = ac.getActor().getPos();
			path.lineTo(next.x, next.y);
		}
	}

	// Gets or creates a new path for an actor
	private Path2D.Double getPath(ActorComponent ac) {
		if (paths.containsKey(ac)) {
			return paths.get(ac);
		} else {
			Path2D.Double path = new Path2D.Double();
			Point start = ac.getActor().getPos();
			path.moveTo(start.x, start.y);
			paths.put(ac, path);
			return path;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		for (ActorComponent ac : tableau.getActors()) {
			if (paths.containsKey(ac)) {
				g2.draw(paths.get(ac));
			}
		}
	}

}
