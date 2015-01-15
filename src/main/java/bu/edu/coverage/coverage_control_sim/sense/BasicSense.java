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
 * @author fran
 *
 */
public class BasicSense extends Sense {
	protected ArrayList<Target> active;
	protected double sample_t;

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
