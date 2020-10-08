package main.filters.point;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.gui.Histogram;
import main.utils.Pair;
import main.utils.Utility;

public class LinealTransform {
	private static final Logger LOGGER = Logger.getLogger(LinealTransform.class.getName());
	private static final int UPPER_BOUND = 255;
	private static final int LOWER_BOUND = 0;

	private LinealTransform() {
		throw new IllegalStateException("Utility class");
	}

	public static BufferedImage transform(BufferedImage image, List<Point> points) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Collections.sort(points, new PointCompare());
		List<Point> filledPoints = fillPoints(points);

		for (Point p : points) {
			LOGGER.log(Level.FINE, "{0}", p);
		}

		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			int redValue = traduceValue(red, filledPoints);
			int greenValue = traduceValue(green, filledPoints);
			int blueValue = traduceValue(blue, filledPoints);

			pixel = (alpha << 24) | (redValue << 16) | (greenValue << 8) | blueValue; // pixel

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "Lineal transform done.");
		return img;
	}

	public static List<Point> fillPoints(List<Point> points) {
		List<Point> pointsCopy = new ArrayList<>(points);
		if (pointsCopy.get(0).x != LOWER_BOUND) {
			Pair<Point, Point> res = boundedIntersection(pointsCopy.get(0), pointsCopy.get(1));
			pointsCopy.add(0, res.getLeft());
			if (res.getLeft().getX() > LOWER_BOUND) {
				pointsCopy.add(0, new Point(LOWER_BOUND, LOWER_BOUND));
			}
		}
		int lastPoint = pointsCopy.size() - 1;
		if (pointsCopy.get(lastPoint).x != UPPER_BOUND) {
			Pair<Point, Point> res = boundedIntersection(pointsCopy.get(lastPoint - 1), pointsCopy.get(lastPoint));
			pointsCopy.add(res.getRight());
			if (res.getRight().getX() < UPPER_BOUND) {
				pointsCopy.add(new Point(UPPER_BOUND, LOWER_BOUND));
			}
		}
		return pointsCopy;
	}

	/**
	 * Calculates the two intersection points of a line in a defined range.
	 *
	 * @param pointA first coordinate
	 * @param pointB second coordinate
	 * 
	 * @return pair of intersection points
	 */
	private static final Pair<Point, Point> boundedIntersection(final Point pointA, final Point pointB) {// y = ax + b
		double slope = (pointB.y - pointA.y) / (double) (pointB.x - pointA.x);
		int offset = (int) (pointA.y - pointA.x * slope);
		int xLower = (int) ((LOWER_BOUND - offset) / slope);
		int xUpper = (int) ((UPPER_BOUND - offset) / slope);
		int yLower = (int) (slope * LOWER_BOUND + offset);
		int yUpper = (int) (slope * UPPER_BOUND + offset);
		int[] results = {xLower, xUpper, yLower, yUpper};
		for (int i = 0; i < results.length; i++) {
			if (LOWER_BOUND > results[i]) {
				results[i] = LOWER_BOUND;
			}
			if (results[i] > UPPER_BOUND) {
				results[i] = UPPER_BOUND;
			}
		}
		final Point left;
		final Point right;
		if (slope > -0.000001) {
			left = new Point(results[0], results[2]);
			right = new Point(results[1], results[3]);
		} else {			
			left = new Point(results[1], results[2]);
			right = new Point(results[0], results[3]);
		}

		// Looking for slope cutting points
		return new Pair<>(left, right);
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
