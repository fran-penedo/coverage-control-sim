/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.util.ArrayList;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.Event.Type;
import bu.edu.coverage.coverage_control_sim.event.EventQueue;

/**
 * @author fran
 *
 */
public class Director {
	protected ArrayList<Actor> actors;
	protected EventQueue queue;
	protected double time;
	protected int last_id;
	protected boolean initialized;

	/**
	 * 
	 */
	public Director(double start) {
		actors = new ArrayList<Actor>();
		queue = new EventQueue();
		time = start;
		last_id = 0;
		initialized = false;
	}

	public Director() {
		this(0);
	}

	public void init() {
		for (Actor a : actors) {
			a.init();
		}
		initialized = true;
	}

	public void updateAll() {
		for (Actor actor : actors) {
			queue.add(new Event(time, time, actor, Type.UPDATE));
		}
		runFor(0);
	}

	public void postEvent(Event e) {
		queue.add(e);
	}

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

	private void printQueue() {
		System.err.println("------------");
		for (Event e : queue) {
			System.err.println(e);
		}

	}

	protected void dispatch(Event e) {
		e.dispatch(); // double dispatch
	}

	public void addActor(Actor actor) {
		actors.add(actor);
		initialized = false;
	}

	public double getCurrentTime() {
		return time;
	}

	public List<Actor> getActors() {
		return actors;
	}

	public int getUniqueID() {
		return ++last_id;
	}
}
