/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JPanel;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.Director;
import bu.edu.coverage.coverage_control_sim.actor.Target;
import bu.edu.coverage.coverage_control_sim.control.DeadlineDiscount;

/**
 * @author fran
 *
 */
public class CoverageApplet extends Applet {
	protected JPanel center;
	protected Tableau t;

	/**
	 * @throws HeadlessException
	 */
	public CoverageApplet() throws HeadlessException {

	}

	@Override
	public void start() {
		super.start();

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init() {
		super.init();
		center = new JPanel();
		Director d = new Director();
		t = new Tableau(200, 200, d); // TODO parameter
		t.add(new ActorComponent(new Agent(1, d)));
		t.add(new ActorComponent(new Target(2, d, new DeadlineDiscount(0.3, 1,
				5), 100, 20)));
		setLayout(new BorderLayout());
		center.add(t);
		add("Center", center);

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				t.start();
			}
		});
	}
}
