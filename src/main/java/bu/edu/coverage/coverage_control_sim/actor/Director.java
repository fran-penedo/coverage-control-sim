/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.util.ArrayList;

import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.EventQueue;
import bu.edu.coverage.coverage_control_sim.event.UpdateEvent;

/**
 * @author fran
 *
 */
public class Director {
	protected ArrayList<Actor> actors;
	protected EventQueue queue;
	protected double time;

	/**
	 * 
	 */
	public Director(double start) {
		actors = new ArrayList<Actor>();
		queue = new EventQueue();
		time = start;
	}

	public Director() {
		this(0);
	}

	public void updateAll() {
		for (Actor actor : actors) {
			queue.add(new UpdateEvent(time, time, actor));
		}
		runFor(0);
	}

	public void postEvent(Event e) {
		queue.add(e);
	}

	public void runFor(double t) {
		double until = time + t;

		while (!queue.isEmpty() && queue.peek().due <= until) {
			Event next = queue.remove();
			dispatch(next);
			time = next.due;
		}
		time = until;
	}

	protected void dispatch(Event e) {
		e.dispatch(); // double dispatch
	}

	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public double getCurrentTime() {
		return time;
	}
}
