/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.util.ParseException;
import bu.edu.coverage.coverage_control_sim.util.Parser;

/**
 * @author fran
 *
 */
public class CoverageApplet extends Applet implements ActionListener {

	public static final String LOAD = "Load";
	public static final String SAVE = "Save";
	public static final String START = "Start";
	public static final String RESTART = "Restart";
	public static final String RESET = "Reset";
	public static final String PAUSE = "Pause";
	public static final String RESUME = "Resume";
	public static final String SELECT = "Select";
	public static final String ADDAGENTS = "Add Agents";
	public static final String ADDTARGETS = "Add Targets";
	public static final String REMOVE = "Remove";
	public static final String TRAJECTORY = "Trajectories";

	public static final int SIZE = 500;

	protected Tableau t;
	protected JCheckBox trajectory;
	protected JCheckBox performance;
	protected JFileChooser fc;

	/**
	 * @throws HeadlessException
	 */
	public CoverageApplet() throws HeadlessException {

	}

	@Override
	public void start() {
		super.start();

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init() {
		super.init();

		try {
			EventQueue.invokeAndWait(new Runnable() {

				public void run() {
					createGUI();
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void createGUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800, 800));

		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				fc = new JFileChooser();
				return null;
			}
		});

		Director d = new Director();
		t = new Tableau(SIZE, SIZE, d);

		JPanel center = new JPanel();
		center.add(t);
		add("Center", center);

		JPanel top = new JPanel();
		top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		JPanel control = createControlPanel();
		JPanel mode = createModePanel();
		JPanel command = createCommandPanel();

		top.add(control);
		top.add(mode);
		top.add(command);
		add("North", top);

		JPanel left = new JPanel();

		JPanel layer = createLayerPanel();

		left.add(layer);
		add("West", left);

		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		t.setInfoPanel(right);

		add("East", right);

		t.addMaster();
		t.repaint();
	}

	private JPanel createModePanel() {
		JPanel mode = new JPanel();
		mode.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

		ButtonGroup group = new ButtonGroup();
		JToggleButton select = createToggleButton(SELECT, mode, this);
		group.add(select);
		group.add(createToggleButton(ADDAGENTS, mode, this));
		group.add(createToggleButton(ADDTARGETS, mode, this));

		select.setSelected(true);
		return mode;
	}

	private JPanel createLayerPanel() {
		JPanel layer = new JPanel();
		layer.setLayout(new BoxLayout(layer, BoxLayout.Y_AXIS));

		trajectory = createCheckBox(TRAJECTORY, layer, this);

		return layer;
	}

	private JPanel createControlPanel() {
		JPanel control = new JPanel();
		control.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

		createButton(LOAD, control, this);
		createButton(SAVE, control, this);
		createButton(START, control, this);
		createButton(RESUME, control, this);
		createButton(PAUSE, control, this);
		createButton(RESTART, control, this);
		createButton(RESET, control, this);

		return control;
	}

	private JPanel createCommandPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

		createButton(REMOVE, panel, this);

		return panel;
	}

	private JButton createButton(String text, Container container,
			ActionListener listener) {
		JButton b = new JButton(text);
		b.setActionCommand(text);
		b.addActionListener(listener);
		container.add(b);

		return b;
	}

	private JToggleButton createToggleButton(String text, Container container,
			ActionListener listener) {
		JToggleButton b = new JToggleButton(text);
		b.setActionCommand(text);
		b.addActionListener(listener);
		container.add(b);

		return b;
	}

	private JCheckBox createCheckBox(String text, Container container,
			ActionListener listener) {
		JCheckBox b = new JCheckBox(text);
		b.setActionCommand(text);
		b.addActionListener(listener);
		b.setSelected(true);
		container.add(b);

		return b;
	}

	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case LOAD: {
			int ret = fc.showOpenDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				try {
					Parser parser = new Parser(new FileInputStream(
							fc.getSelectedFile()));
					Director d = parser.input();
					t.setDirector(d);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		}
		case SAVE: {
			int ret = fc.showSaveDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				try {
					PrintStream st = new PrintStream(fc.getSelectedFile());
					st.print(t.getSaved().toCode());

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		}
		case START: {
			t.start();
			break;
		}
		case RESUME: {
			t.resume();
			break;
		}
		case RESTART: {
			t.restart();
			break;
		}
		case RESET: {
			t.reset();
			break;
		}
		case PAUSE: {
			t.pause();
			break;
		}
		case SELECT: {
			t.setMode(Mode.SELECT);
			break;
		}
		case ADDAGENTS: {
			t.setMode(Mode.ADD_AGENT);
			break;
		}
		case ADDTARGETS: {
			t.setMode(Mode.ADD_TARGET);
			break;
		}
		case REMOVE: {
			t.removeSelectedActor();
			break;
		}
		case TRAJECTORY: {
			t.setInfoLayerActive(Tableau.TRAJECTORIES, trajectory.isSelected());
		}
		default:
			break;
		}

	}
}
