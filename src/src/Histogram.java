import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

public class Histogram implements Runnable {
	ArrayList<Pixel> image = new ArrayList<Pixel>();
	Map<Integer, Integer> red = new TreeMap<Integer, Integer>();
	Map<Integer, Integer> green = new TreeMap<Integer, Integer>();
	Map<Integer, Integer> blue = new TreeMap<Integer, Integer>();
	int[][] valores = new int[256][3];
	Integer tamaño = 0, accr = 0, accg = 0, accb = 0;

	public Histogram(ArrayList<Pixel> imagen) {

		this.image = imagen;

		Thread thread = new Thread(this, "histogram");
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return "Media: r " + (accr / tamaño) + ",g " + (accg / tamaño) + ",b " + (accb / tamaño);
	}

	@Override
	public void run() {
		tamaño = image.size();

		for (Pixel pixel : image) {
			valores[(int) pixel.get(1)][0]++;
			valores[(int) pixel.get(2)][1]++;
			valores[(int) pixel.get(3)][2]++;
		}

		for (int i = 0; i < valores.length; i++) {
			red.put(i, valores[i][0]);
			green.put(i, valores[i][1]);
			blue.put(i, valores[i][2]);
		}
		{
			int value = 0;
			for (int[] pixel : valores) {
				accr += pixel[0] * value;
				accg += pixel[1] * value;
				accb += pixel[2] * value++;
			}
		}
		image.clear();
		graphicHisogram();
	}

	public void graphicHisogram() {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		Container container = new Container();
		container.add(new Graph(red, "red", accr / tamaño));
		container.add(new Graph(green, "green", accg / tamaño));
		container.add(new Graph(blue, "blue", accb / tamaño));
		container.setLayout(new GridLayout(3, 1));
		frame.add(container);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	protected class Graph extends JPanel {
		private static final long serialVersionUID = 1L;
		protected static final int MIN_BAR_WIDTH = 3;
		private Map<Integer, Integer> mapHistory;
		private Map<Rectangle2D, String> rectValue = new HashMap<Rectangle2D, String>();
		private String color;
		private Integer median;
		
		public Graph(Map<Integer, Integer> mapHistory, String color, Integer median) {
			this.mapHistory = mapHistory;
			this.color = color;
			this.median = median;
			int width = (mapHistory.size() * MIN_BAR_WIDTH) + 11;			
			
			setMinimumSize(new Dimension(width, 128));
			setPreferredSize(new Dimension(width, 256));
			
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
				int maxValue = 0;
				int barWidth =  Math.max(MIN_BAR_WIDTH,  width / mapHistory.size());
				
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(Color.DARK_GRAY);
				g2d.drawRect(xOffset, yOffset, width, height);
				
				for (Integer value : mapHistory.values()) 
					maxValue = Math.max(maxValue, value);
				
				int xPos = xOffset;
				for (Integer key : mapHistory.keySet()) {
					int value = mapHistory.get(key);
					int barHeight = Math.round((value / (float) maxValue) * height);					
					int yPos = height + yOffset - barHeight;	
					
					Rectangle2D bar;

					if(key.equals(median)) {
						bar = new Rectangle2D.Float(xPos, yOffset, barWidth, height);
						rectValue.put(bar, "Valor medio:"+key);
						g2d.fill(bar);
						g2d.draw(bar);
					}
					
					g2d.setColor(new Color(color == "red" ? key : 0,
									color == "green" ? key : 0,
									color == "blue" ? key : 0));
					
					bar = new Rectangle2D.Float(xPos, yPos, barWidth, barHeight);
					rectValue.put(bar, String.valueOf(value));
					
					g2d.fill(bar);
					g2d.setColor(Color.DARK_GRAY);
					g2d.draw(bar);
					
					
					xPos += barWidth;
				}
				
				g2d.dispose();
			}
		}
		
	}
}
