/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.event;

import bu.edu.coverage.coverage_control_sim.actor.Actor;

/**
 * @author fran
 *
 */
public class Event implements Comparable<Event> {
	public final double due;
	public final double start;
	public final Actor actor;

	/**
	 * 
	 */
	public Event(double start, double due, Actor actor) {
		if (start > due) {
			throw new IllegalArgumentException("Start time after due time");
		}
		this.start = start;
		this.due = due;
		this.actor = actor;
	}

	public int compareTo(Event o) {
		if (this.due < o.due) {
			return -1;
		} else if (this.due > o.due) {
			return 1;
		} else {
			return this.start < o.start ? -1 : 1;
		}
	}

	@Override
	public String toString() {
		return "Event due: " + due + ". ";
	}

	public void dispatch() {
		actor.fire(this);
	}

}
