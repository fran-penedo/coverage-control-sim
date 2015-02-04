/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui.actorinfo;

import bu.edu.coverage.coverage_control_sim.actor.Obstacle;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;

/**
 * @author fran
 *
 */
public class ObstacleInfo extends ActorInfo {
	private static final long serialVersionUID = 1L;

	// Singleton instance
	static protected ObstacleInfo instance = new ObstacleInfo();

	// Referring agent
	protected Obstacle a;

	protected ObstacleInfo() {
	}

	/**
	 * Obtains the info panel for the obstacle associated to the given tableau.
	 * Only one panel can exist at any given time.
	 * 
	 * @param a
	 *            The Obstacle
	 * @param t
	 *            The containing tableau
	 * @return The info panel associated to the obstacle
	 */
	static public ObstacleInfo getAgentInfo(Obstacle a, Tableau t) {
		instance.setObstacle(a);
		instance.setTableau(t);
		return instance;
	}

	protected void setObstacle(Obstacle a) {
		this.a = a;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.ui.actorinfo.ActorInfo#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.ui.actorinfo.ActorInfo#set()
	 */
	@Override
	public void set() {
		// TODO Auto-generated method stub

	}

}
