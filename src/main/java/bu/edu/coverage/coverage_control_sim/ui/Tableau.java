/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.MasterAgent;
import bu.edu.coverage.coverage_control_sim.actor.Target;
import bu.edu.coverage.coverage_control_sim.comm.BasicComm;
import bu.edu.coverage.coverage_control_sim.control.DeadlineDiscount;
import bu.edu.coverage.coverage_control_sim.control.Discount;
import bu.edu.coverage.coverage_control_sim.control.KLCRH;
import bu.edu.coverage.coverage_control_sim.control.KLCRHClient;
import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.sense.BasicSense;
import bu.edu.coverage.coverage_control_sim.sense.MasterSense;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class Tableau extends JPanel implements ActionListener {
	public final int width;
	public final int height;
	protected Director d;
	protected Thread tableau;
	protected final Timer timer = new Timer(40, this); // TODO parameter
	protected Mode mode;
	protected GlassLayer glass;

	public Tableau(int width, int height, Director d) {
		this.width = width;
		this.height = height;
		this.d = d;

		this.mode = Mode.SELECT;
		this.glass = new GlassLayer(false);
		glass.setPreferredSize(new Dimension(width, height));
		glass.setBounds(0, 0, width, height);
		glass.addMouseListener(new GlassMouseAdapter());
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(null);
		setOpaque(true);
	}

	public void start() {
		timer.start();
	}

	public void pause() {
		timer.stop();
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		if (mode == Mode.SELECT) {
			setGlassPaneActive(false);
		} else {
			setGlassPaneActive(true);
		}
	}

	protected void setGlassPaneActive(boolean active) {
		if (active) {
			if (!isAncestorOf(glass)) {
				add(glass);
			}
		} else {
			remove(glass);
		}
	}

	public void addTarget(Point p) {
		double size = 40;
		double v = 0;
		double heading = 0;
		Discount discount = new DeadlineDiscount(1, 1, 1000);
		double ireward = 100;
		Target t = new Target(d, p, size, v, heading, discount, ireward, true);
		ActorComponent ac = new ActorComponent(t);
		add(ac);
	}

	public void addAgent(Point p) {
		Point size = new Point(20, 20);
		double v = 4;
		double heading = 0;
		Agent a = new Agent(d, p, size, v, heading);
		a.setCommunication(new BasicComm());
		a.setSense(new BasicSense(1));
		a.setControl(new KLCRHClient());
		ActorComponent ac = new ActorComponent(a);
		add(ac);
	}

	public void addMaster() {
		int K = 2;
		double delta = 0.5;
		int b = 2;
		MasterAgent master = new MasterAgent(d);
		master.setCommunication(new BasicComm());
		master.setControl(new KLCRH(K, delta, b));
		master.setSense(new MasterSense());
		ActorComponent ac = new ActorComponent(master);
		add(ac);
	}

	public void glassLeftClick(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());

		switch (mode) {
		case ADD_AGENT: {
			addAgent(p);
			break;
		}
		case ADD_TARGET: {
			addTarget(p);
			break;
		}
		default:
			break;
		}

		repaint();

	}

	public void actionPerformed(ActionEvent e) {
		d.runFor(1);
		d.updateAll();
		repaint();
	}

	private class GlassMouseAdapter extends MouseInputAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				glassLeftClick(e);
			}
		}
	}
}
