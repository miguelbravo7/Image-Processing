package main.filters.point;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.gui.Histogram;
import main.utils.Utility;

public class LinealTransform {
	private static final Logger LOGGER = Logger.getLogger(LinealTransform.class.getName());

	private LinealTransform() {
		throw new IllegalStateException("Utility class");
	}

	public static BufferedImage transform(BufferedImage image, List<Point> points) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		Collections.sort(points, new PointCompare());
		traducePoints(points);
		for (Point p : points) {
			LOGGER.log(Level.FINE, "{0}", p);
		}

		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			int redValue = traduceValue(red, points);
			int greenValue = traduceValue(green, points);
			int blueValue = traduceValue(blue, points);

			pixel = (alpha << 24) | (redValue << 16) | (greenValue << 8) | blueValue; // pixel

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "Lineal transform done.");
		return img;
	}

	private static void traducePoints(List<Point> points) {
		if (points.get(0).x != 0) { // y = ax + b
			double pendiente = (points.get(1).y - points.get(0).y) / (double) (points.get(1).x - points.get(0).x);
			int offset = points.get(0).x - points.get(0).y;

			// Looking for slope cutting points
			if (pendiente == 0) {
				points.add(0, new Point(0, offset));
			} else if (pendiente > 0) {
				int cutPoint = (int) (-offset / pendiente);

				points.add(0, new Point(cutPoint, 0));
				if (cutPoint > 0)
					points.add(0, new Point(0, 0));
			} else {
				int cutPoint = (int) (255 - offset / pendiente);

				points.add(0, new Point(cutPoint, 255));
				if (cutPoint > 0)
					points.add(0, new Point(0, 255));
			}
		}
		int lastPoint = points.size() - 1;
		if (points.get(lastPoint).x != 255) {
			double pendiente = (points.get(lastPoint).y - points.get(lastPoint - 1).y)
					/ (double) (points.get(lastPoint).x - points.get(lastPoint - 1).x);
			int offset = points.get(lastPoint - 1).x - points.get(lastPoint - 1).y;

			if (pendiente == 0) {
				points.add(new Point(255, offset));
			} else if (pendiente > 0) {
				int cutPoint = (int) (255 - offset / pendiente);

				points.add(new Point(cutPoint, 255));
				if (cutPoint < 255)
					points.add(new Point(255, 255));
			} else {
				int cutPoint = (int) (255 - offset / pendiente);

				points.add(new Point(cutPoint, 0));
				if (cutPoint < 255)
					points.add(new Point(255, 0));
			}
		}
	}

	static class PointCompare implements Comparator<Point> {
		public int compare(final Point a, final Point b) {
			if (a.x < b.x) {
				return -1;
			} else if (a.x > b.x) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public static BufferedImage ecualize(BufferedImage image, Histogram imgHist) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			int redValue = (int) Math.floor(imgHist.normColorsAcc.get(0).getOrDefault(red, (double) 0) * 255);
			int greenValue = (int) Math.floor(imgHist.normColorsAcc.get(1).getOrDefault(green, (double) 0) * 255);
			int blueValue = (int) Math.floor(imgHist.normColorsAcc.get(2).getOrDefault(blue, (double) 0) * 255);

			pixel = (alpha << 24) | (redValue << 16) | (greenValue << 8) | blueValue; // pixel

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "Ecualization done.");
		return img;
	}

	private static int traduceValue(int point, List<Point> points) {
		double pendiente = 0;
		double offset = 0;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).x > point) {
				pendiente = (points.get(i).y - points.get(i - 1).y) / (double) (points.get(i).x - points.get(i - 1).x);
				offset = points.get(i - 1).y - points.get(i - 1).x * pendiente;
				break;
			}
		}
		return (int) Math.max(Math.min(pendiente * point + offset, 255), 0);
	}
}
