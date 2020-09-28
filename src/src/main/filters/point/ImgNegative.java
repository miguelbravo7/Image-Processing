package main.filters.point;

import java.awt.image.BufferedImage;

public class ImgNegative {

	static public BufferedImage render(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				pixel = (alpha << 24) | (255 - red << 16) | (255 - green << 8) | 255 - blue; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("ImgNegative done.");
		return img;
	}
}
