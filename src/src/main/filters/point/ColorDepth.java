package main.filters.point;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.ImgConvert;
import main.utils.Utility;

public class ColorDepth {
	private static final Logger LOGGER = Logger.getLogger(ColorDepth.class.getName());

	public static BufferedImage colorDepthShift(BufferedImage image, int depth) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			int calc = 8 - depth;

			pixel = (alpha << 24) | (((red >> calc) << calc) << 16) | (((green >> calc) << calc) << 8)
					| ((blue >> calc) << calc);

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "Color depth by shift done.");
		return img;
	}

	public static BufferedImage colorDepthRegion(BufferedImage image, int region) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Integer[][] array = ImgConvert.toIntArray(image);
		LOGGER.log(Level.FINE, Integer.toString(array.length));
		LOGGER.log(Level.FINE, Integer.toString(array[0].length));

		for (int i = 0; i < image.getHeight(); i += region) {
			for (int j = 0; j < image.getWidth(); j += region) {
				int aAcc = 0;
				int rAcc = 0;
				int gAcc = 0;
				int bAcc = 0;
				int nCells = 0;

				for (int h_gap = 0; h_gap < region && i + h_gap < image.getHeight(); h_gap++) {
					for (int w_gap = 0; w_gap < region && j + w_gap < image.getWidth(); w_gap++) {
						aAcc += (array[i + h_gap][j + w_gap] >> 24) & 0xff;
						rAcc += (array[i + h_gap][j + w_gap] >> 16) & 0xff;
						gAcc += (array[i + h_gap][j + w_gap] >> 8) & 0xff;
						bAcc += (array[i + h_gap][j + w_gap]) & 0xff;
						nCells++;
					}
				}

				int pixel = ((aAcc / nCells) << 24) | ((rAcc / nCells) << 16) | ((gAcc / nCells) << 8)
						| (bAcc / nCells);

				for (int h_gap = 0; h_gap < region && i + h_gap < image.getHeight(); h_gap++)
					for (int w_gap = 0; w_gap < region && j + w_gap < image.getWidth(); w_gap++)
						img.setRGB(j + w_gap, i + h_gap, pixel);
			}
		}
		LOGGER.log(Level.FINE, "Color depth by region done.");
		return img;
	}
}
