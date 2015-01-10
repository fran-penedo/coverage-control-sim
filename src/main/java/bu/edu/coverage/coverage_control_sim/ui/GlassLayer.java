package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A wrapper Container for holding components that use a background Color
 * containing an alpha value with some transparency.
 *
 * A Component that uses a transparent background should really have its
 * opaque property set to false so that the area it occupies is first painted
 * by its opaque ancestor (to make sure no painting artifacts exist). However,
 * if the property is set to false, then most Swing components will not paint
 * the background at all, so you lose the transparent background Color.
 *
 * This components attempts to get around this problem by doing the
 * background painting on behalf of its contained Component, using the
 * background Color of the Component.
 */
public class GlassLayer extends JComponent {
	private JPanel panel;
	public final boolean opaque;

	public GlassLayer(boolean opaque) {
		this.opaque = opaque;
		panel = new JPanel();
		setLayout(new BorderLayout());
		setOpaque(false);
		panel.setOpaque(false);
		add(panel);

	}

	/**
	 * Paint the background using the background Color of the
	 * contained component
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (opaque) {
			g.setColor(panel.getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		} else {
			super.paintComponent(g);
		}
	}
}
