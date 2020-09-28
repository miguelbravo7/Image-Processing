package main.filters.point;
import java.awt.image.BufferedImage;

public class ImgDifference {

	static public BufferedImage difference(BufferedImage image1, BufferedImage image2) {
		BufferedImage img = new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());

		for (int i = 0; i < image1.getHeight(); i++) {
			for (int j = 0; j < image1.getWidth(); j++) {
				
				int pixel1 = image1.getRGB(j, i);
				int pixel2 = image2.getRGB(j, i);


				int alpha1 = (pixel1 >> 24) & 0xff;
				int red1 = (pixel1 >> 16) & 0xff;
				int green1 = (pixel1 >> 8) & 0xff;
				int blue1 = (pixel1) & 0xff;
			

				int red2 = (pixel2 >> 16) & 0xff;
				int green2 = (pixel2 >> 8) & 0xff;
				int blue2 = (pixel2) & 0xff;

				
				pixel1 = (alpha1 << 24) 
						| (Math.abs(red1- red2) << 16) 
						| (Math.abs(green1 - green2) << 8)
						| Math.abs(blue1 - blue2); // pixel

				img.setRGB(j, i, pixel1);
			}
		}
		System.out.println("ImgDifference done.");
		return img;
	}
	
	static public BufferedImage changeUmbral(BufferedImage image, Integer umbral) {
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				
				int pixel = image.getRGB(j, i);


				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
							
				pixel = (alpha << 24) 
						| ((red <= umbral ? red : 255) << 16) 
						| ((red <= umbral ? green : 0) << 8)
						| (red <= umbral ? blue : 0); // pixel

				image.setRGB(j, i, pixel);
			}
		}
		return image;
	}

}
