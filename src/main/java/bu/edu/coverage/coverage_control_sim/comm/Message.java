/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.comm;

/**
 * Basic message class. If more complex protocols are needed, this should be
 * the top header (or an equivalent class)
 * 
 * @author fran
 *
 */
public class Message {
	public static final int BC = -1;

	public final int from; // No need for relays
	public final int to;
	public final MType type;
	public final Object payload;

	public Message(int from, int to, MType type, Object payload) {
		this.from = from;
		this.to = to;
		this.type = type;
		this.payload = payload;
	}

	public enum MType {
		PING, VISITED, CONTROL, JOIN_CONTROL
	}

}