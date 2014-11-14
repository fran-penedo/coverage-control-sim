/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Graphics;

import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.UpdateEvent;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public abstract class Actor {
	protected final int id; // TODO Maybe name
	protected final Director director;
	protected Point p;
	protected Point size; // TODO not so sure I like this
	protected double last_update;

	public Actor(int id, Director director) {
		this.id = id;
		this.director = director;

		p = new Point(100, 100);
		size = new Point(50, 50);
		director.addActor(this);
		last_update = 0;
	}

	public void postEvent(Event e) {
		director.postEvent(e);
	}

	public void fire(Event e) {
		System.err.println("Unhandled event in actor " + id + ": " + e);
	}

	public void fire(UpdateEvent e) {
		if (last_update > e.due) {

		} else {
			last_update = e.due;
		}
	}

	public abstract void paint(Graphics g);

	public Point getPos() {
		return p;
	}

	public Point getSize() {
		return size;
	}

	public Director getDirector() {
		return director;
	}

}
