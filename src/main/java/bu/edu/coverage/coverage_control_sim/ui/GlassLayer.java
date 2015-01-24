package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * FIXME kill all
 * 
 * A wrapper Container for holding components that use a background Color
 * containing an alpha value with some transparency.
 * <p>
 * A Component that uses a transparent background should really have its opaque
 * property set to false so that the area it occupies is first painted by its
 * opaque ancestor (to make sure no painting artifacts exist). However, if the
 * property is set to false, then most Swing components will not paint the
 * background at all, so you lose the transparent background Color.
 * <p>
 * This components attempts to get around this problem by doing the background
 * painting on behalf of its contained Component, using the background Color of
 * the Component.
 * <p>
 * Addapted from
 * https://tips4java.wordpress.com/2009/05/31/backgrounds-with-transparency/
 * 
 */
public class GlassLayer extends JComponent {
	private static final long serialVersionUID = 1L;

	protected InfoLayer fg; // Actual displayed component
	/**
	 * If the layer is completely transparent, the default swing handling of
	 * non opaque components is used.
	 */
	public final boolean opaque;

	/**
	 * Creates a new glass layer with the given layer as the foreground. If the
	 * layer has a semitransparent background
	 * 
	 * @param fg
	 * @param opaque
	 */
	public GlassLayer(InfoLayer fg, boolean opaque) {
		this.opaque = opaque;
		this.fg = fg;
		setLayout(new BorderLayout());
		setOpaque(false);
		if (fg != null) {
			fg.setOpaque(false);
			add(fg);
		}
	}

	public void update() {
		if (fg != null) {
			fg.update();
		}
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		if (fg != null) {
			fg.setPreferredSize(preferredSize);
		}
	}

	/**
	 * Paint the background using the background Color of the
	 * contained component
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (opaque && fg != null) {
			g.setColor(fg.getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		} else {
			super.paintComponent(g);
		}
	}
}
