/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import bu.edu.coverage.coverage_control_sim.actor.Target.VisitedEventInfo;
import bu.edu.coverage.coverage_control_sim.comm.Communication;
import bu.edu.coverage.coverage_control_sim.comm.Message;
import bu.edu.coverage.coverage_control_sim.comm.Message.MType;
import bu.edu.coverage.coverage_control_sim.control.Control;
import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.sense.Sense;
import bu.edu.coverage.coverage_control_sim.ui.ActorInfo;
import bu.edu.coverage.coverage_control_sim.ui.AgentInfo;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;
import bu.edu.coverage.coverage_control_sim.util.Painter;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * A moving agent with control, communication and sense modules. All logic is
 * coded in the modules, only the event dispatch is relevant in this class.
 * 
 * FIXME no need for getactors?
 * 
 * @author Francisco Penedo (franp@bu.edu)
 */
public class Agent extends MovingActor {

	// Modules
	protected Control control;
	protected Communication comm;
	protected Sense sense;

	/**
	 * Creates an agent in the given director in the position p, with size size,
	 * speed v and heading heading. The agent won't have any modules attached.
	 * 
	 * @param director
	 *            The director to add the agent to
	 * @param p
	 *            The position of the agent
	 * @param size
	 *            The size of the agent
	 * @param v
	 *            The speed of the agent
	 * @param heading
	 *            The heading of the agent
	 */
	public Agent(Director director, Point p, Point size, double v,
			double heading) {
		super(director, p, size, v, heading);
	}

	/**
	 * Test constructor
	 * 
	 * @param director
	 */
	public Agent(Director director) {
		this(director, new Point(10, 10), new Point(20, 20), 5, Math.PI / 4);
	}

	/**
	 * Copies the agent and adds the copy to the director.
	 * 
	 * @param a
	 *            The agent to copy
	 * @param director
	 *            The director to add the copy to
	 */
	public Agent(Agent a, Director director) {
		this(director, a.getPos(), a.getSize(), a.getV(), a.getHeading());
	}

	@Override
	public void init() {
		if (this.comm != null) {
			comm.init();
		}

		if (this.control != null) {
			control.init();
		}

		if (this.sense != null) {
			sense.init();
		}

	}

	@Override
	public void fire(Event e) {
		switch (e.type) {
		case CONTROL: {
			controlEvent(e);
			break;
		}
		case VISITED: {
			visitedEvent(e);
			break;
		}
		case TARGET: {
			targetEvent(e);
			break;
		}
		case SENSE: {
			senseEvent(e);
			break;
		}
		case MESSAGE: {
			messageEvent(e);
			break;
		}
		case AGENT: {
			agentEvent(e);
			break;
		}
		default: {
			super.fire(e);
			break;
		}
		}

	}

	protected void controlEvent(Event e) {
		if (control != null) {
			control.control();
		}
	}

	protected void visitedEvent(Event e) {
		VisitedEventInfo info = (VisitedEventInfo) e.payload;
		// Sense needs to know the agent is not active anymore. Also keeps track
		// of the rewards in master
		if (sense != null) {
			sense.visited(info.target, info.reward);
		}
		// Broadcast visit to other agents
		if (comm != null) {
			comm.send(new Message(id, Message.BC, MType.VISITED,
					new VisitedMsgInfo(info.target, info.reward)));
		}
	}

	protected void targetEvent(Event e) {
		if (sense != null) {
			Target t = (Target) e.payload;
			sense.addTarget(t);
		}
	}

	protected void senseEvent(Event e) {
		if (sense != null) {
			sense.sense();
		}
	}

	protected void messageEvent(Event e) {
		if (comm != null) {
			comm.receive((Message) e.payload);
		}
	}

	protected void agentEvent(Event e) {
		if (comm != null) {
			comm.addAgent((Agent) e.payload);
		}
	}

	/**
	 * Sets the control module.
	 * 
	 * @param control
	 *            The new control module
	 */
	public void setControl(Control control) {
		this.control = control;
		control.attach(this);
	}

	/**
	 * Gets the control module.
	 * 
	 * @return The control module
	 */
	public Control getControl() {
		return control;
	}

	/**
	 * Sets the communication module.
	 * 
	 * @param comm
	 *            The new communication module
	 */
	public void setCommunication(Communication comm) {
		this.comm = comm;
		comm.attach(this);
	}

	/**
	 * Gets the communication module.
	 * 
	 * @return The communication module
	 */
	public Communication getCommunication() {
		return comm;
	}

	/**
	 * Gets the sense module.
	 * 
	 * @return The sense module
	 */
	public Sense getSense() {
		return sense;
	}

	/**
	 * Sets the sense module.
	 * 
	 * @param sense
	 *            The sense module to set
	 */
	public void setSense(Sense sense) {
		this.sense = sense;
		sense.attach(this);
	}

	/**
	 * Visited message payload.
	 * 
	 * @author Francisco Penedo (franp@bu.edu)
	 *
	 */
	public class VisitedMsgInfo {
		/**
		 * The visited target.
		 */
		public final Target target;
		/**
		 * The collected reward.
		 */
		public final double reward;

		/**
		 * Creates the payload for a visited message.
		 * 
		 * @param target
		 *            The visited target
		 * @param reward
		 *            The collected reward
		 */
		public VisitedMsgInfo(Target target, double reward) {
			this.target = target;
			this.reward = reward;
		}
	}

	@Override
	public String toString() {
		return getPos().toString();
	}

	@Override
	public String toCode() {
		String s = super.toCode();
		if (sense != null) {
			s = s + sense.toCode();
		}
		if (comm != null) {
			s = s + comm.toCode();
		}
		if (control != null) {
			s = s + control.toCode();
		}
		return s;
	}

	@Override
	public Actor deepCopy(Director d) {
		Agent a = new Agent(this, d);
		if (sense != null) {
			a.setSense(sense.deepCopy());
		}
		if (comm != null) {
			a.setCommunication(comm.deepCopy());
		}
		if (control != null) {
			a.setControl(control.deepCopy());
		}

		return a;
	}

	@Override
	public ActorInfo getInfoPanel(Tableau tableau) {
		return AgentInfo.getAgentInfo(this, tableau);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see coverage.actor.Actor#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Painter.drawAgent((Graphics2D) g, size, heading, id, Color.cyan);
	}

	@Override
	public Integer getLayer() {
		return Tableau.AGENT_LAYER;
	}
}
