package main.filters.geometric;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.ImgConvert;
import main.utils.Utility;
import main.utils.Pair;

public class Rotation {
	private static final Logger LOGGER = Logger.getLogger(Rotation.class.getName());

	public static BufferedImage rotate(BufferedImage image, Double degrees, String function) {
		Double deg = degrees % 360;

		int alpha = 0 & 0xff;
		int red = 255 & 0xff;
		int green = 255 & 0xff;
		int blue = 255 & 0xff;

		int def = (alpha << 24) | (red << 16) | (green << 8) | blue;

		Pair<Double, Double> poo = new Pair<Double, Double>(0d, 0d);
		Pair<Double, Double> pxo = translatePoint(new Point(image.getWidth(), 0), degrees);
		Pair<Double, Double> poy = translatePoint(new Point(0, image.getHeight()), degrees);
		Pair<Double, Double> pxy = translatePoint(new Point(image.getWidth(), image.getHeight()), degrees);

		int minX = (int) Math.min(poo.x, Math.min(pxo.x, Math.min(poy.x, pxy.x)));
		int minY = (int) Math.min(poo.y, Math.min(pxo.y, Math.min(poy.y, pxy.y)));
		int maxX = (int) Math.max(poo.x, Math.max(pxo.x, Math.max(poy.x, pxy.x)));
		int maxY = (int) Math.max(poo.y, Math.max(pxo.y, Math.max(poy.y, pxy.y)));

		LOGGER.log(Level.FINE, Integer.toString(minX));
		LOGGER.log(Level.FINE, Integer.toString(minY));
		LOGGER.log(Level.FINE, Integer.toString(maxX));
		LOGGER.log(Level.FINE, Integer.toString(maxY));

		int width = maxX - minX;
		int height = maxY - minY;
		Integer[][] array = ImgConvert.toIntArray(image);

		BufferedImage img = new BufferedImage(width, height, image.getType());

		Utility.imgApply(img, (i, j) -> {
			Pair<Double, Double> og = translatePoint(new Point(j + minX, i + minY), -deg);

			if (og.y >= 0 && og.y < image.getHeight() && og.x >= 0 && og.x < image.getWidth()) {
				try {
					img.setRGB(j, i, (int) Utility.methodMap.get(function).invoke(new Object(), array, og.x, og.y,
							image.getWidth(), image.getHeight()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// break;
				}
			} else {
				img.setRGB(j, i, def);
			}
		});
		LOGGER.log(Level.FINE, "{0} rotation done.", function);
		return img;
	}

	static Pair<Double, Double> translatePoint(Point p, double rotation) {
		double cos = Math.cos(Math.toRadians(rotation));
		double sin = Math.sin(Math.toRadians(rotation));
		return new Pair<Double, Double>(p.x * cos - p.y * sin, p.x * sin + p.y * cos);
	}

	public static BufferedImage rotateDirect(BufferedImage image, Double degrees, String funcName) {
		final Double modDeg = degrees % 360;

		Pair<Double, Double> poo = new Pair<Double, Double>(0d, 0d);
		Pair<Double, Double> pxo = translatePoint(new Point(image.getWidth(), 0), modDeg);
		Pair<Double, Double> poy = translatePoint(new Point(0, image.getHeight()), modDeg);
		Pair<Double, Double> pxy = translatePoint(new Point(image.getWidth(), image.getHeight()), modDeg);

		double minX = Math.min(poo.x, Math.min(pxo.x, Math.min(poy.x, pxy.x)));
		double minY = Math.min(poo.y, Math.min(pxo.y, Math.min(poy.y, pxy.y)));
		double maxX = Math.max(poo.x, Math.max(pxo.x, Math.max(poy.x, pxy.x)));
		double maxY = Math.max(poo.y, Math.max(pxo.y, Math.max(poy.y, pxy.y)));

		LOGGER.log(Level.FINE, "min x {0}", minX);
		LOGGER.log(Level.FINE, "min y {0}", minY);
		LOGGER.log(Level.FINE, "max x {0}", maxX);
		LOGGER.log(Level.FINE, "max y {0}", maxY);

		int width = (int) (maxX - minX);
		int height = (int) (maxY - minY);
		Integer[][] array = ImgConvert.toIntArray(image);

		BufferedImage img = new BufferedImage(width, height, image.getType());

		Utility.imgApply(image, (i, j) -> {
			Pair<Double, Double> og = translatePoint(new Point(j, i), modDeg);
			og.x += Math.abs(minX);
			og.y += Math.abs(minY);
			int mappedX = (int) Math.floor(og.x) == width ? width - 1 : (int) Math.floor(og.x);
			int mappedY = (int) Math.floor(og.y) == height ? height - 1 : (int) Math.floor(og.y);
			try {
				img.setRGB(mappedX, mappedY, (int) Utility.methodMap.get(funcName).invoke(new Object(), array,
						j + og.x % 1, i + og.y % 1, image.getWidth(), image.getHeight()));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// break;
			}
		});

		LOGGER.log(Level.FINE, "{0} direct rotation done.", funcName);
		return img;
	}

}
