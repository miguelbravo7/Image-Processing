package main.filters.point;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.Utility;

public class Monochrome {
	private static final Logger LOGGER = Logger.getLogger(Monochrome.class.getName());

	public static BufferedImage renderPal(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Utility.imgApply(image, (i, j) -> {

			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			int greyVal = Math.round(red * 0.222f + green * 0.707f + blue * 0.071f);

			pixel = (alpha << 24) | (greyVal << 16) | (greyVal << 8) | greyVal;

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "ImgMonochrome PAL done.");
		return img;
	}
}
