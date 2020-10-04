package main.gui;

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

import main.gui.graphs.LayeredGraph;
import main.utils.Pixel;
import main.utils.Utility;

public class Histogram implements Runnable {
	private static final double ZERO = 0.0;
	Thread thread;
	ArrayList<Pixel> image = new ArrayList<>();
	public final List<Map<Integer, Double>> colors;
	public final List<Map<Integer, Double>> colorsAcc;
	public final List<Map<Integer, Double>> normColors;
	public final List<Map<Integer, Double>> normColorsAcc;
	public final Double[] med;
	public final Double[] dev;
	public final Double[] ent;
	public final Integer[] min;
	public final Integer[] max;
	int[][] valores;
	int[] colorSizes;
	int numColors;

	public Histogram(BufferedImage image) throws InterruptedException {
		this.numColors = image.getColorModel().getNumColorComponents();
		this.colorSizes = new int[this.numColors];
		this.valores = new int[numColors][];
		this.colors = new ArrayList<>(numColors);
		this.colorsAcc = new ArrayList<>(numColors);
		this.normColors = new ArrayList<>(numColors);
		this.normColorsAcc = new ArrayList<>(numColors);

		for (int i = 0; i < numColors; i++) {
			this.valores[i] = new int[colorSizes[i]];
			this.colors.add(i, new TreeMap<>());
			this.colorsAcc.add(i, new TreeMap<>());
			this.normColors.add(i, new TreeMap<>());
			this.normColorsAcc.add(i, new TreeMap<>());
			this.colorSizes[i] = (int) Math.pow(2, image.getColorModel().getComponentSize()[i]);
		}

		this.med = new Double[numColors];
		this.dev = new Double[numColors];
		this.ent = new Double[numColors];
		this.min = new Integer[numColors];
		this.max = new Integer[numColors];

		Arrays.fill(min, -1);
		Arrays.fill(med, ZERO);

		Utility.imgApply(image, (i, j) -> {
			int colorIndex = numColors;
			int offsetAcc = 0;
			int bitMask = 0xff;
			for (int colorSize : image.getColorModel().getComponentSize()) {
				int colorValue = (image.getRGB(j, i) & bitMask) >> offsetAcc;
				this.valores[--colorIndex][colorValue]++;
				bitMask <<= colorSize;
				offsetAcc += colorSize;
			}
		});

		this.thread = new Thread(this, "histogram");
		this.thread.start();
		this.thread.join();
	}

	public String toString() {
		String[] colorNames = { "red", "green", "blue", "alpha" };
		String stringFormat = "%.5f";
		StringBuilder msg = new StringBuilder();
		msg.append("Media:\n\t");
		for (int i = 0; i < this.numColors; i++)
			msg.append(colorNames[i] + " " + String.format(stringFormat, med[i]) + ",");
		msg.deleteCharAt(msg.length() - 1);
		msg.append("\nMin/Max:\n\t");
		for (int i = 0; i < this.numColors; i++)
			msg.append(colorNames[i] + " [" + min[i] + "," + max[i] + "]\n\t");
		msg.deleteCharAt(msg.length() - 1);
		msg.append("Desv. tipica:\n\t");
		for (int i = 0; i < this.numColors; i++)
			msg.append(colorNames[i] + " " + String.format(stringFormat, dev[i]) + " ");
		msg.append("\nEntropia:\n\t");
		for (int i = 0; i < this.numColors; i++)
			msg.append(colorNames[i] + " " + String.format(stringFormat, ent[i]) + " ");
		return msg.toString();
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
		for (int i = 0; i < numColors; i++) {
			for (int j = 0; j < colorSizes[i]; j++) {
				colors.get(i).put(j, (double) valores[i][j]);

				if (min[i] == -1 && valores[i][j] > 0)
					min[i] = j;
				if (valores[i][j] != 0)
					max[i] = j;
			}
		}
	}

	// Valores acumulados y media
	private void acumulado() {
		for (int i = 0; i < numColors; i++) {
			for (int j = 0; j < colorSizes[i]; j++) {
				colorsAcc.get(i).put(j, valores[i][j] + colorsAcc.get(i).getOrDefault(j - 1 < 0 ? 0 : j - 1, ZERO));
			}
		}
	}

	// Valores normalizados
	private void normValues() {
		for (int i = 0; i < numColors; i++) {
			for (int j = 0; j < colorSizes[i]; j++) {
				normColors.get(i).put(j, valores[i][j] / colorsAcc.get(i).get(255));
				normColorsAcc.get(i).put(j, normColors.get(i).getOrDefault(j, ZERO)
						+ normColorsAcc.get(i).getOrDefault(j - 1 < 0 ? 0 : j - 1, ZERO));
			}
		}
	}

	// Media
	private void media() {
		for (int i = 0; i < numColors; i++) {
			double acc = 0;
			for (int j = 0; j < colorSizes[i]; j++) {
				acc += j * normColors.get(i).getOrDefault(j, ZERO);
			}
			med[i] = acc;
		}
	}

	// Desviacion tipica
	private void stdDev() {
		for (int i = 0; i < numColors; i++) {
			double acc = 0;
			for (int j = 0; j < colorSizes[i]; j++) {
				double val = normColors.get(i).getOrDefault(j, ZERO) * Math.pow(j - med[i], 2);
				if (Double.isFinite(val) && !Double.isNaN(val)) {
					acc += val;
				}
			}
			dev[i] = Math.sqrt(acc);
		}
	}

	// Entropia
	private void entropy() {
		for (int i = 0; i < numColors; i++) {
			double acc = 0;
			for (int j = 0; j < colorSizes[i]; j++) {
				double val = normColors.get(i).getOrDefault(j, ZERO)
						* Math.log10(normColors.get(i).getOrDefault(j, ZERO)) / Math.log10(2);
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
