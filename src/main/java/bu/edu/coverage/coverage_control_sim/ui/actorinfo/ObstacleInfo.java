/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui.actorinfo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import bu.edu.coverage.coverage_control_sim.actor.Obstacle;
import bu.edu.coverage.coverage_control_sim.ui.Tableau;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class ObstacleInfo extends ActorInfo {
	private static final long serialVersionUID = 1L;

	// Singleton instance
	static protected ObstacleInfo instance = new ObstacleInfo();

	protected static final String ADD = "Add point";
	protected static final String REMOVE = "Remove point";

	// Referring agent
	protected Obstacle a;
	protected JTable table;
	protected JButton add;
	protected JButton remove;

	protected ObstacleInfo() {
		this.table = new JTable(new ObsTableModel());
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		JPanel master = new JPanel(new BorderLayout());
		master.add("Center", scrollPane);

		this.add = new JButton(ADD);
		add.setActionCommand(ADD);
		add.addActionListener(this);
		this.remove = new JButton(REMOVE);
		remove.setActionCommand(REMOVE);
		remove.addActionListener(this);
		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(add);
		buttons.add(remove);
		master.add("South", buttons);

		info.add(master);
		info.setPreferredSize(new Dimension(200, 300));
		remove(set);
	}

	/**
	 * Obtains the info panel for the obstacle associated to the given tableau.
	 * Only one panel can exist at any given time.
	 * 
	 * @param a
	 *            The Obstacle
	 * @param t
	 *            The containing tableau
	 * @return The info panel associated to the obstacle
	 */
	static public ObstacleInfo getObstacleInfo(Obstacle a, Tableau t) {
		instance.setObstacle(a);
		instance.setTableau(t);
		return instance;
	}

	protected void setObstacle(Obstacle a) {
		this.a = a;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.ui.actorinfo.ActorInfo#update()
	 */
	@Override
	public void update() {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bu.edu.coverage.coverage_control_sim.ui.actorinfo.ActorInfo#set()
	 */
	@Override
	public void set() {
		// Ignore, no need for set button
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		switch (e.getActionCommand()) {
		case ADD: {
			((ObsTableModel) table.getModel()).addPoint();
			break;
		}
		case REMOVE: {
			((ObsTableModel) table.getModel()).removePoint();
		}
		}
	}

	class ObsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		protected final String[] cols = { "x", "y" };

		@Override
		public int getColumnCount() {
			return cols.length;
		}

		@Override
		public String getColumnName(int column) {
			return cols[column];
		}

		@Override
		public int getRowCount() {
			return a.getPoints().size(); // +1 header
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			Point p = a.getPoints().get(arg0);
			if (arg1 == 0) {
				return p.x;
			} else {
				return p.y;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			Double value = (Double) aValue;
			Point p;
			if (columnIndex == 0) {
				p = new Point(value, (double) getValueAt(rowIndex, 1));
			} else {
				p = new Point((double) getValueAt(rowIndex, 0), value);
			}
			a.setPoint(rowIndex, p);
			fireTableCellUpdated(rowIndex, columnIndex);
			tableau.repaint();
		}

		public void addPoint() {
			int size = a.getPoints().size();
			a.setPoint(size, a.getPoints().get(size - 1));
			fireTableRowsInserted(size, size);
		}

		public void removePoint() {
			if (table.getSelectedRow() > -1) {
				a.removePoint(table.getSelectedRow());
				fireTableRowsDeleted(table.getSelectedRow(),
						table.getSelectedRow());
				tableau.repaint();
			}
		}

	}

}
