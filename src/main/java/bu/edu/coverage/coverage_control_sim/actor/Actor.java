/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Graphics;

import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.ui.ActorInfo;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * Base class for actors.
 * 
 * NOTE: could be non abstract really.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public abstract class Actor {
	protected final int id; // TODO Maybe name
	protected final Director director;
	protected Point p; // Position
	// TODO not so sure I like this
	protected Point size; // Size of the rectangle covering the actor
	protected double last_update;

	/**
	 * Creates an actor in the given director with position p and the given
	 * size.
	 * 
	 * @param director
	 *            A director to add the actor to
	 * @param p
	 *            The position of the actor
	 * @param size
	 *            The size of the actor
	 */
	public Actor(Director director, Point p, Point size) {
		this.id = director.getUniqueID();
		this.director = director;
		this.p = p;
		this.size = size;
		director.addActor(this);
		last_update = director.getCurrentTime();
	}

	/**
	 * Test constructor
	 * 
	 * @param director
	 */
	public Actor(Director director) {
		this(director, new Point(100, 100), new Point(50, 50));
	}

	/**
	 * Creates a copy of the actor in the given director.
	 * 
	 * @param director
	 *            The director to add the new actor to
	 * @param actor
	 *            The actor to copy
	 */
	public Actor(Director director, Actor actor) {
		this(director, actor.p, actor.size);
	}

	/**
	 * Posts an event to the director owning the actor.
	 * 
	 * @param e
	 *            The event
	 */
	public void postEvent(Event e) {
		director.postEvent(e);
	}

	/**
	 * Processes an event in this actor.
	 * 
	 * @param e
	 *            The event to process
	 */
	public void fire(Event e) {
		// Process each event type in its own method
		switch (e.type) {
		case UPDATE: {
			updateEvent(e);
			break;
		}
		default: {
			// System.err.println("Unhandled event in actor " + id + ": " + e);
			break;
		}
		}
	}

	// ignores updates to the past
	protected void updateEvent(Event e) {
		if (last_update > e.due) {

		} else {
			last_update = e.due;
		}
	}

	/**
	 * Initializes the actor. An actor should not assume other actors are
	 * initialized at current time.
	 */
	public abstract void init();

	/**
	 * Paints the actor
	 * 
	 * @param g
	 */
	public abstract void paint(Graphics g);

	/**
	 * Gets the position.
	 * 
	 * @return The position
	 */
	public Point getPos() {
		return p;
	}

	/**
	 * Sets the position.
	 * 
	 * @param npos
	 *            The new position
	 */
	public void setPos(Point npos) {
		this.p = npos;
	}

	/**
	 * Gets the size.
	 * 
	 * @return The size
	 */
	public Point getSize() {
		return size;
	}

	/**
	 * Gets the director.
	 * 
	 * @return The director
	 */
	public Director getDirector() {
		return director;
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Destroys the actor and removes it from the director.
	 */
	public void destroy() {
		director.removeActor(this);
	}

	/**
	 * Gets a text representation of the actor.
	 * 
	 * @return A string representing the actor
	 */
	public String toCode() {
		return this.getClass().getSimpleName() + " " + p.toCode() + " "
				+ size.toCode();
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

	/**
	 * Hard copies this actor and adds the copy to the given director.
	 * 
	 * @param d
	 *            The director to add the copy to
	 * @return A deep copy of this actor
	 */
	public abstract Actor deepCopy(Director d);

	/**
	 * Generates a panel with information of this actor.
	 * 
	 * @param tableau
	 *            The tableau for the info panel
	 * @return An info panel linked to this actor
	 */
	public abstract ActorInfo getInfoPanel(Tableau tableau);

}
