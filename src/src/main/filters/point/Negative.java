package main.filters.point;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.Utility;

public class Negative {
	private static final Logger LOGGER = Logger.getLogger(Negative.class.getName());

	public static BufferedImage render(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			pixel = (alpha << 24) | (255 - red << 16) | (255 - green << 8) | 255 - blue; // pixel

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "ImgNegative done.");
		return img;
	}
}
