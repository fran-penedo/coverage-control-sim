/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.comm;

/**
 * Basic message class. If more complex protocols are needed, this should be
 * the top header (or an equivalent class)
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class Message {
	/**
	 * Broadcast direction.
	 */
	public static final int BC = -1;

	/**
	 * Message origin.
	 */
	public final int from; // No need for relays
	/**
	 * Message destination.
	 */
	public final int to;
	/**
	 * Message type.
	 */
	public final MType type;
	/**
	 * Message extra data.
	 */
	public final Object payload;

	/**
	 * Creates a message.
	 * 
	 * @param from
	 * @param to
	 * @param type
	 * @param payload
	 */
	public Message(int from, int to, MType type, Object payload) {
		this.from = from;
		this.to = to;
		this.type = type;
		this.payload = payload;
	}

	/**
	 * Message types.
	 * 
	 * @author Francisco Penedo (franp@bu.edu)
	 *
	 */
	public enum MType {
		/**
		 * Ping message. Currently not needed.
		 */
		PING,
		/**
		 * Message announcing a target has been visited.
		 */
		VISITED,
		/**
		 * Message with control information from leader to followers.
		 */
		CONTROL,
		/**
		 * Message asking to join the control system.
		 */
		JOIN_CONTROL
	}

}