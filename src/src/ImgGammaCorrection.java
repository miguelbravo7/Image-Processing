import java.awt.image.BufferedImage;

public class ImgGammaCorrection {

	static public BufferedImage gammaCorrection(BufferedImage image, int gamma) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				double red_value = Math.pow(red / 255, gamma);
				double green_value = Math.pow(green / 255, gamma);
				double blue_value = Math.pow(blue / 255, gamma);
				
				pixel = (alpha << 24) 
						| ((int)(red_value*255) << 16) 
						| ((int)(green_value*255) << 8)
						| (int)(blue_value*255) ; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Gamma correction done.");
		return img;
	}

}
