/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import bu.edu.coverage.coverage_control_sim.actor.Actor;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class ActorComponent extends JComponent {

	protected final Actor a;

	/**
	 * 
	 */
	public ActorComponent(Actor a) {
		this.a = a;
		Point size = a.getSize();
		setPreferredSize(new Dimension((int) size.x, (int) size.y));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		a.paint(g);
		Point p = a.getPos();
		int x = (int) p.x;
		int y = (int) p.y;
		Dimension size = getPreferredSize();
		setBounds(x - size.width / 2, y - size.height / 2, x + size.width / 2,
				y + size.height / 2);
	}
}
