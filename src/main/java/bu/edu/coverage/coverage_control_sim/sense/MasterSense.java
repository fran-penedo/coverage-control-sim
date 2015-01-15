/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.sense;

import bu.edu.coverage.coverage_control_sim.actor.Target;

/**
 * @author fran
 *
 */
public class MasterSense extends BasicSense {
	protected double total_reward;

	public MasterSense() {
		super(0);
		total_reward = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.sense.Sense#init()
	 */
	@Override
	public void init() {
		super.init();
		total_reward = 0;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.sense.Sense#sense()
	 */
	@Override
	public void sense() {
		// ignore

	}

	public double getReward() {
		return total_reward;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bu.edu.coverage.coverage_control_sim.sense.Sense#visited(bu.edu.coverage
	 * .coverage_control_sim.actor.Target, double)
	 */
	@Override
	public void visited(Target target, double reward) {
		super.visited(target, reward);
		total_reward += reward;
	}

	@Override
	public Sense deepCopy() {
		return new MasterSense();
	}
}
