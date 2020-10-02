package main.graphs;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.gui.LayeredGraph;
import main.utils.Pixel;
import main.utils.Utility;

public class Histogram implements Runnable {
	Thread thread;
	ArrayList<Pixel> image = new ArrayList<>();
	public final List<Map<Integer, Double>> colors;
	public final List<Map<Integer, Double>> colorsAcc;
	public final List<Map<Integer, Double>> normColors;
	public final List<Map<Integer, Double>> normColorsAcc;
	public final Double[] dev;
	public final Double[] ent;
	public final Integer[] min;
	public final Integer[] max;
	public final Integer[] med;
	Integer tamano = 0;
	Integer colorBits;
	Integer displayedGraphs;
	int[][] valores;

	public Histogram(BufferedImage image) throws InterruptedException {
		this.colorBits = 3;
		this.displayedGraphs = image.getType() == BufferedImage.TYPE_BYTE_GRAY ? 1 : 3;

		colors = new ArrayList<>(colorBits);
		colorsAcc = new ArrayList<>(colorBits);
		normColors = new ArrayList<>(colorBits);
		normColorsAcc = new ArrayList<>(colorBits);

		for (int i = 0; i < colorBits; i++) {
			colors.add(i, new TreeMap<>());
			colorsAcc.add(i, new TreeMap<>());
			normColors.add(i, new TreeMap<>());
			normColorsAcc.add(i, new TreeMap<>());
		}

		valores = new int[colorBits][256];

		med = new Integer[colorBits];
		dev = new Double[colorBits];
		ent = new Double[colorBits];
		min = new Integer[colorBits];
		max = new Integer[colorBits];

		Arrays.fill(min, -1);
		Arrays.fill(med, 0);

		Utility.imgApply(image, (i, j) -> {
			for (int offset = 0; offset < colorBits; offset++) {
				this.valores[colorBits - offset - 1][(image.getRGB(j, i) >> (8 * offset)) & 0xff]++;
			}
		});

		this.thread = new Thread(this, "histogram");
		this.thread.start();
		this.thread.join();
	}

	public String toString() {
		String msg;
		if (displayedGraphs == 3)
			msg = "Media:\n\t r " + med[0] + ",g " + med[1] + ",b " + med[2] + "\nMin/Max:\n\t red [" + min[0] + ","
					+ max[0] + "]\n\t green [" + min[1] + "," + max[1] + "]\n\t blue [" + min[2] + "," + max[2] + "]\n"
					+ "Desv. tipica:\n\t red " + String.format("%.4f", dev[0]) + " green "
					+ String.format("%.4f", dev[1]) + " blue " + String.format("%.4f", dev[2]) + "\n"
					+ "Entropia:\n\t red " + String.format("%.4f", ent[0]) + " green " + String.format("%.4f", ent[1])
					+ " blue " + String.format("%.4f", ent[2]) + "\n";
		else {
			msg = "Media: " + med[0];
			msg += "\n Min/Max: [" + min[0] + "," + max[0] + "]\n";
			msg += "Desv. tipica: " + String.format("%.4f", dev[0]) + "\n";
			msg += "Entropia: " + String.format("%.4f", ent[0]) + "\n";
		}
		return msg;
	}

	@Override
	public void run() {
		maxminhm();
		acumulado();
		normValues();
		media();
		stdDev();
		entropy();

		image.clear();
	}

	// Valores minimos maximos y paso de valores al hashmap
	private void maxminhm() {
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < colorBits; j++) {
				colors.get(j).put(i, (double) valores[j][i]);

				if (min[j] == -1 && valores[j][i] > 0)
					min[j] = i;
				if (valores[j][i] != 0)
					max[j] = i;
			}
		}
	}

	// Valores acumulados y media
	private void acumulado() {
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < colorBits; j++) {
				colorsAcc.get(j).put(i, valores[j][i] + colorsAcc.get(j).getOrDefault(i - 1 < 0 ? 0 : i - 1, 0.));
			}
		}
	}

	// Valores normalizados
	private void normValues() {
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < colorBits; j++) {
				normColors.get(j).put(i, valores[j][i] / (double) colorsAcc.get(j).get(255));
				normColorsAcc.get(j).put(i, normColors.get(j).getOrDefault(i, 0.)
						+ normColorsAcc.get(j).getOrDefault(i - 1 < 0 ? 0 : i - 1, 0.));
			}
		}
	}

	// Media
	private void media() {
		for (int i = 0; i < colorBits; i++) {
			double acc = 0;
			for (int j = 0; j < 256; j++) {
				acc += j * normColors.get(i).getOrDefault(j, 0.);
			}
			med[i] = (int) acc;
		}
	}

	// Desviacion tipica
	private void stdDev() {
		for (int i = 0; i < colorBits; i++) {
			double acc = 0;
			for (int j = 0; j < 255; j++) {
				double val = normColors.get(i).getOrDefault(j, 0.) * (j - med[i]) * (j - med[i]);
				if (Double.isFinite(val) && !Double.isNaN(val)) {
					acc += val;
				}
			}
			dev[i] = Math.sqrt(acc);
		}
	}

	// Entropia
	private void entropy() {
		for (int i = 0; i < colorBits; i++) {
			double acc = 0;
			for (int j = 0; j < 255; j++) {
				double val = normColors.get(i).getOrDefault(j, 0.0) * Math.log10(normColors.get(i).getOrDefault(j, 0.0))
						/ Math.log10(2);
				if (Double.isFinite(val) && !Double.isNaN(val)) {
					acc += val;
				}
			}
			ent[i] = -acc;
		}
	}

	public void histogram() {
		initializeGraph(colors);
	}

	public void histogramAcc() {
		initializeGraph(colorsAcc);
	}

	public void normHistogram() {
		initializeGraph(normColors);
	}

	public void normHistogramAcc() {
		initializeGraph(normColorsAcc);
	}

	private void initializeGraph(List<Map<Integer, Double>> hmArray) {
		JFrame frame = new JFrame("Histogram");
		frame.setLayout(new GridLayout(2, 1));
		
		JPanel texto = new JPanel();
		texto.add(new JLabel("<html>" + this.toString().replace("\n", "<br/>").replace("\t", "&ensp;") + "<html>",
		SwingConstants.LEFT));
		texto.setBackground(Color.LIGHT_GRAY);
		
		Container container = new LayeredGraph(hmArray, med);
		
		frame.add(container);
		frame.add(texto);
		
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
