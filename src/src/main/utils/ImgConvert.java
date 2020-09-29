package main.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImgConvert {

	public static List<Pixel> toPixelArrayList(BufferedImage image) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				pixels.add(new Pixel(new Float[] { (float) alpha, (float) red, (float) green, (float) blue }));
			}
		}
		return pixels;
	}

	public static Integer[][] toIntArray(BufferedImage image) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		Integer[][] array = new Integer[image.getHeight()][image.getWidth()];

		// Utility.imgApply(image, (i, j) ->
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				array[i][j] = image.getRGB(j, i);
			}
		}
		// );
		return array;
	}

	public static BufferedImage toBuffImg(List<Pixel> pixelList, int width, int height) {

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		int linea = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = (pixelList.get(linea).alpha() << 24) | (pixelList.get(linea).red() << 16)
						| (pixelList.get(linea).green() << 8) | pixelList.get(linea++).blue(); // pixel

				img.setRGB(j, i, pixel);
			}
		}

		return img;
	}
}
