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
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

import bu.edu.coverage.coverage_control_sim.actor.Actor;
import bu.edu.coverage.coverage_control_sim.actor.Agent;
import bu.edu.coverage.coverage_control_sim.actor.MasterAgent;
import bu.edu.coverage.coverage_control_sim.actor.Obstacle;
import bu.edu.coverage.coverage_control_sim.actor.Target;
import bu.edu.coverage.coverage_control_sim.comm.BasicComm;
import bu.edu.coverage.coverage_control_sim.control.ControlClient;
import bu.edu.coverage.coverage_control_sim.control.DeadlineDiscount;
import bu.edu.coverage.coverage_control_sim.control.Discount;
import bu.edu.coverage.coverage_control_sim.control.KLCRH;
import bu.edu.coverage.coverage_control_sim.event.Director;
import bu.edu.coverage.coverage_control_sim.sense.BasicSense;
import bu.edu.coverage.coverage_control_sim.sense.MasterSense;
import bu.edu.coverage.coverage_control_sim.ui.actorinfo.ActorInfo;
import bu.edu.coverage.coverage_control_sim.util.Point;

/**
 * @author fran
 *
 */
public class Tableau extends JLayeredPane implements ActionListener {
	protected static final String TRAJECTORIES = "Trajectories";
	public static final Integer INFO_LAYER = new Integer(0);
	public static final Integer OBSTACLE_LAYER = new Integer(1);
	public static final Integer TARGET_LAYER = new Integer(2);
	public static final Integer AGENT_LAYER = new Integer(3);
	// These two should be the highest
	public static final Integer TOP_LAYER = new Integer(9);
	public static final Integer GLASS_LAYER = new Integer(10);

	public final int width;
	public final int height;
	protected Director d;
	protected Director saved;
	protected Thread tableau;
	protected final Timer timer = new Timer(40, this);
	protected Mode mode;

	protected GlassLayer glass;
	protected DrawPolygonLayer draw_poly;
	protected HashMap<String, InfoLayer> info_layers;
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
		InfoLayer trajectory = new TrajectoryLayer(this);
		this.info_layers.put(TRAJECTORIES, trajectory);

		for (Map.Entry<String, InfoLayer> entry : info_layers.entrySet()) {

			entry.getValue().setPreferredSize(new Dimension(width, height));
			entry.getValue().setBounds(0, 0, width, height);
			setInfoLayerActive(entry.getKey(), true);
		}

		this.glass = new GlassLayer();
		glass.setPreferredSize(new Dimension(width, height));
		glass.setBounds(0, 0, width, height);
		glass.addMouseListener(new GlassMouseAdapter());
		this.draw_poly = new DrawPolygonLayer();
		draw_poly.setPreferredSize(new Dimension(width, height));
		draw_poly.setBounds(0, 0, width, height);

		addMouseListener(new TableauMouseAdapter());
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(Color.white);
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
			ActorInfo info = a.getInfoPanel(this);
			if (info != null && info.alwaysVisible()) {
				info_panel.add(info);
			}
			add(new ActorComponent(this, a), a.getLayer());
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
		if (saved == null) {
			saved = d.deepCopy();
		}
		setDirector(saved);
	}

	public void reset() {
		saved = new Director();
		restart();
		addMaster();
	}

	public void togglePause() {
		if (started) {
			timer.stop();
		} else {
			timer.start();
		}
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		setGlassLayerActive(glass, !(mode == Mode.SELECT));
		setGlassLayerActive(draw_poly, mode == Mode.ADD_OBST);
	}

	protected void setGlassLayerActive(GlassLayer layer, boolean active) {
		if (active) {
			if (!isAncestorOf(layer)) {
				add(layer, GLASS_LAYER);
			}
		} else {
			remove(layer);
		}
	}

	public void addTarget(Point p) {
		double size = 5;
		double v = 0;
		double heading = 0;
		Discount discount = new DeadlineDiscount(1, 1, 1000);
		double ireward = 100;
		Target t = new Target(d, p, size, v, heading, discount, ireward, true);
		ActorComponent ac = new ActorComponent(this, t);
		add(ac, TARGET_LAYER);
	}

	public void addAgent(Point p) {
		Point size = new Point(20, 20);
		double v = 2;
		double heading = 0;
		Agent a = new Agent(d, p, size, v, heading);
		a.setCommunication(new BasicComm());
		a.setSense(new BasicSense(1));
		a.setControl(new ControlClient());
		ActorComponent ac = new ActorComponent(this, a);
		add(ac, AGENT_LAYER);
	}

	public void addMaster() {
		int K = 2;
		double delta = 0.5;
		int b = 2;
		double gamma = 0;
		boolean actionh = false;
		MasterAgent master = new MasterAgent(d);
		master.setCommunication(new BasicComm());
		master.setControl(new KLCRH(K, delta, b, gamma, actionh));
		master.setSense(new MasterSense());
		ActorComponent ac = new ActorComponent(this, master);
		add(ac, new Integer(0));
		info_panel.add(master.getInfoPanel(this));
	}

	public void addObstacle(List<Point> points) {
		Obstacle obs = new Obstacle(d, points);
		ActorComponent ac = new ActorComponent(this, obs);
		add(ac, OBSTACLE_LAYER);
	}

	public void add(ActorComponent ac, Integer layer) {
		add((JComponent) ac, layer);
		actors.add(ac);
	}

	public void remove(ActorComponent ac) {
		remove((JComponent) ac);
		actors.remove(ac);
	}

	public void setInfoLayerActive(String layer_key, boolean active) {
		if (info_layers.containsKey(layer_key)) {
			InfoLayer layer = info_layers.get(layer_key);
			if (active) {
				if (!isAncestorOf(layer)) {
					add(layer, INFO_LAYER);
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
			ActorInfo ip = ac.getActor().getInfoPanel(this);
			if (ip != null) {
				info_panel.add(ac.getActor().getInfoPanel(this));
			}
			repaint();
		}
	}

	public void unselect() {
		if (selected != null) {
			ActorInfo ip = selected.getActor().getInfoPanel(this);
			if (ip != null) {
				info_panel.remove(ip);
			}
			selected.setSelected(false);
			selected = null;
			repaint();
		}
	}

	public void removeSelectedActor() {
		if (selected != null) {
			ActorInfo ip = selected.getActor().getInfoPanel(this);
			if (ip != null) {
				info_panel.remove(ip);
			}
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
		case ADD_OBST: {
			draw_poly.addPoint(p);
			break;
		}
		default:
			break;
		}

		repaint();

	}

	public void glassDoubleClick(MouseEvent e) {
		addObstacle(draw_poly.getPoints());
		draw_poly.clear();
	}

	public List<ActorComponent> getActors() {
		return actors;
	}

	public void actionPerformed(ActionEvent e) {
		d.runFor(1);
		// d.updateAll();
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
		for (InfoLayer layer : info_layers.values()) {
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
				if (e.getClickCount() == 1) {
					glassLeftClick(e);
				} else {
					glassDoubleClick(e);
				}
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
