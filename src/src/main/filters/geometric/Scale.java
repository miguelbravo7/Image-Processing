package main.filters.geometric;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.ImgConvert;
import main.utils.Utility;

public class Scale {
	private static final Logger LOGGER = Logger.getLogger(Scale.class.getName());

	private Scale() {
		throw new IllegalStateException("Utility class");
	}

	public static BufferedImage scale(BufferedImage image, double width, double height, String function) {
		int cappedWidth = (int) Math.floor(image.getWidth() * width);
		int cappedHeight = (int) Math.floor(image.getHeight() * height);
		Integer[][] array = ImgConvert.toIntArray(image);

		BufferedImage img = new BufferedImage(cappedWidth, cappedHeight, image.getType());

		for (int i = 0; i < cappedHeight; i++) {
			for (int j = 0; j < cappedWidth; j++) {
				try {
					img.setRGB(j, i, (int) Utility.methodMap.get(function).invoke(new Object(), array, j / width,
							i / height, image.getWidth(), image.getHeight()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		LOGGER.log(Level.FINE, "{0} scale done.", function);
		return img;
	}
}
