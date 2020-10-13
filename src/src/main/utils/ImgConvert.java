package main.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImgConvert {

	private ImgConvert() {
		throw new IllegalStateException("Utility class");
	}

	public static List<Pixel<Integer>> toPixelArrayList(BufferedImage image) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		ArrayList<Pixel<Integer>> pixels = new ArrayList<>(image.getWidth() * image.getHeight());

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				pixels.add(new Pixel<>(new Integer[] { alpha, red, green, blue }));
			}
		}
		return pixels;
	}

	public static Integer[][] toIntArray(BufferedImage image) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		Integer[][] array = new Integer[image.getHeight()][image.getWidth()];

		Utility.imgApply(image, (i, j) -> array[i][j] = image.getRGB(j, i));
		return array;
	}

	public static BufferedImage toBuffImg(List<Pixel<?>> pixelList, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		Utility.imgApply(img, (i, j) -> {
			int pixelOffset = i * height + j;
			int pixel = pixelList.get(pixelOffset).getRGB();

			img.setRGB(j, i, pixel);
		});

		return img;
	}
}
