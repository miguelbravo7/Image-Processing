import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImgScalar {
	
	static public BufferedImage render(BufferedImage image, float percr, float percg, float percb) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Instanciacion de los puntos a partir de los valores de la imagen
		List<Pixel> pixels = ImgConvert.toPixelArrayList(image);
		
		int linea = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = (pixels.get(linea).alpha() << 24) 
						| ((int)(pixels.get(linea).red()*percr) << 16) 
						| ((int)(pixels.get(linea).green()*percg) << 8)
						| (int)(pixels.get(linea++).blue()*percb) ; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		return img;
	}
}
