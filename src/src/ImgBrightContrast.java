import java.awt.image.BufferedImage;

public class ImgBrightContrast {

	static public BufferedImage adjustImg(BufferedImage image, Histogram img_hist, int new_brightness, int new_contrast) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				
				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				red = (int) (red *(new_contrast / img_hist.dev[0]) + new_brightness - img_hist.med[0] *(new_contrast / img_hist.dev[0]));
				green = (int) (green *(new_contrast / img_hist.dev[1]) + new_brightness - img_hist.med[1] *(new_contrast / img_hist.dev[1]));
				blue = (int) (blue *(new_contrast / img_hist.dev[2]) + new_brightness - img_hist.med[2] *(new_contrast / img_hist.dev[2]));
				
				red = Math.min(red, 255);
				green = Math.min(green, 255);
				blue = Math.min(blue, 255);

				pixel = (alpha << 24) 
						| ( Math.max(red, 0) << 16) 
						| ( Math.max(green, 0) << 8)
						|  Math.max(blue, 0) ; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Bright contrast change done.");
		return img;
	}
}
