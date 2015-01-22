/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.event;

import bu.edu.coverage.coverage_control_sim.actor.Actor;

/**
 * An event in the simulation. The event is processed at the due time in
 * he actor passed to the constructor.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 * 
 */
public class Event implements Comparable<Event> {

	/**
	 * The event will be processed in the due time.
	 */
	public final double due;

	/**
	 * The time of generation. Not in use currently.
	 */
	public final double start;

	/**
	 * The event will be processed in this actor.
	 */
	public final Actor actor;

	/**
	 * The event type.
	 */
	public final EType type;

	/**
	 * Additional data required by the actor processing the event.
	 */
	public final Object payload;

	/**
	 * Creates an event of a given type to be processed in due time in the
	 * actor.
	 * Additional data can be passed as payload.
	 * 
	 * @param start
	 *            The generation time
	 * @param due
	 *            The due time
	 * @param actor
	 *            The processing actor
	 * @param type
	 *            The type of the event
	 * @param payload
	 *            Additional data
	 */
	public Event(double start, double due, Actor actor, EType type,
			Object payload) {
		if (start > due) {
			throw new IllegalArgumentException("Start time after due time");
		}
		this.start = start;
		this.due = due;
		this.actor = actor;
		this.type = type;
		this.payload = payload;
	}

	/**
	 * Creates an event without payload.
	 * 
	 * @param start
	 *            The generation time
	 * @param due
	 *            The due time
	 * @param actor
	 *            The processing actor
	 * @param type
	 *            The type of the event
	 */
	public Event(double start, double due, Actor actor, EType type) {
		this(start, due, actor, type, null);
	}

	/**
	 * Compares by due time.
	 */
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
		return "Event due: " + due + ". " + "Type: " + type + ". ";
	}

	/**
	 * Dispatchs the event invoking the fire method in the Actor class
	 */
	public void dispatch() {
		actor.fire(this);
	}

	/**
	 * Event types.
	 * 
	 * @author Francisco Penedo (franp@bu.edu)
	 *
	 */
	public enum EType {
		/**
		 * Periodic updates (movement, etc).
		 */
		UPDATE,
		/**
		 * Agent creation.
		 */
		AGENT,
		/**
		 * Target creation.
		 */
		TARGET,
		/**
		 * Control periodic process.
		 */
		CONTROL,
		/**
		 * Sense periodic process.
		 */
		SENSE,
		/**
		 * Message sent.
		 */
		MESSAGE,
		/**
		 * Target visited.
		 */
		VISITED,
		/**
		 * Visit target.
		 */
		VISIT
	}

}
