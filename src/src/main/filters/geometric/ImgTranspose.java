package main.filters.geometric;

import java.awt.image.BufferedImage;

public class ImgTranspose {

	static public BufferedImage transpose(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				img.setRGB(i, j, pixel);
			}
		}
		System.out.println("Image transpose done.");
		return img;
	}

}
