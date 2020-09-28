package main.graphs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.utils.Pixel;

public class Histogram implements Runnable {
	Thread thread;
	ArrayList<Pixel> image = new ArrayList<Pixel>();
	ArrayList<Map<Integer, Double>> colores, colores_acc;
	ArrayList<Map<Integer, Double>> norm_colores, norm_colores_acc;
	int[][] valores;
	Integer tamano = 0;
	Integer med[];
	Double dev[], ent[];
	Integer min[], max[];
	Integer color_bits, displayed_graphs;

	public Histogram(BufferedImage image) {
		this.color_bits = 3;
		this.displayed_graphs = image.getType() == BufferedImage.TYPE_BYTE_GRAY ? 1 : 3;

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

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				for (int w = 0; w < color_bits; w++) {
					this.valores[color_bits - w - 1][(image.getRGB(j, i) >> (8 * w)) & 0xff]++;
				}
			}
		}

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
		if (displayed_graphs == 3)
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
			for (int j = 0; j < color_bits; j++) {
				colores.get(j).put(i, (double) valores[j][i]);

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
			for (int j = 0; j < color_bits; j++) {
				colores_acc.get(j).put(i,
						valores[j][i] + colores_acc.get(j).getOrDefault(i - 1 < 0 ? 0 : i - 1, (double) 0));
			}
		}
	}

	// Valores normalizados
	private void normValues() {
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < color_bits; j++) {
				norm_colores.get(j).put(i, valores[j][i] / (double) colores_acc.get(j).get(255));
				norm_colores_acc.get(j).put(i, norm_colores.get(j).getOrDefault(i, (double) 0)
						+ norm_colores_acc.get(j).getOrDefault(i - 1 < 0 ? 0 : i - 1, (double) 0));
			}
		}
	}

	// Media
	private void media() {
		for (int i = 0; i < color_bits; i++) {
			double acc = 0;
			for (int j = 0; j < 256; j++) {
				acc += j * norm_colores.get(i).getOrDefault(j, (double) 0);
			}
			med[i] = (int) acc;
		}
	}

	// Desviacion tipica
	private void stdDev() {
		for (int i = 0; i < color_bits; i++) {
			double acc = 0;
			for (int j = 0; j < 255; j++) {
				double val = norm_colores.get(i).getOrDefault(j, (double) 0) * (j - med[i]) * (j - med[i]);
				if (Double.isFinite(val) && !Double.isNaN(val)) {
					acc += val;
				}
			}
			dev[i] = Math.sqrt(acc);
		}
	}

	// Entropia
	private void entropy() {
		for (int i = 0; i < color_bits; i++) {
			double acc = 0;
			for (int j = 0; j < 255; j++) {
				double val = norm_colores.get(i).getOrDefault(j, (double) 0)
						* Math.log10(norm_colores.get(i).getOrDefault(j, (double) 0)) / Math.log10(2);
				if (Double.isFinite(val) && !Double.isNaN(val)) {
					acc += val;
				}
			}
			ent[i] = -acc;
		}
	}

	public void histogram() {
		initializeGraph(colores);
	}

	public void histogramAcc() {
		initializeGraph(colores_acc);
	}

	public void normHistogram() {
		initializeGraph(norm_colores);
	}

	public void normHistogramAcc() {
		initializeGraph(norm_colores_acc);
	}

	private void initializeGraph(ArrayList<Map<Integer, Double>> hm_array) {
		Container container = new Container();

		if (displayed_graphs == 3) {
			container.add(new Graph(hm_array.get(0), Color.RED, med[0]));
			container.add(new Graph(hm_array.get(1), Color.GREEN, med[1]));
			container.add(new Graph(hm_array.get(2), Color.BLUE, med[2]));
		} else
			container.add(new Graph(hm_array.get(0), Color.GRAY, med[0]));

		graphicHistogram(container);
	}

	private void graphicHistogram(Container container) {
		JFrame frame = new JFrame("Histogram");

		JPanel texto = new JPanel();
		texto.add(new JLabel("<html>" + this.toString().replaceAll("\n", "<br/>").replaceAll("\t", "&ensp;") + "<html>",
				SwingConstants.LEFT));
		texto.setBackground(new Color(213, 202, 189));
		container.add(texto);

		container.setLayout(new GridLayout(displayed_graphs + 1, 1));

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(container);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
