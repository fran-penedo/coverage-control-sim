/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.sense;

import java.util.ArrayList;
import java.util.List;

import bu.edu.coverage.coverage_control_sim.actor.Target;
import bu.edu.coverage.coverage_control_sim.event.Event;
import bu.edu.coverage.coverage_control_sim.event.Event.EType;

/**
 * A simple sense module that performs periodic sensing for visiting targets
 * and makes all targets in the simulation available to the agent.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class BasicSense extends Sense {
	protected ArrayList<Target> active; // List of active targets
	protected double sample_t; // sample period

	/**
	 * Creates a BasicSense module with a sample period.
	 * 
	 * @param sample_t
	 *            The sample period
	 */
	public BasicSense(double sample_t) {
		this.sample_t = sample_t;
	}

	@Override
	public void init() {
		super.init();
		active = new ArrayList<Target>();
		agent.postEvent(new Event(agent.getDirector().getCurrentTime(), agent
				.getDirector().getCurrentTime(), agent, EType.SENSE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.sense.Sense#sense()
	 */
	@Override
	public void sense() {
		// Look for visitable targets periodically
		for (Target t : active) {
			if (visitable(t)) {
				agent.postEvent(new Event(agent.getDirector().getCurrentTime(),
						agent.getDirector().getCurrentTime(), t, EType.VISIT,
						agent));
			}
		}
		agent.postEvent(new Event(agent.getDirector().getCurrentTime(), agent
				.getDirector().getCurrentTime() + sample_t, agent, EType.SENSE));
	}

	protected boolean visitable(Target t) {
		return t.inRange(agent.getPos());
	}

	@Override
	public void addTarget(Target target) {
		super.addTarget(target);
		if (!active.contains(target) && target.isActive()) {
			active.add(target);
		}
	}

	@Override
	public List<Target> getTargets() {
		return active;
	}

	@Override
	public void visited(Target target, double reward) {
		active.remove(target);
	}

	@Override
	public Sense deepCopy() {
		return new BasicSense(sample_t);
	}

}
