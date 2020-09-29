package main.filters.geometric;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FixedRotation {
	private static final Logger LOGGER = Logger.getLogger(FixedRotation.class.getName());

	public static BufferedImage rotate90(BufferedImage image) {
		LOGGER.log(Level.FINE, "90� rotation done.");
		return Mirror.vertical(Transpose.transpose(image));
	}

	public static BufferedImage rotate180(BufferedImage image) {
		LOGGER.log(Level.FINE, "180� rotation done.");
		return Mirror.vertical(Mirror.horizontal(image));
	}
	
	public static BufferedImage rotate270(BufferedImage image) {
		LOGGER.log(Level.FINE, "270� rotation done.");
		return Mirror.horizontal(Transpose.transpose(image));
	}
}
