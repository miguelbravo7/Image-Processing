import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImgScalar {
	static public BufferedImage render(BufferedImage image, float percr, float percg, float percb) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Instanciacion de los puntos a partir de los valores de la imagen
		List<Integer[]> pixels_ordered = new ArrayList<Integer[]>();

		int w = image.getWidth();
		int h = image.getHeight();


		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				pixels_ordered.add(new Integer[] { alpha, (int) (red*percr), (int)(green*percg), (int)(blue*percb) });
			}
		}
		
		int linea = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {

				int pixel = (pixels_ordered.get(linea)[0] << 24) 
						| (pixels_ordered.get(linea)[1] << 16) 
						| (pixels_ordered.get(linea)[2] << 8)
						| pixels_ordered.get(linea++)[3]; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		return img;
	}
}
