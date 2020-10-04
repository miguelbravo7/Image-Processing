package main.filters.point;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.gui.Histogram;
import main.utils.Utility;

public class SpecHist {
	private static final Logger LOGGER = Logger.getLogger(SpecHist.class.getName());

	private SpecHist() {
		throw new IllegalStateException("Utility class");
	}

	public static BufferedImage convertHist(BufferedImage image, Histogram imgHist, Histogram refImgHist) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		TreeMap<Integer, Integer> rLut = makeLut(refImgHist.normColorsAcc.get(0), imgHist.normColorsAcc.get(0));
		TreeMap<Integer, Integer> gLut = makeLut(refImgHist.normColorsAcc.get(1), imgHist.normColorsAcc.get(1));
		TreeMap<Integer, Integer> bLut = makeLut(refImgHist.normColorsAcc.get(2), imgHist.normColorsAcc.get(2));

		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			int redValue = rLut.get(red);
			int greenValue = gLut.get(green);
			int blueValue = bLut.get(blue);

			pixel = (alpha << 24) | (redValue << 16) | (greenValue << 8) | blueValue;

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "Histogram specification done.");
		return img;
	}

	static TreeMap<Integer, Integer> makeLut(Map<Integer, Double> reference, Map<Integer, Double> original) {
		TreeMap<Integer, Integer> res = new TreeMap<>();
		for (Map.Entry<Integer, Double> entry : original.entrySet()) {
			int i = 0;
			while (i < reference.size() && entry.getValue() > reference.get(i)) {
				i++;
			}
			if (nearestValue(entry.getValue(), reference.getOrDefault(i - 1, 0d), reference.get(i)) == reference
					.get(i)) {
				res.put(entry.getKey(), i);
			} else {
				res.put(entry.getKey(), i - 1);
			}
		}
		return res;
	}

	private static double nearestValue(double point, double left, double right) {
		double res;
		if (Math.abs(point - left) < Math.abs(point - right)) {
			res = left;
		} else
			res = right;
		return res;
	}
}
