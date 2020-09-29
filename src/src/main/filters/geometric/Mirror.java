package main.filters.geometric;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.Utility;

public class Mirror {
	private static final Logger LOGGER = Logger.getLogger(Mirror.class.getName());

	public static BufferedImage vertical(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			img.setRGB(j, image.getHeight() - i - 1, pixel);
		});
		LOGGER.log(Level.FINE, "Vertical mirror done.");
		return img;
	}

	public static BufferedImage horizontal(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			img.setRGB(image.getWidth() - j - 1, i, pixel);
		});
		LOGGER.log(Level.FINE, "horizontal mirror done.");
		return img;
	}

}
