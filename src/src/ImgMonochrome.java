import java.awt.image.BufferedImage;

public class ImgMonochrome {
	
	static public BufferedImage renderPal(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				
				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				int grey_value = (int) (red*0.222 + green*0.707 + blue*0.071);
				
				pixel = (alpha << 24) 
						| (grey_value << 16) 
						| (grey_value << 8)
						| grey_value ; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("ImgMonochrome PAL done.");
		return img;
	}
}
