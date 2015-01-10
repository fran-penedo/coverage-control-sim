/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Graphics;

import bu.edu.coverage.coverage_control_sim.event.Event;
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

	public Actor(Director director, Point p, Point size) {
		this.id = director.getUniqueID();
		this.director = director;
		this.p = p;
		this.size = size;
		director.addActor(this);
		last_update = 0;
	}

	public Actor(Director director) {
		this(director, new Point(100, 100), new Point(50, 50));
	}

	public void postEvent(Event e) {
		director.postEvent(e);
	}

	public void fire(Event e) {
		switch (e.type) {
		case UPDATE: {
			updateEvent(e);
			break;
		}
		default: {
			System.err.println("Unhandled event in actor " + id + ": " + e);
			break;
		}
		}
	}

	protected void updateEvent(Event e) {
		if (last_update > e.due) {

		} else {
			last_update = e.due;
		}
	}

	public abstract void init();

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

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
