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
import java.lang.reflect.InvocationTargetException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import bu.edu.coverage.coverage_control_sim.event.Director;

/**
 * @author fran
 *
 */
public class CoverageApplet extends Applet implements ActionListener {

	public static final String RESUME = "Resume";
	public static final String PAUSE = "Pause";
	public static final String SELECT = "Select";
	public static final String ADDAGENTS = "Add Agents";
	public static final String ADDTARGETS = "Add Targets";

	public static final int SIZE = 500;

	protected Tableau t;

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

		Director d = new Director();
		t = new Tableau(SIZE, SIZE, d);
		t.addMaster();

		JPanel center = new JPanel();
		center.add(t);
		add("Center", center);

		JPanel top = new JPanel();
		top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		JPanel control = createControlPanel();
		JPanel mode = createModePanel();

		top.add(control);
		top.add(mode);
		add("North", top);
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

	private JPanel createControlPanel() {
		JPanel control = new JPanel();
		control.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

		createButton(RESUME, control, this);
		createButton(PAUSE, control, this);

		return control;
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

	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case RESUME: {
			t.start();
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
		default:
			break;
		}

	}
}
