package main.filters.point;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.Utility;

public class GammaCorrection {
	private static final Logger LOGGER = Logger.getLogger(GammaCorrection.class.getName());

	public static BufferedImage gammaCorrection(BufferedImage image, double gamma) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			double redValue = Math.pow(red / 255d, gamma);
			double greenValue = Math.pow(green / 255d, gamma);
			double blueValue = Math.pow(blue / 255d, gamma);

			pixel = (alpha << 24) | ((int) (redValue * 255) << 16) | ((int) (greenValue * 255) << 8)
					| (int) (blueValue * 255); // pixel

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "Gamma correction done.");
		return img;
	}

}
