package main.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImgConvert {

	public static List<Pixel> toPixelArrayList(BufferedImage image) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();

		Utility.imgApply(image, (i, j) -> {
			int pixel = image.getRGB(j, i);

			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

			pixels.add(new Pixel(new Float[] { (float) alpha, (float) red, (float) green, (float) blue }));
		});
		return pixels;
	}

	public static Integer[][] toIntArray(BufferedImage image) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		Integer[][] array = new Integer[image.getHeight()][image.getWidth()];

		Utility.imgApply(image, (i, j) -> array[i][j] = image.getRGB(j, i));
		return array;
	}

	public static BufferedImage toBuffImg(List<Pixel> pixelList, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		Utility.imgApply(img, (i, j) -> {
			int pixelOffset = i * height + j;
			int pixel = (pixelList.get(pixelOffset).alpha() << 24) | (pixelList.get(pixelOffset).red() << 16)
					| (pixelList.get(pixelOffset).green() << 8) | pixelList.get(pixelOffset).blue();

			img.setRGB(j, i, pixel);
		});

		return img;
	}
}
