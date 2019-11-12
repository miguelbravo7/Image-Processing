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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;

public class Histogram implements Runnable {
	Thread thread;
	ArrayList<Pixel> image = new ArrayList<Pixel>();
	ArrayList<Map<Integer, Double>> colores, colores_acc;
	ArrayList<Map<Integer, Double>> norm_colores, norm_colores_acc;	
	int[][] valores;
	Integer tamano = 0;
	Integer med[];
	Double dev[],ent[];
	Integer min[], max[];
	Integer color_bits, displayed_graphs;

	public Histogram(BufferedImage imagen) {
		this.image = ImgConvert.toPixelArrayList(imagen);
		this.color_bits = 3;
		this.displayed_graphs = imagen.getType() == BufferedImage.TYPE_BYTE_GRAY ? 1 : 3;

		colores = new ArrayList<Map<Integer, Double>>(color_bits);
		colores_acc = new ArrayList<Map<Integer, Double>>(color_bits);
		norm_colores = new ArrayList<Map<Integer, Double>>(color_bits);
		norm_colores_acc = new ArrayList<Map<Integer, Double>>(color_bits);
		
		for (int i = 0; i < color_bits; i++) {
			colores.add(i, new TreeMap<Integer, Double>());
			colores_acc.add(i, new TreeMap<Integer, Double>());
			norm_colores.add(i, new TreeMap<Integer, Double>());
			norm_colores_acc.add(i, new TreeMap<Integer, Double>());
		}
		
		valores = new int[color_bits][256];
		
		med = new Integer[color_bits];
		dev = new Double[color_bits];
		ent = new Double[color_bits];
		min = new Integer[color_bits];
		max = new Integer[color_bits];
		
		Arrays.fill(min, -1);
		Arrays.fill(med, 0);

		this.thread = new Thread(this, "histogram");
		this.thread.start();
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

	public String toString() {
		String msg = new String();
		if(color_bits == 3)
			msg = "Media:\n\t r " + med[0] + ",g " + med[1] + ",b " + med[2] + ": " + colores_acc.get(0).get(255) + "/" + tamano
				+"\nMin/Max:\n\t red ["+ min[0] +","+ max[0] +"]\n\t green ["+ min[1] +","+ max[1] +"]\n\t blue ["+ min[2] +","+ max[2] +"]\n"
				+"Desv. tipica:\n\t red "+ String.format("%.4f",dev[0]) +" green "+ String.format("%.4f",dev[1]) +" blue "+ String.format("%.4f",dev[2]) +"\n"
				+"Entropia:\n\t red "+ String.format("%.4f",ent[0]) +" green "+ String.format("%.4f",ent[1]) +" blue "+ String.format("%.4f",ent[2]) +"\n";
		else {
				msg =  "Media: ->" + med[0];
				msg += "\n Intervalo min-max [" + min[0] + "," + max[0] + "]\n";
				msg += "Desv. tipica ->" + String.format("%.4f",dev[0]) + "\n";	
				msg += "Entropia ->" + String.format("%.4f",ent[0]) + "\n";
		}
		return msg;
	}

	@Override
	public void run() {
		this.tamano = image.size();

		for (Pixel pixel : image) {
			for (int j = 0; j < color_bits; j++) {	
				this.valores[j][(int) pixel.get(j+1)]++;
			}
		}
		maxminhm();
		acumulado();
		normValues();
		media();
		stdDev();
		entropy();
		
		image.clear();
	}

	//valores minimos maximos y paso de valores al hashmap
	private void maxminhm() {
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < color_bits; j++) {
				colores.get(j).put(i, (double) valores[j][i]);
				if (min[j] == -1 && valores[j][i] != 0) min[j] = i;
				if (valores[j][i] != 0) max[j] = i;
			}
		}
	}

	//valores acumulados y media
	private void acumulado() {
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < color_bits; j++) {	
				colores_acc.get(j).put(i, valores[j][i] + colores_acc.get(j).getOrDefault(i-1 < 0 ? 0 : i-1, (double) 0));
			}
		}
	}
	//Valores normalizados
	private void normValues() {
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < color_bits; j++) {
				norm_colores.get(j).put(i, valores[j][i]/(double) colores_acc.get(j).get(255));
				norm_colores_acc.get(j).put(i, norm_colores.get(j).getOrDefault(i, (double) 0) + norm_colores_acc.get(j).getOrDefault(i-1 < 0 ? 0 : i-1, (double) 0));
			}
		}
	}
	//media
	private void media() {
		for (int i = 0; i < color_bits; i++) {
			double acc = 0;
			for(int j = 0; j < 256; j++) {
				acc += j * norm_colores.get(i).getOrDefault(j, (double) 0);				
			}
			med[i] = (int) acc;
		}		
	}
	//desviacion tipica
	private void stdDev() {
		for (int i= 0; i< color_bits; i++) {
			double acc=0;
			for (int j = 0; j < 255; j++) {
				double val = norm_colores.get(i).getOrDefault(j, (double) 0) * (j - med[i])*(j - med[i]);
				if(Double.isFinite(val) && !Double.isNaN(val)) {
					acc += val;
				}
			}		
			dev[i] = Math.sqrt(acc);
		}
	}
	
	//entropia
	private void entropy() {
		for (int i= 0; i< color_bits; i++) {
			double acc = 0;
			for (int j = 0; j < 255; j++) {
				double val = norm_colores.get(i).getOrDefault(j, (double) 0) * Math.log10(norm_colores.get(i).getOrDefault(j, (double) 0))/Math.log10(2);
				if(Double.isFinite(val) && !Double.isNaN(val)) {
					acc += val;
				}
			}		
			ent[i] = -acc;
		}
	}

	public void histogram(){
        initializeGraph(colores);
	}
	public void histogramAcc(){
		initializeGraph(colores_acc);
	}
	
	public void normHistogram(){
		initializeGraph(norm_colores);			
	}
	
	public void normHistogramAcc(){
		initializeGraph(norm_colores_acc);		
	}
	
	private void initializeGraph(ArrayList<Map<Integer, Double>> hm_array) {
		Container container = new Container();
		
		if (displayed_graphs == 3) {						
			container.add(new Graph(hm_array.get(0), "red", med[0]));
			container.add(new Graph(hm_array.get(1), "green", med[1]));
			container.add(new Graph(hm_array.get(2), "blue", med[2]));
		}else
			container.add(new Graph(hm_array.get(0), "grey", med[0]));
		
		graphicHistogram(container);
	}

	private void graphicHistogram(Container container) {
		JFrame frame = new JFrame("Histogram");
		
		JPanel texto = new JPanel();
		texto.add(new JLabel("<html>" + this.toString().replaceAll("\n","<br/>").replaceAll("\t", "&ensp;") + "<html>", SwingConstants.LEFT));
		texto.setBackground(new Color(213, 202, 189));
		container.add(texto);

		container.setLayout(new GridLayout(color_bits+1, 1));
		
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());	
		
		frame.add(container);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	protected class Graph extends JPanel {
		private static final long serialVersionUID = 1L;
		protected static final int MIN_BAR_WIDTH = 1;
		protected static final int MIN_BAR_HEIGHT = 100;
		private Map<Integer, Number> mapHistory;
		private Map<Rectangle2D, String> rectValue = new HashMap<Rectangle2D, String>();
		private String color;
		private Integer median;
		
		@SuppressWarnings("unchecked")
		public Graph(Map<Integer, ? > mapHistory, String color, Integer median) {
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
				g2d.drawRect(xOffset, yOffset, width, height);
				
				for (Number value : mapHistory.values()) 
					maxValue = Math.max(maxValue, value.doubleValue() % 1 != 0 ? value.doubleValue()*100 : value.doubleValue());
				
				int xPos = xOffset;
				for (Integer key : mapHistory.keySet()) {
					double value = mapHistory.get(key).doubleValue();
					value = value % 1 != 0 ? value*100 : value;
					double barHeight = (value / (float) maxValue) * height;					
					double yPos = height + yOffset - barHeight;	
					
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
					
					bar = new Rectangle2D.Float(xPos, (int)yPos-1, barWidth, (int) barHeight+1);
					rectValue.put(bar, String.valueOf((value % 1 != 0 ? value + "%" : (int)value)));
					
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
