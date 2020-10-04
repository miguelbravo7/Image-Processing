package main.gui.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.ToolTipManager;

public class Graph extends JLabel {
	private static final long serialVersionUID = 1L;
	private static final int MIN_BAR_WIDTH = 1;
	private static final int MIN_BAR_HEIGHT = 100;
	private Map<Integer, Number> mapHistory;
	private transient Map<Line2D, String> lineValues = new HashMap<>();
	private final Color color;
	private Integer median;
	public float heightScale = 1f;

	@SuppressWarnings("unchecked")
	public Graph(Map<Integer, ?> mapHistory, Color color, Integer median) {
		this.mapHistory = (Map<Integer, Number>) mapHistory;
		this.color = color;
		this.median = median;
		int width = (mapHistory.size() * MIN_BAR_WIDTH);

		this.setOpaque(false);
		this.setMinimumSize(new Dimension(width, MIN_BAR_HEIGHT));
		this.setPreferredSize(new Dimension(width, MIN_BAR_HEIGHT * 2));

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Rectangle2D cursorArea = new Rectangle2D.Float(e.getX(), e.getY(), 1, 1);
				for (Map.Entry<Line2D, String> entry : lineValues.entrySet()) {
					if (entry.getKey().intersects(cursorArea)) {
						((JLabel) e.getSource()).setToolTipText(entry.getValue());
					}
				}
				ToolTipManager.sharedInstance().mouseMoved(e);
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		this.lineValues.clear();
		Graphics2D g2d = (Graphics2D) g.create();
		Polygon polygon = new Polygon();
		Line2D line;
		float maxValue = 0.0000000000001f;
		double valueWidthOffset = (double) getWidth() / mapHistory.size();
		float xPos = 0;
		float yPos = 0;
		float barHeight;
		float value;
		
		g2d.setColor(Color.LIGHT_GRAY);

		for (Number mapValue : mapHistory.values()) {
			float doubleValue = mapValue.floatValue();
			maxValue = Math.max(maxValue, doubleValue % 1 != 0 ? doubleValue * 100 : doubleValue);
		}

		polygon.addPoint((int)xPos, getHeight());
		
		for (Map.Entry<Integer, Number> entry : mapHistory.entrySet()) {
			value = entry.getValue().floatValue();
			value = value % 1 != 0 ? value * 100 : value;
			barHeight = (value / maxValue) * getHeight() * this.heightScale;
			yPos = getHeight() - barHeight;
			
			if (entry.getKey().equals(this.median)) {
				line = new Line2D.Float(xPos, 0, xPos, getHeight());
				lineValues.put(line, "Valor medio:" + entry.getKey());
				g2d.draw(line);
			}
			
			line = new Line2D.Float(xPos, getHeight(), xPos, yPos);
			lineValues.put(line, String.valueOf(value % 1 != 0 ? value + "%" : (int) value));
			polygon.addPoint((int)xPos, (int)yPos);
			xPos += valueWidthOffset;
		}
		polygon.addPoint((int)xPos, (int)yPos);
		polygon.addPoint(getWidth(), getHeight());
		g2d.setColor(this.getColor(64));
		g2d.fill(polygon);

		g2d.dispose();
	}

	public Color getColor() {
		return this.getColor(255);
	}
	public Color getColor(int transparencyMask){
		int hsbColor = this.color.getRGB();
		hsbColor &= ~(0xff << 24);
		hsbColor |= (transparencyMask & 0xff) << 24;
		return new Color(hsbColor, true);
	}
}
