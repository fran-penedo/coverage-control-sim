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

	protected final Tableau tableau;
	protected final Actor actor;
	protected boolean selected;

	/**
	 * 
	 */
	public ActorComponent(Tableau tableau, Actor a) {
		this.tableau = tableau;
		this.actor = a;
		this.selected = false;
		setActorBounds();

		ACMouseAdapter adapter = new ACMouseAdapter();
		addMouseListener(adapter);
		addMouseMotionListener(adapter);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		actor.paint(g);
		setActorBounds();

	}

	public void setActorBounds() {
		Point p = actor.getPos();
		Point size = actor.getSize();
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

	protected void moveActor(Point npos) {
		actor.setPos(actor.getPos().add(npos));
	}

	private class ACMouseAdapter extends MouseInputAdapter {
		private Point source;

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

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				source = new Point(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point npoint = new Point(e.getX(), e.getY());
			moveActor(npoint.diff(source));
			tableau.repaint();
		}

		private void mouseClicked2(MouseEvent e) {
			// ignore for now
		}

		private void mouseClicked1(MouseEvent e) {
			tableau.select(ActorComponent.this);
		}

	}

	public void remove() {
		actor.destroy();
		tableau.remove(this);
	}

	public Actor getActor() {
		return actor;
	}
}
