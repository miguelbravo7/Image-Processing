package main.filters.point;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.graphs.Histogram;
import main.utils.Utility;

public class ImgBrightContrast {
	private static final Logger LOGGER = Logger.getLogger(ImgBrightContrast.class.getName());

	public static BufferedImage adjustImg(BufferedImage image, Histogram imgHist, double newBrightness,
			double newContrast) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			red = (int) (red * (newContrast / imgHist.dev[0]) + newBrightness
					- imgHist.med[0] * (newContrast / imgHist.dev[0]));
			green = (int) (green * (newContrast / imgHist.dev[1]) + newBrightness
					- imgHist.med[1] * (newContrast / imgHist.dev[1]));
			blue = (int) (blue * (newContrast / imgHist.dev[2]) + newBrightness
					- imgHist.med[2] * (newContrast / imgHist.dev[2]));

			red = Math.min(red, 255);
			green = Math.min(green, 255);
			blue = Math.min(blue, 255);

			pixel = (alpha << 24) | (Math.max(red, 0) << 16) | (Math.max(green, 0) << 8) | Math.max(blue, 0); // pixel

			img.setRGB(j, i, pixel);
		});
		LOGGER.log(Level.FINE, "Bright contrast change done.");
		return img;
	}
}
