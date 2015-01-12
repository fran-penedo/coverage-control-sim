/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.actor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import bu.edu.coverage.coverage_control_sim.actor.Target.VisitEventInfo;
import bu.edu.coverage.coverage_control_sim.comm.Communication;
import bu.edu.coverage.coverage_control_sim.comm.Message;
import bu.edu.coverage.coverage_control_sim.comm.Message.MType;
import bu.edu.coverage.coverage_control_sim.control.Control;
import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.sense.Sense;
import bu.edu.coverage.coverage_control_sim.util.Painter;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 *         FIXME no need for getactors?
 */
public class Agent extends MovingActor {

	protected Control control;
	protected Communication comm;
	protected Sense sense;

	/**
	 * @param id
	 * @param director
	 */
	public Agent(Director director, Point p, Point size, double v,
			double heading) {
		super(director, p, size, v, heading);
	}

	public Agent(Director director) {
		this(director, new Point(10, 10), new Point(20, 20), 5, Math.PI / 4);
	}

	public Agent(Agent a, Director director) {
		this(director, a.p, a.size, a.v, a.heading);
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
		VisitEventInfo info = (VisitEventInfo) e.payload;
		if (sense != null) {
			sense.visited(info.target, info.reward);
		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see coverage.actor.Actor#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Painter.drawAgent((Graphics2D) g, p, size, heading, id, Color.cyan);
	}

	public void setControl(Control control) {
		this.control = control;
		control.attach(this);
	}

	public Control getControl() {
		return control;
	}

	public void setCommunication(Communication comm) {
		this.comm = comm;
		comm.attach(this);
	}

	public Communication getCommunication() {
		return comm;
	}

	/**
	 * @return the sense module
	 */
	public Sense getSense() {
		return sense;
	}

	/**
	 * @param sense
	 *            the sense module to set
	 */
	public void setSense(Sense sense) {
		this.sense = sense;
		sense.attach(this);
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public class VisitedMsgInfo {
		public final Target target;
		public final double reward;

		public VisitedMsgInfo(Target target, double reward) {
			this.target = target;
			this.reward = reward;
		}
	}

	@Override
	public String toString() {
		return p.toString();
	}

}
