import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImgConvert {

	static ArrayList<Pixel> toPixelArrayList(BufferedImage image){
		// Instanciacion de los puntos a partir de los valores de la imagen
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				pixels.add(new Pixel(new Float[] {(float) alpha, (float) red, (float) green, (float) blue}));
			}
		}
		return pixels;
	}

	static Integer[][] toIntArray(BufferedImage image){
		// Instanciacion de los puntos a partir de los valores de la imagen
		Integer[][] array = new Integer[image.getHeight()][image.getWidth()];

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				array[i][j] = image.getRGB(j, i);
			}
		}
		return array;
	}
	
	static BufferedImage toBuffImg(ArrayList<Pixel> pixel_list, int width, int height) {

		BufferedImage img =  new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		int linea = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = ((int)pixel_list.get(linea).alpha() << 24) 
						| ((int)pixel_list.get(linea).red() << 16) 
						| ((int)pixel_list.get(linea).green() << 8)
						| (int)pixel_list.get(linea++).blue(); // pixel

				img.setRGB(j, i, pixel);
			}
		}

		return img;
	}
}
