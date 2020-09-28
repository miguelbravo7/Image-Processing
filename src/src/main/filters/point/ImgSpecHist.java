package main.filters.point;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

import main.graphs.Histogram;

public class ImgSpecHist {

	static public BufferedImage convertHist(BufferedImage image, Histogram img_hist, Histogram ref_img_hist) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Map<Integer, Integer> lut_r = makeLut(ref_img_hist.norm_colores_acc.get(0), img_hist.norm_colores_acc.get(0));
		Map<Integer, Integer> lut_g = makeLut(ref_img_hist.norm_colores_acc.get(1), img_hist.norm_colores_acc.get(1));
		Map<Integer, Integer> lut_b = makeLut(ref_img_hist.norm_colores_acc.get(2), img_hist.norm_colores_acc.get(2));

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				int red_value = lut_r.get(red);
				int green_value = lut_g.get(green);
				int blue_value = lut_b.get(blue);

				pixel = (alpha << 24) | (red_value << 16) | (green_value << 8) | blue_value; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Histogram specification done.");
		return img;
	}

	static Map<Integer, Integer> makeLut(Map<Integer, Double> reference, Map<Integer, Double> original) {
		Map<Integer, Integer> res = new TreeMap<Integer, Integer>();
		for (Integer algo : original.keySet()) {
			int i = 0;
			while (original.get(algo) > reference.get(i)) {
				i++;
			}
			if (nearestValue(original.get(algo), reference.getOrDefault(i - 1, 0d), reference.get(i)) == reference
					.get(i)) {
				res.put(algo, i);
			} else
				res.put(algo, i - 1);
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
