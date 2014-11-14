/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import bu.edu.coverage.coverage_control_sim.actor.Director;

/**
 * @author fran
 *
 */
public class Tableau extends JPanel implements ActionListener {
	public final int width;
	public final int height;
	protected Director d;
	protected Thread tableau;
	protected final Timer timer = new Timer(1000, this); // TODO parameter

	public Tableau(int width, int height, Director d) {
		this.width = width;
		this.height = height;
		this.d = d;

		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void actionPerformed(ActionEvent e) {
		d.runFor(1);
		d.updateAll();
		repaint();
	}

	public void start() {
		timer.start();
	}
}
