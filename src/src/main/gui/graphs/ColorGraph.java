package main.gui.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.ToolTipManager;

public class ColorGraph extends JLabel {
	private static final long serialVersionUID = 1L;
	private static final int MIN_BAR_WIDTH = 1;
	private static final int MIN_BAR_HEIGHT = 100;
	private Map<Integer, Number> mapHistory;
	private transient Map<Integer, Integer> sectionValues = new HashMap<>();
	private transient Map<Integer, String> sectionLabels = new HashMap<>();
	private final Color color;
	private Integer median;
	public float heightScale = 1f;
	public float upperBound = Float.MIN_VALUE;
	public float lowerBound = Float.MAX_VALUE;
	public float spaceBound = Float.MAX_VALUE;

	@SuppressWarnings("unchecked")
	public ColorGraph(Map<Integer, ?> mapHistory, Color color, Integer median) {
		this.mapHistory = (Map<Integer, Number>) mapHistory;
		this.color = color;
		this.median = median;
		int width = (mapHistory.size() * MIN_BAR_WIDTH);

		this.setOpaque(false);
		this.setMinimumSize(new Dimension(width, MIN_BAR_HEIGHT));
		this.setPreferredSize(new Dimension(width, MIN_BAR_HEIGHT * 2));

		for (Object mapValue : mapHistory.values()) {
			float value = ((Number) mapValue).floatValue();
			upperBound = Math.max(upperBound, value);
			lowerBound = Math.min(lowerBound, value);
		}
		spaceBound = upperBound - lowerBound;

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (sectionValues.containsKey(e.getX())
				&& Math.signum(sectionValues.get(e.getX())) == Math.signum(getHeight()*upperBound/spaceBound - e.getY())
				&& Math.abs(sectionValues.get(e.getX())) > Math.abs(getHeight()*upperBound/spaceBound - e.getY())) {
					((JLabel) e.getSource()).setToolTipText(sectionLabels.get(e.getX()));
				}
				ToolTipManager.sharedInstance().mouseMoved(e);
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Polygon polygon = new Polygon();
		double xStepOffset = (double) getWidth() / mapHistory.size();
		float scale = getHeight() / (upperBound - lowerBound);
		int xPos = 0;
		double yPos = 0;
		this.sectionValues.clear();

		g2d.translate(0, (int) (scale * upperBound));
		g2d.scale(xStepOffset, -1);
		
		g2d.drawLine(0, 0, (int) (scale * upperBound), 0);
		g2d.drawLine(this.median, 0, this.median, getHeight());
		polygon.addPoint(xPos, 0);

		for (Map.Entry<Integer, Number> entry : mapHistory.entrySet()) {
			double value = entry.getValue().doubleValue();
			yPos = value * scale * this.heightScale;

			addSection(xPos * xStepOffset, yPos, String.valueOf(value % 1 != 0 ? value + "%" : (int) value));
			polygon.addPoint(xPos, (int) yPos);
			xPos++;
		}
		addSection(this.median * xStepOffset, (double) getHeight(), "Valor medio:" + this.median);
		polygon.addPoint(xPos, (int) yPos);
		polygon.addPoint(getWidth(), 0);
		g2d.setColor(this.getColor((byte) 64));
		g2d.fill(polygon);

		g2d.dispose();
	}

	public Color getColor() {
		return this.getColor((byte) 255);
	}

	public Color getColor(byte transparencyMask) {
		int hsbColor = this.color.getRGB();
		hsbColor &= ~(0xff << 24);
		hsbColor |= (transparencyMask & 0xff) << 24;
		return new Color(hsbColor, true);
	}

	private void addSection(double x, double y, String label) {
		sectionValues.put((int) x, (int) y);
		sectionLabels.put((int) x, label);
	}
}
