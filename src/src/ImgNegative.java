import java.awt.image.BufferedImage;
import java.util.List;

public class ImgNegative {

	static public BufferedImage render(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Instanciacion de los puntos a partir de los valores de la imagen
		List<Pixel> pixels = ImgConvert.toPixelArrayList(image);
		
		int linea = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = (pixels.get(linea).alpha() << 24) 
						| (255 - pixels.get(linea).red() << 16) 
						| (255 - pixels.get(linea).green() << 8)
						| 255 - pixels.get(linea++).blue() ; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("ImgNegative done.");
		return img;
	}
}
