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
import java.util.ArrayList;

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
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class CoverageApplet extends Applet implements ActionListener {
	private static final long serialVersionUID = 1L;

	// Button labels
	protected static final String LOAD = "Load";
	protected static final String SAVE = "Save";
	protected static final String START = "Start";
	protected static final String RESTART = "Restart";
	protected static final String RESET = "Reset";
	protected static final String PAUSE = "Pause";
	protected static final String RESUME = "Resume";
	protected static final String SELECT = "Select";
	protected static final String ADDAGENTS = "Add Agents";
	protected static final String ADDTARGETS = "Add Targets";
	protected static final String ADDOBSTACLES = "Add Obstacles";
	protected static final String REMOVE = "Remove";
	protected static final String TRAJECTORY = "Trajectories";

	// Size of the tableau. Should be made mutable at some point.
	protected static final int SIZE = 500;

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
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void createGUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800, 800));

		// Get permission for accessing the file system
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				fc = new JFileChooser();
				return null;
			}
		});

		// Create the tableau and add it to the center
		Director d = new Director();
		t = new Tableau(SIZE, SIZE, d);

		JPanel center = new JPanel();
		center.add(t);
		add("Center", center);

		// Create the top panels
		JPanel top = new JPanel();
		top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		JPanel control = createControlPanel();
		JPanel mode = createModePanel();
		JPanel command = createCommandPanel();

		top.add(control);
		top.add(mode);
		top.add(command);
		add("North", top);

		// Create the left panel (used for toggling information layers
		JPanel left = new JPanel();

		JPanel layer = createLayerPanel();

		left.add(layer);
		add("West", left);

		// Create the right panel (used for information panels)
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		t.setInfoPanel(right);

		add("East", right);

		// Add master to tableau (need everything setup so it can spawn the
		// information panel
		t.addMaster();
		DEBUGOBS(t);
		t.repaint();
	}

	private void DEBUGOBS(Tableau t) {
		ArrayList<Point> ps = new ArrayList<>();
		ps.add(new Point(100, 100));
		ps.add(new Point(200, 100));
		ps.add(new Point(100, 200));
		t.addObstacle(ps);
	}

	private JPanel createModePanel() {
		JPanel mode = new JPanel();
		mode.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

		ButtonGroup group = new ButtonGroup();
		JToggleButton select = createToggleButton(SELECT, mode, this);
		group.add(select);
		group.add(createToggleButton(ADDAGENTS, mode, this));
		group.add(createToggleButton(ADDTARGETS, mode, this));
		group.add(createToggleButton(ADDOBSTACLES, mode, this));

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

	// Creates a button with the given text and adds the listener to it. The
	// action command string will be the same as the text
	private JButton createButton(String text, Container container,
			ActionListener listener) {
		JButton b = new JButton(text);
		b.setActionCommand(text);
		b.addActionListener(listener);
		container.add(b);

		return b;
	}

	// Creates a toggle button with the given text and adds the listener to it.
	// The action command string will be the same as the text
	private JToggleButton createToggleButton(String text, Container container,
			ActionListener listener) {
		JToggleButton b = new JToggleButton(text);
		b.setActionCommand(text);
		b.addActionListener(listener);
		container.add(b);

		return b;
	}

	// Creates a check box with the given text and adds the listener to it. The
	// action command string will be the same as the text. Defaults to selected
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
					// FIXME will fail if called twice I think
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
					st.close();

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
		case ADDOBSTACLES: {
			t.setMode(Mode.ADD_OBST);
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
