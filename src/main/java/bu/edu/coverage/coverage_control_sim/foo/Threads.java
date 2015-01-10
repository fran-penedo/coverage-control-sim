package bu.edu.coverage.coverage_control_sim.foo;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Threads extends JPanel implements ActionListener {
	Label l;

	public static void main(String[] args) {
		// Create and set up the window.
		JFrame frame = new JFrame("MouseEventDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new Threads();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public Threads() {
		setPreferredSize(new Dimension(450, 450));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		l = new Label("0");
		add(l);
		JButton b = new JButton("Button");
		b.addActionListener(this);
		add(b);

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				l.start();
			}
		});
	}

	class Label extends JLabel implements ActionListener {
		Timer t = new Timer(1000, this);
		int i = 0;

		public Label(String s) {
			super(s);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setText("" + ++i);
		}

		public void start() {
			t.start();
		}

		public void setI(int i) {
			this.i = i;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		l.setI(0);

	}
}
