package main.filters.point;

import java.awt.image.BufferedImage;

import main.utils.ImgConvert;

public class ImgColorDepth {

	static public BufferedImage colorDepthShift(BufferedImage image, int depth) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				int calc = 8 - depth;

				pixel = (alpha << 24) | ((int) ((red >> calc) << calc) << 16) | ((int) ((green >> calc) << calc) << 8)
						| (int) ((blue >> calc) << calc);

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Color depth by shift done.");
		return img;
	}

	static public BufferedImage colorDepthRegion(BufferedImage image, int region) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Integer[][] array = ImgConvert.toIntArray(image);
		System.out.println(array.length);
		System.out.println(array[0].length);

		for (int i = 0; i < image.getHeight(); i += region) {
			for (int j = 0; j < image.getWidth(); j += region) {
				int acc_a = 0, acc_r = 0, acc_g = 0, acc_b = 0, n_cells = 0;

				for (int h_gap = 0; h_gap < region && i + h_gap < image.getHeight(); h_gap++) {
					for (int w_gap = 0; w_gap < region && j + w_gap < image.getWidth(); w_gap++) {
						acc_a += (array[i + h_gap][j + w_gap] >> 24) & 0xff;
						acc_r += (array[i + h_gap][j + w_gap] >> 16) & 0xff;
						acc_g += (array[i + h_gap][j + w_gap] >> 8) & 0xff;
						acc_b += (array[i + h_gap][j + w_gap]) & 0xff;
						n_cells++;
					}
				}

				int pixel = ((acc_a / n_cells) << 24) | ((acc_r / n_cells) << 16) | ((acc_g / n_cells) << 8)
						| (acc_b / n_cells);

				for (int h_gap = 0; h_gap < region && i + h_gap < image.getHeight(); h_gap++)
					for (int w_gap = 0; w_gap < region && j + w_gap < image.getWidth(); w_gap++)
						img.setRGB(j + w_gap, i + h_gap, pixel);
			}
		}
		System.out.println("Color depth by region done.");
		return img;
	}
}
