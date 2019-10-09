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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

public class Histogram implements Runnable {
	ArrayList<Pixel> image = new ArrayList<Pixel>();
	ArrayList<Map<Integer, Integer>> colores, colores_acc;	
	boolean flagacc;
	int[][] valores, acumulados;
	Integer tamano = 0, acc[];
	Integer med[];
	Integer min[], max[];
	Integer color_bits;

	public Histogram(ArrayList<Pixel> imagen, int color_bits, boolean acumulado) {
		this.flagacc = acumulado;
		this.image = imagen;
		this.color_bits = color_bits;
		
		colores = new ArrayList<Map<Integer, Integer>>(color_bits);
		colores_acc = new ArrayList<Map<Integer, Integer>>(color_bits);
		
		for (int i = 0; i < color_bits; i++) {
			colores.add(i, new TreeMap<Integer, Integer>());
			colores_acc.add(i, new TreeMap<Integer, Integer>());
		}
		
		acumulados = new int[256][color_bits];
		valores = new int[256][color_bits];
		
		acc = new Integer[color_bits];
		med = new Integer[color_bits];
		min = new Integer[color_bits];
		max = new Integer[color_bits];
		Arrays.fill(acc, -1);
		Arrays.fill(min, -1);

		Thread thread = new Thread(this, "histogram");
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(this);
	}

	public String toString() {
		String msg = new String();
		if(color_bits == 3)
			msg = "Media: r " + med[0] + ",g " + med[1] + ",b " + med[2] + ": " + (acc[0] + acc[1] +acc[2])/tamano
				+"\nMin/Max:\n\t red ["+ min[0] +","+ max[0] +"]\n\t green ["+ min[1] +","+ max[1] +"]\n\t blue ["+ min[2] +","+ max[2] +"]\n";
		else {
			msg ="Media:\n";
			for (int i = 0; i < color_bits; i++) {
				 msg += "valor "+ i + " ->" +med[i];
				 msg += " : " + (acc[0]*3)/tamano
						+"\nMin/Max:\n";
				 msg += "\t valor "+i+"["+ min[i] +","+ max[i] +"]\n";				
			}
		}
		return msg;
	}

	@Override
	public void run() {
		tamano = image.size();

		for (Pixel pixel : image) {
			for (int j = 0; j < color_bits; j++) {	
				valores[(int) pixel.get(j+1)][j]++;
			}
		}

		//valores minimos maximos y paso de valores al hashmap
		for (int i = 0; i < valores.length; i++) {
			for (int j = 0; j < color_bits; j++) {
				colores.get(j).put(i, valores[i][j]);
				if (min[j] == -1 && valores[i][j] != 0) min[j] = i;
				if (valores[i][j] != 0) max[j] = i;
			}
		}
		
		//valores acumulados y media
		{
			int value = 0;
			for (int[] pixel : valores) {
				for (int j = 0; j < color_bits; j++) {	
					acc[j] += pixel[j] * value;
					colores_acc.get(j).put(value, pixel[j] + colores_acc.get(j).getOrDefault(value-1 <0 ? 0 : value-1, 0));
				}
				++value;
			}
			for (int j = 0; j < med.length; j++) {	
				med[j] = acc[j] / tamano;
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
		
		if (flagacc) {
			if (color_bits == 3) {						
				container.add(new Graph(colores_acc.get(0), "red", med[0]));
				container.add(new Graph(colores_acc.get(1), "green", med[1]));
				container.add(new Graph(colores_acc.get(2), "blue", med[2]));
			}else
				container.add(new Graph(colores_acc.get(0), "grey", med[0]));
		}
		else {
			if (color_bits == 3) {						
				container.add(new Graph(colores.get(0), "red", med[0]));
				container.add(new Graph(colores.get(1), "green", med[1]));
				container.add(new Graph(colores.get(2), "blue", med[2]));
			}else
				container.add(new Graph(colores.get(0), "grey", med[0]));
		}		
		
			
		
		container.setLayout(new GridLayout(color_bits, 1));
		frame.add(container);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	protected class Graph extends JPanel {
		private static final long serialVersionUID = 1L;
		protected static final int MIN_BAR_WIDTH = 2;
		private Map<Integer, Integer> mapHistory;
		private Map<Rectangle2D, String> rectValue = new HashMap<Rectangle2D, String>();
		private String color;
		private Integer median;
		
		public Graph(Map<Integer, Integer> mapHistory, String color, Integer median) {
			this.mapHistory = mapHistory;
			this.color = color;
			this.median = median;
			int width = (mapHistory.size() * MIN_BAR_WIDTH) + 11;			
			
			setBackground(new Color(213, 202, 189));
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
				g2d.setColor(Color.BLACK);
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
					
					bar = new Rectangle2D.Float(xPos, yPos-1, barWidth, barHeight+1);
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
