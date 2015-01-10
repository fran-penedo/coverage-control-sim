/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.sense;

import bu.edu.coverage.coverage_control_sim.actor.Target;

/**
 * @author fran
 *
 */
public class MasterSense extends Sense {
	protected double total_reward;

	public MasterSense() {
		total_reward = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.sense.Sense#init()
	 */
	@Override
	public void init() {
		total_reward = 0;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.sense.Sense#sense()
	 */
	@Override
	public void sense() {
		// TODO Auto-generated method stub

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
		total_reward += reward;
	}

}
