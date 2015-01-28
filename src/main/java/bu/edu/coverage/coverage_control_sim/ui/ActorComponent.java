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
 * Component holding a single actor. Delegates painting to the actor. It's
 * responsible for positioning and event handling.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public class ActorComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	protected static final int MIN_SIZE = 20;

	protected final Tableau tableau; // Containing tableau
	protected final Actor actor; // Associated actor
	protected boolean selected; // Selected status

	/**
	 * Creates a component for an actor with a containing tableau. The component
	 * is not added to the tableau here.
	 * 
	 * @param tableau
	 *            The containing tableau
	 * @param a
	 *            The associated actor
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

	protected void setActorBounds() {
		Point p = actor.getPos();
		Point size = actor.getSize();
		int x = (int) p.x;
		int y = (int) p.y;
		int w = drawSize(size.x);
		int h = drawSize(size.y);
		setBounds(x - w / 2, y - h / 2, w, h);
	}

	/**
	 * Sets the selected. If selected, a border will be displayed.
	 * 
	 * @param selected
	 *            The new selected status
	 */
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

	/**
	 * Removes the actor from the simulation and removes the component from the
	 * tableau.
	 */
	public void remove() {
		actor.destroy();
		tableau.remove(this);
	}

	/**
	 * Gets the actor associated with this component.
	 * 
	 * @return The actor
	 */
	public Actor getActor() {
		return actor;
	}

	/**
	 * Returns the minimum allowed size if the intended size is too small.
	 * 
	 * @param size
	 *            The intended size
	 * @return The maximum between the intended size and the minimum allowed
	 */
	public static int drawSize(double size) {
		return Math.max((int) size, MIN_SIZE);
	}
}
