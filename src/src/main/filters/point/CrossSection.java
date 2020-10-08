package main.filters.point;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import main.gui.graphs.ColorGraph;

public class CrossSection {
	private static final Logger LOGGER = Logger.getLogger(CrossSection.class.getName());

	private CrossSection() {
		throw new IllegalStateException("Utility class");
	}

	public static void profile(BufferedImage image, Point p1, Point p2) {
		Map<Integer, Integer> monochrome = new TreeMap<>();
		Map<Integer, Integer> derivative = new TreeMap<>();
		LOGGER.log(Level.FINE, "{0}", p1);
		LOGGER.log(Level.FINE, "{0}", p2);

		List<Point> linePoints = bresenhamAlgorithm(p1, p2);
		Point prev = linePoints.remove(0);
		int i = 0;

		for (Point curr : linePoints) {
			int pixel = image.getRGB(prev.x, prev.y);
			int nextPixel = image.getRGB(curr.x, curr.y);

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

			monochrome.put(i, (int) (monopixel));
			derivative.put(i, (int) (nextMonopixel - monopixel));
			prev = curr;
			i++;
		}

		Container container = new Container();
		container.add(new ColorGraph(monochrome, Color.BLACK, -1));
		container.add(new ColorGraph(derivative, Color.BLACK, -1));

		JFrame frame = new JFrame("Perfil de la imagen");

		container.setLayout(new GridLayout(2, 1));

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(container);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static List<Point> bresenhamAlgorithm(Point p1, Point p2) {
		List<Point> res = new ArrayList<>();
		p1 = (Point) p1.clone();
		p2 = (Point) p2.clone();
		int w = p2.x - p1.x;
		int h = p2.y - p1.y;
		int dx1 = 0;
		int dy1 = 0;
		int dx2 = 0;
		int dy2 = 0;
		if (w < 0){
			dx1 = dx2 = -1;
		}
		else if (w > 0) {
			dx1 = dx2 = 1;
		}
		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (longest <= shortest) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			res.add(new Point(p1.x, p1.y));
			numerator += shortest;
			if (numerator >= longest) {
				numerator -= longest;
				p1.x += dx1;
				p1.y += dy1;
			} else {
				p1.x += dx2;
				p1.y += dy2;
			}
		}
		return res;
	}

}
