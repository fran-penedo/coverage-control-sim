/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.sense;

import bu.edu.coverage.coverage_control_sim.actor.Target;

/**
 * The sensing module for a master agent keeping track of the total collected
 * reward.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class MasterSense extends BasicSense {
	protected double total_reward;

	/**
	 * Creates the sensing module for the master agent
	 */
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

	/**
	 * Obtains the total reward collected by the agents so far.
	 * 
	 * @return The total reward
	 */
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
