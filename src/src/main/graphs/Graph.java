package main.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import main.utils.Utility;

public class Graph extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int MIN_BAR_WIDTH = 1;
	private static final int MIN_BAR_HEIGHT = 100;
	private Map<Integer, Number> mapHistory;
	private transient Map<Line2D, String> lineValues = new HashMap<Line2D, String>();
	private final Color color;
	private Integer median;
	private final int xOffset;
	private final int yOffset;

	@SuppressWarnings("unchecked")
	public Graph(Map<Integer, ?> mapHistory, Color color, Integer median) {
		this.mapHistory = (Map<Integer, Number>) mapHistory;
		this.color = color;
		this.median = median;
		int width = (mapHistory.size() * MIN_BAR_WIDTH);
		this.xOffset = 5;
		this.yOffset = 5;

		setBackground(new Color(213, 202, 189));
		setMinimumSize(new Dimension(width, MIN_BAR_HEIGHT));
		setPreferredSize(new Dimension(width, MIN_BAR_HEIGHT * 2));

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Rectangle2D cursorArea = new Rectangle2D.Float(e.getX(), e.getY(), 1, 1);
				for (Map.Entry<Line2D, String> entry : lineValues.entrySet()) {
					if (entry.getKey().intersects(cursorArea)) {
						setToolTipText(entry.getValue());
					}
				}
				ToolTipManager.sharedInstance().mouseMoved(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (mapHistory != null) {
			Graphics2D g2d = (Graphics2D) g.create();
			Line2D line;
			float maxValue = 0.0000000000001f;
			int frameWidth = getWidth() - (xOffset * 2);
			int frameHeight = getHeight() - (yOffset * 2);
			float barWidth = (float) frameWidth / mapHistory.size();
			float xPos = xOffset * 1.5f;
			float yPos;
			float barHeight;
			float value;

			for (Number mapValue : mapHistory.values()) {
				float doubleValue = mapValue.floatValue();
				maxValue = Math.max(maxValue, doubleValue % 1 != 0 ? doubleValue * 100 : doubleValue);
			}

			g2d.setStroke(new BasicStroke(barWidth+1));
			
			for (Map.Entry<Integer, Number> entry : mapHistory.entrySet()) {
				value = entry.getValue().floatValue();
				value = value % 1 != 0 ? value * 100 : value;
				barHeight = (value / maxValue) * (frameHeight-1);
				yPos = frameHeight + yOffset - barHeight;
				
				if (entry.getKey().equals(this.median)) {
					line = new Line2D.Float(xPos, yOffset, xPos, getHeight() - (float) yOffset);
					lineValues.put(line, "Valor medio:" + entry.getKey());
					g2d.setColor(Color.DARK_GRAY);
					g2d.draw(line);
				}
				
				line = new Line2D.Float(xPos, getHeight() - yOffset, xPos, yPos);
				lineValues.put(line, String.valueOf(value % 1 != 0 ? value + "%" : (int) value));
				
				float hue = (float) Math.floor(Utility.getHue(this.color));
				float light = entry.getKey() / 255f / 2f;
				int hsbColor = Color.getHSBColor(hue / 360f, 1f, light).getRGB();
				hsbColor &= ~(0xff << 24);
				hsbColor |= (128 & 0xff) << 24;
				g2d.setColor(new Color(hsbColor, true));
				g2d.draw(line);
				
				xPos += barWidth;
			}
			
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(.5f));
			g2d.drawRect(xOffset, yOffset, frameWidth, frameHeight);
			g2d.dispose();
		}
	}
}
