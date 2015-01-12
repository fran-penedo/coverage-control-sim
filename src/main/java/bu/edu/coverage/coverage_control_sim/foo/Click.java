package bu.edu.coverage.coverage_control_sim.foo;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.ui.ActorComponent;
import bu.edu.coverage.coverage_control_sim.util.Point;

public class Click extends Applet {

	// public static void main(String[] args) {
	// // Create and set up the window.
	// JFrame frame = new JFrame("MouseEventDemo");
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	// // Create and set up the content pane.
	// JComponent newContentPane = new Click();
	// newContentPane.setOpaque(true); // content panes must be opaque
	// frame.setContentPane(newContentPane);
	//
	// // Display the window.
	// frame.pack();
	// frame.setVisible(true);
	// }

	public Click() throws HeadlessException {

	}

	@Override
	public void init() {
		super.init();

		try {
			EventQueue.invokeAndWait(new Runnable() {

				public void run() {
					makeGUI();
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

	public void makeGUI() {
		setLayout(new BorderLayout());
		JLabel l = new JLabel("Foo");
		JPanel p = new JPanel();
		// p.add(l);
		JPanel center = new JPanel();
		center.add(p);
		add("Center", center);
		p.setLayout(null);
		p.setPreferredSize(new Dimension(450, 450));
		p.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		// p.setBounds(50, 10, 450, 450);
		// JPanel p2 = new JPanel();
		// p.add(p2);
		// p2.setPreferredSize(new Dimension(400, 400));
		// p2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		// p2.setBounds(0, 0, 400, 400);

		ActorComponent p2 = new ActorComponent(new Agent(new Director(),
				new Point(225, 225), new Point(400, 400), 0, 0));
		p.add(p2);

		// addMouseListener(new MouseInputAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// System.out.println("panel");
		// }
		// });
		//
		// p.addMouseListener(new MouseInputAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// System.out.println("panel 2");
		// }
		// });

		// p2.addMouseListener(new MouseInputAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// System.out.println("panel 3");
		// }
		// });

		setPreferredSize(new Dimension(450, 450));
		// setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
}
