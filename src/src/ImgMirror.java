import java.awt.image.BufferedImage;

public class ImgMirror {

	static public BufferedImage vertical(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				img.setRGB(j, image.getHeight() - i - 1, pixel);
			}
		}
		System.out.println("Vertical mirror done.");
		return img;
	}
	
	static public BufferedImage horizontal(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int pixel = image.getRGB(j, i);

				img.setRGB(image.getWidth() - j - 1, i, pixel);
			}
		}
		System.out.println("horizontal mirror done.");
		return img;
	}

}
