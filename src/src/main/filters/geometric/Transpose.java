package main.filters.geometric;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.Utility;

public class Transpose {
	private static final Logger LOGGER = Logger.getLogger(Transpose.class.getName());

	private Transpose() {
		throw new IllegalStateException("Utility class");
	}

	public static BufferedImage transpose(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);
			img.setRGB(i, j, pixel);
		});
		LOGGER.log(Level.FINE, "Image transpose done.");
		return img;
	}
}
