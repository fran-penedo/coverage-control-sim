/**
 * 
 */
package bu.edu.coverage.coverage_control_sim.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

import bu.edu.coverage.coverage_control_sim.actor.Actor;
import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.MasterAgent;
import bu.edu.coverage.coverage_control_sim.actor.Target;
import bu.edu.coverage.coverage_control_sim.comm.BasicComm;
import bu.edu.coverage.coverage_control_sim.control.DeadlineDiscount;
import bu.edu.coverage.coverage_control_sim.control.Discount;
import bu.edu.coverage.coverage_control_sim.control.KLCRH;
import bu.edu.coverage.coverage_control_sim.control.KLCRHClient;
import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.sense.BasicSense;
import bu.edu.coverage.coverage_control_sim.sense.MasterSense;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class Tableau extends JPanel implements ActionListener {
	public static final String TRAJECTORIES = "Trajectories";

	public final int width;
	public final int height;
	protected Director d;
	protected Director saved;
	protected Thread tableau;
	protected final Timer timer = new Timer(40, this); // TODO parameter
	protected Mode mode;

	protected GlassLayer glass;
	protected HashMap<String, GlassLayer> info_layers;
	protected ArrayList<ActorComponent> actors;
	protected ActorComponent selected;
	protected JPanel info_panel;

	protected boolean started;
	protected boolean init;

	public Tableau(int width, int height, Director d) {
		this.init = false;
		this.width = width;
		this.height = height;
		this.d = d;
		started = false;

		this.mode = Mode.SELECT;
		this.actors = new ArrayList<>();

		this.info_layers = new HashMap<>();
		GlassLayer trajectory = new GlassLayer(new TrajectoryLayer(this), false);
		this.info_layers.put(TRAJECTORIES, trajectory);

		for (GlassLayer layer : info_layers.values()) {
			add(layer);
			layer.setPreferredSize(new Dimension(width, height));
			layer.setBounds(0, 0, width, height);
		}

		this.glass = new GlassLayer(null, false);
		glass.setPreferredSize(new Dimension(width, height));
		glass.setBounds(0, 0, width, height);
		glass.addMouseListener(new GlassMouseAdapter());

		addMouseListener(new TableauMouseAdapter());
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(null);
		setOpaque(true);
		this.init = true;
	}

	public void setDirector(Director d) {
		if (this.d != null) {
			for (ActorComponent ac : actors) {
				remove((JComponent) ac);
			}
			actors.clear();
		}
		this.d = d;
		for (Actor a : d.getActors()) {
			if (a instanceof MasterAgent) {
				MasterAgent m = (MasterAgent) a;
				info_panel.add(m.getInfoPanel(this));
			}
			add(new ActorComponent(this, a));
		}
		repaint();
	}

	public void start() {
		started = true;
		saved = d.deepCopy();
		timer.start();
	}

	public void resume() {
		if (started) {
			timer.start();
		}
	}

	public void restart() {
		started = false;
		timer.stop();
		setDirector(saved);
	}

	public void pause() {
		timer.stop();
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		if (mode == Mode.SELECT) {
			setGlassPaneActive(false);
		} else {
			setGlassPaneActive(true);
		}
	}

	protected void setGlassPaneActive(boolean active) {
		if (active) {
			if (!isAncestorOf(glass)) {
				add(glass);
			}
		} else {
			remove(glass);
		}
	}

	public void addTarget(Point p) {
		double size = 40;
		double v = 0;
		double heading = 0;
		Discount discount = new DeadlineDiscount(1, 1, 1000);
		double ireward = 100;
		Target t = new Target(d, p, size, v, heading, discount, ireward, true);
		ActorComponent ac = new ActorComponent(this, t);
		add(ac);
	}

	public void addAgent(Point p) {
		Point size = new Point(20, 20);
		double v = 2;
		double heading = 0;
		Agent a = new Agent(d, p, size, v, heading);
		a.setCommunication(new BasicComm());
		a.setSense(new BasicSense(1));
		a.setControl(new KLCRHClient());
		ActorComponent ac = new ActorComponent(this, a);
		add(ac);
	}

	public void addMaster() {
		int K = 2;
		double delta = 0.5;
		int b = 2;
		MasterAgent master = new MasterAgent(d);
		master.setCommunication(new BasicComm());
		master.setControl(new KLCRH(K, delta, b));
		master.setSense(new MasterSense());
		ActorComponent ac = new ActorComponent(this, master);
		add(ac);
		info_panel.add(master.getInfoPanel(this));
	}

	public void add(ActorComponent ac) {
		add((JComponent) ac);
		actors.add(ac);
	}

	public void remove(ActorComponent ac) {
		remove((JComponent) ac);
		actors.remove(ac);
	}

	public void setInfoLayerActive(String layer_key, boolean active) {
		if (info_layers.containsKey(layer_key)) {
			GlassLayer layer = info_layers.get(layer_key);
			if (active) {
				if (!isAncestorOf(layer)) {
					add(layer);
				}
			} else {
				remove(layer);
			}

		}
		repaint();
	}

	public void select(ActorComponent ac) {
		if (isAncestorOf(ac)) {
			unselect();
			ac.setSelected(true);
			selected = ac;
			info_panel.add(ac.getActor().getInfoPanel(this));
			repaint();
		}
	}

	public void unselect() {
		if (selected != null) {
			info_panel.remove(selected.getActor().getInfoPanel(this));
			selected.setSelected(false);
			selected = null;
			repaint();
		}
	}

	public void removeSelectedActor() {
		if (selected != null) {
			info_panel.remove(selected.getActor().getInfoPanel(this));
			selected.remove();
			selected = null;
			repaint();
		}
	}

	public void glassLeftClick(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());

		switch (mode) {
		case ADD_AGENT: {
			addAgent(p);
			break;
		}
		case ADD_TARGET: {
			addTarget(p);
			break;
		}
		default:
			break;
		}

		repaint();

	}

	public List<ActorComponent> getActors() {
		return actors;
	}

	public void actionPerformed(ActionEvent e) {
		d.runFor(1);
		d.updateAll();
		repaint();
	}

	@Override
	public void repaint() {
		if (init) {
			updateInfo();
		}
		super.repaint();
	}

	protected void updateInfo() {
		for (GlassLayer layer : info_layers.values()) {
			layer.update();
		}
		for (Component c : info_panel.getComponents()) {
			ActorInfo a = (ActorInfo) c;
			a.update();
		}
	}

	private class GlassMouseAdapter extends MouseInputAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				glassLeftClick(e);
			}
		}
	}

	private class TableauMouseAdapter extends MouseInputAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				unselect();
			}
		}
	}

	public Director getSaved() {
		if (started) {
			return saved;
		} else {
			return d.deepCopy();
		}
	}

	public void setInfoPanel(JPanel right) {
		this.info_panel = right;
	}

	public Director getDirector() {
		return d;
	}
}
