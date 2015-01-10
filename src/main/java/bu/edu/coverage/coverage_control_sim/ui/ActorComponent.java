/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

import bu.edu.coverage.coverage_control_sim.actor.Actor;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class ActorComponent extends JComponent {

	protected final Actor a;
	protected boolean selected;

	/**
	 * 
	 */
	public ActorComponent(Actor a) {
		this.a = a;
		this.selected = false;
		setActorBounds();
		addMouseListener(new ACMouseAdapter());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		a.paint(g);
		setActorBounds();

	}

	public void setActorBounds() {
		Point p = a.getPos();
		Point size = a.getSize();
		int x = (int) p.x;
		int y = (int) p.y;
		int w = (int) size.x;
		int h = (int) size.y;
		setBounds(x - w / 2, y - h / 2, w, h);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		updateBorder();
	}

	protected void updateBorder() {
		if (selected) {
			setBorder(BorderFactory.createDashedBorder(Color.black));
		} else {
			setBorder(null);
		}
	}

	private class ACMouseAdapter extends MouseInputAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			switch (e.getButton()) {
			case MouseEvent.BUTTON1: {
				mouseClicked1(e);
				break;
			}
			case MouseEvent.BUTTON2: {
				mouseClicked2(e);
				break;
			}
			default:
				break;
			}
		}

		private void mouseClicked2(MouseEvent e) {
			// ignore for now
		}

		private void mouseClicked1(MouseEvent e) {
			setSelected(true);
		}

	}
}
