/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui.actorinfo;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bu.edu.coverage.coverage_control_sim.ui.Tableau;

/**
 * Panel used for showing and changing information about a single actor.
 * Extending classes are supposed to define a singleton instance.
 * 
 * @author Francisco Penedo (franp@bu.edu)
 *
 */
public abstract class ActorInfo extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	// Labels
	protected static final String ID = "Id";
	protected static final String SET = "Set";

	protected Tableau tableau; // Associated tableau
	protected JButton set; // Set button
	protected JPanel info; // Panel holding the fields
	protected BoxLayout layout; // Layout of the panel
	protected HashMap<String, JTextField> fields; // Field retrievable by name

	// Non public constructor for singleton
	protected ActorInfo() {
		fields = new HashMap<>();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

		set = new JButton(SET);
		set.setActionCommand(SET);
		set.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		add(set, c);

		c.gridy = 1;
		c.insets = new Insets(10, 0, 0, 0);
		add(info, c);
	}

	protected void setTableau(Tableau tableau) {
		this.tableau = tableau;
	}

	// Adds a pair label-textfield with the given label
	protected void addInfoPair(String label) {
		JPanel pair = new JPanel(new FlowLayout());
		JLabel l = new JLabel(label);
		JTextField text = new JTextField(20);
		pair.add(l);
		pair.add(text);
		info.add(pair);
		fields.put(label, text);
	}

	/**
	 * Updates the panel
	 */
	public abstract void update();

	/**
	 * Sets the changes on the actor
	 */
	public abstract void set();

	/**
	 * 
	 * @return If the panel should always be visible
	 */
	public boolean alwaysVisible() {
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == SET) {
			set();
			tableau.repaint();
		}
	}

}
