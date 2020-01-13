import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public class ImgScale {

	static public BufferedImage scale(BufferedImage image, double width, double height, String function) {
		int n_width = (int) Math.floor(image.getWidth() * width);
		int n_height = (int) Math.floor(image.getHeight() * height);
		Integer[][] array = ImgConvert.toIntArray(image);
		
		BufferedImage img = new BufferedImage(n_width, n_height, image.getType());
		
		for (int i = 0; i < n_height; i++) {
			for (int j = 0; j < n_width; j++) {
				try {
					img.setRGB(j, i, (int) Utility.methodMap.get(function).invoke(new Object(), array, j/width, i/height, image.getWidth(), image.getHeight()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(function + " scale done.");
		return img;
	}
}
