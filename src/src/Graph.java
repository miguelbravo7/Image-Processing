import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;

public class Graph extends JPanel {
		private static final long serialVersionUID = 1L;
		protected static final int MIN_BAR_WIDTH = 1;
		protected static final int MIN_BAR_HEIGHT = 100;
		private Map<Integer, Number> mapHistory;
		private Map<Rectangle2D, String> rectValue = new HashMap<Rectangle2D, String>();
		private Color color;
		private Integer median;
		
		@SuppressWarnings("unchecked")
		public Graph(Map<Integer, ? > mapHistory, Color color, Integer median) {
			this.mapHistory = (Map<Integer, Number>) mapHistory;
			this.color = color;
			this.median = median;
			int width = (mapHistory.size() * MIN_BAR_WIDTH) + 11;			
			
			setBackground(new Color(213, 202, 189));
			setMinimumSize(new Dimension(width, MIN_BAR_HEIGHT));
			setPreferredSize(new Dimension(width, MIN_BAR_HEIGHT*2));
			
			addMouseMotionListener(new MouseMotionListener() {			     
		        @Override
		        public void mouseMoved(MouseEvent e) {
		        	for(Rectangle2D rect : rectValue.keySet()) {
			            if(rect.contains(e.getPoint()))
			                setToolTipText(rectValue.get(rect));		        		
		        	}
			         
			        ToolTipManager.sharedInstance().mouseMoved(e);
			    }
			     
			    @Override
			    public void mouseDragged(MouseEvent e) {}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (mapHistory != null) {
				int xOffset = 5;
				int yOffset = 5;
				int width = getWidth() - 1 - (xOffset * 2);
				int height = getHeight() - 1 - (yOffset * 2);
				double maxValue = 0;
				int barWidth =  Math.max(MIN_BAR_WIDTH,  width / mapHistory.size());
				
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(0f));
				g2d.drawRect(xOffset, yOffset, width, height);
				
				float hue = (float) Math.floor(Utility.getHue(this.color));
				
				for (Number value : mapHistory.values()) 
					maxValue = Math.max(maxValue, value.doubleValue() % 1 != 0 ? value.doubleValue() * 100 : value.doubleValue());
				
				int xPos = xOffset;
				for (Integer key : mapHistory.keySet()) {
					double value = mapHistory.get(key).doubleValue();
					value = value % 1 != 0 ? value * 100 : value;
					double barHeight = (value / (float) maxValue) * height;
					double yPos = height + yOffset - barHeight;
					
					Rectangle2D bar;

					if(key.equals(median)) {
						bar = new Rectangle2D.Float(xPos, yOffset, barWidth, height);
						rectValue.put(bar, "Valor medio:" + key);
						g2d.fill(bar);
						g2d.draw(bar);
					}
					
					float light = key.equals(0) ? 0f : (key / 255f) / 2f;
															
					g2d.setColor(Color.getHSBColor(hue / 360f, 1f, light));
					
					bar = new Rectangle2D.Float(xPos, (int) yPos - 1, barWidth, (int) barHeight + 1);
					rectValue.put(bar, String.valueOf((value % 1 != 0 ? value + "%" : (int) value)));
					
					g2d.fill(bar);
					g2d.setColor(Color.DARK_GRAY);
					g2d.draw(bar);
					
					xPos += barWidth;
				}
				
				g2d.dispose();
			}
		}		
	}
