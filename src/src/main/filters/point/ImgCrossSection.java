package main.filters.point;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;

import main.graphs.Graph;

public class ImgCrossSection {

	static public void profile(BufferedImage image, Point p1, Point p2) {
		Map<Integer, Integer> monochrome = new TreeMap<Integer, Integer>();
		Map<Integer, Double> derivative = new TreeMap<Integer, Double>();

		if (p1.x > p2.x) {
			Point tmp = (Point) p1.clone();
			p1 = (Point) p2.clone();
			p2 = (Point) tmp.clone();

		}

		double pendiente = (p2.y - p1.y) / (double) (p2.x - p1.x);
		double offset = p1.y - p1.x * pendiente;

		System.out.println(p1);
		System.out.println(p2);
		System.out.println(pendiente);
		System.out.println(offset);

		for (Integer point_x = p1.x; point_x <= p2.x; point_x++) {
			int point_y = (int) (pendiente * point_x + offset);
			int next_point_y = (int) (pendiente * (point_x + 1 >= image.getWidth() ? point_x : point_x + 1) + offset);

			int pixel = image.getRGB(point_x, point_y);
			int next_pixel = image.getRGB(point_x, next_point_y);

			// get separate colors
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = pixel & 0xff;
			int next_red = (next_pixel >> 16) & 0xff;
			int next_green = (next_pixel >> 8) & 0xff;
			int next_blue = next_pixel & 0xff;

			double monopixel = red * 0.222d + green * 0.707d + blue * 0.071d;
			double next_monopixel = next_red * 0.222d + next_green * 0.707d + next_blue * 0.071d;
			System.out.println(next_monopixel / monopixel);

			monochrome.put(point_x, new Integer((int) (monopixel)));
			derivative.put(point_x, new Double((1d + next_monopixel / monopixel) / 2d) + .00001);
		}

		Container container = new Container();
		container.add(new Graph(monochrome, Color.BLACK, -1));
		container.add(new Graph(derivative, Color.BLACK, -1));

		JFrame frame = new JFrame("Perfil de la imagen");

		container.setLayout(new GridLayout(2, 1));

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(container);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
