/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import javax.swing.JComponent;

/**
 * Component used for displaying information on top of the tableau. Used by
 * GlassLayer component.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public abstract class InfoLayer extends JComponent {
	private static final long serialVersionUID = 1L;

	/**
	 * Updates the content of the layer.
	 */
	public abstract void update();
}
