/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author fran
 *
 */
public abstract class ActorInfo extends JPanel implements ActionListener {
	public static final String ID = "Id";
	public static final String SET = "Set";

	protected Tableau tableau;
	protected JButton set;
	protected JPanel info;
	protected BoxLayout layout;
	protected HashMap<String, JTextField> fields;

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
		add(info, c);

		addInfoPair(ID);
	}

	protected void setTableau(Tableau tableau) {
		this.tableau = tableau;
	}

	protected void addInfoPair(String label) {
		JPanel pair = new JPanel(new FlowLayout());
		JLabel l = new JLabel(label);
		JTextField text = new JTextField(20);
		pair.add(l);
		pair.add(text);
		info.add(pair);
		fields.put(label, text);
	}

	protected abstract void update();

	protected abstract void set();

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == SET) {
			set();
			tableau.repaint();
		}
	}

}
