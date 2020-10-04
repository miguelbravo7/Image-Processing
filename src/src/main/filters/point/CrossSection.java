package main.filters.point;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import main.gui.graphs.Graph;

public class CrossSection {
	private static final Logger LOGGER = Logger.getLogger(CrossSection.class.getName());

	private CrossSection() {
		throw new IllegalStateException("Utility class");
	}

	public static void profile(BufferedImage image, Point p1, Point p2) {
		Map<Integer, Integer> monochrome = new TreeMap<>();
		Map<Integer, Double> derivative = new TreeMap<>();

		if (p1.x > p2.x) {
			Point tmp = (Point) p1.clone();
			p1 = (Point) p2.clone();
			p2 = (Point) tmp.clone();

		}

		double pendiente = (p2.y - p1.y) / (double) (p2.x - p1.x);
		double offset = p1.y - p1.x * pendiente;

		LOGGER.log(Level.FINE, "{0}", p1);
		LOGGER.log(Level.FINE, "{0}", p2);
		LOGGER.log(Level.FINE, "{0}", pendiente);
		LOGGER.log(Level.FINE, "{0}", offset);

		for (Integer point_x = p1.x; point_x <= p2.x; point_x++) {
			int yPoint = (int) (pendiente * point_x + offset);
			int nextyPoint = (int) (pendiente * (point_x + 1 >= image.getWidth() ? point_x : point_x + 1) + offset);

			int pixel = image.getRGB(point_x, yPoint);
			int nextPixel = image.getRGB(point_x, nextyPoint);

			// get separate colors
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = pixel & 0xff;
			int nextRed = (nextPixel >> 16) & 0xff;
			int nextGreen = (nextPixel >> 8) & 0xff;
			int nextBlue = nextPixel & 0xff;

			double monopixel = red * 0.222d + green * 0.707d + blue * 0.071d;
			double nextMonopixel = nextRed * 0.222d + nextGreen * 0.707d + nextBlue * 0.071d;
			LOGGER.log(Level.FINE, "{0}", nextMonopixel / monopixel);

			monochrome.put(point_x, (int) (monopixel));
			derivative.put(point_x, (1d + nextMonopixel / monopixel) / 2d + .00001);
		}

		Container container = new Container();
		container.add(new Graph(monochrome, Color.BLACK, -1));
		container.add(new Graph(derivative, Color.BLACK, -1));

		JFrame frame = new JFrame("Perfil de la imagen");

		container.setLayout(new GridLayout(2, 1));

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(container);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
