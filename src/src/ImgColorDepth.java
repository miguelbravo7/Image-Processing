import java.awt.image.BufferedImage;

public class ImgColorDepth {

	static public BufferedImage colorDepth(BufferedImage image, int depth) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				int calc = 8 - depth;
				
				pixel = (alpha << 24) 
						| ((int)((red >> calc) << calc) << 16) 
						| ((int)((green >> calc) << calc) << 8)
						| (int)((blue >> calc) << calc);

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Color depth done.");
		return img;
	}
}
