/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.event;

import java.util.ArrayList;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.actor.Actor;

/**
 * Simulation director class. Controls the simulation by dispatching events in
 * due time order. The simulation can be initialized or not. When it's not,
 * running the director will first call the init() method for each actor. The
 * director goes to not initialized state when actors are added or removed.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class Director {
	protected ArrayList<Actor> actors;
	protected EventQueue queue;
	protected double time; // current time
	protected int last_id; // last id generated
	protected boolean initialized; // if false, call init()

	/**
	 * Creates an uninitialized director with start current time.
	 * 
	 * @param start
	 *            The current time
	 */
	public Director(double start) {
		actors = new ArrayList<Actor>();
		queue = new EventQueue();
		time = start;
		last_id = 0;
		initialized = false;
	}

	/**
	 * Creates an unitialized director with 0 current time.
	 */
	public Director() {
		this(0);
	}

	protected void init() {
		for (Actor a : actors) {
			a.init();
		}
		initialized = true;
	}

	/**
	 * Adds an event to the queue to be processed by the director.
	 * 
	 * @param e
	 *            The event
	 */
	public void postEvent(Event e) {
		queue.add(e);
	}

	/**
	 * Runs the director for t seconds. It will process all events pending that
	 * are due t seconds from now (including) and all events created
	 * subsequently. This method won't post update events.
	 * 
	 * @param t
	 *            Time in seconds the simulation will be run. If 0, all events
	 *            due now will be dispatched
	 */
	public void runFor(double t) {
		double until = time + t;

		if (!initialized) {
			init();
		}

		while (!queue.isEmpty() && queue.peek().due <= until) {
			// printQueue();
			Event next = queue.remove();
			time = next.due;
			dispatch(next);
		}
		time = until;
	}

	protected void printQueue() {
		System.err.println("------------");
		for (Event e : queue) {
			System.err.println(e);
		}

	}

	protected void dispatch(Event e) {
		e.dispatch(); // double dispatch
	}

	/**
	 * Adds an actor to the simulation. The simulation will be marked not
	 * initialized.
	 * 
	 * @param actor
	 *            The actor to add
	 */
	public void addActor(Actor actor) {
		actors.add(actor);
		initialized = false;
	}

	/**
	 * Removes an actor from the simulation. The simulation will be marked not
	 * initialized.
	 * 
	 * @param actor
	 *            The actor to remove
	 */
	public void removeActor(Actor actor) {
		actors.remove(actor);
		initialized = false;
	}

	/**
	 * Gets the current time.
	 * 
	 * @return The current time
	 */
	public double getCurrentTime() {
		return time;
	}

	/**
	 * Gets a list of all the actors currently included in the director.
	 * 
	 * @return Actors in this director
	 */
	public List<Actor> getActors() {
		return actors;
	}

	/**
	 * Generates a unique id. Currently very basic.
	 * 
	 * @return A unique id
	 */
	public int getUniqueID() {
		return ++last_id;
	}

	/**
	 * Generates a text representation of the simulation.
	 * 
	 * @return A string with the code
	 */
	public String toCode() {
		String s = "";
		for (Actor a : actors) {
			s = s + a.toCode() + "\n";
		}
		return s;
	}

	/**
	 * Hard copies the simulation (no mutable references will be shared between
	 * this director and the returned copy).
	 * 
	 * @return A copy of this director
	 */
	public Director deepCopy() {
		Director d = new Director(this.getCurrentTime());
		for (Actor a : actors) {
			a.deepCopy(d);
		}
		return d;
	}
}
