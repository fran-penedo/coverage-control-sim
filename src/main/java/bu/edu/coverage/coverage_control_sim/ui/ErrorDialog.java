/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * @author fran
 *
 */
public class ErrorDialog {
	public static void showQuickErrorDialog(Component parent, Throwable e) {
		// create and configure a text area - fill it with exception text.
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		textArea.setText(writer.toString());

		// stuff it in a scrollpane with a controlled size.
		final JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 400));
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				scrollPane.getViewport().setViewPosition(new Point(0, 0));
			}
		});

		// pass the scrollpane to the joptionpane.
		JOptionPane.showMessageDialog(parent, scrollPane,
				"An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
	}
}
