import java.awt.image.BufferedImage;
import java.util.List;

public class ImgMonochrome {
	
	static public BufferedImage renderPal(BufferedImage image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Instanciacion de los puntos a partir de los valores de la imagen
		List<Pixel> pixels = ImgConvert.toPixelArrayList(image);
		
		int linea = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int grey_value = (int) (pixels.get(linea).red()*0.222 + pixels.get(linea).green()*0.707 + pixels.get(linea).blue()*0.071);
				
				int pixel = (pixels.get(linea++).alpha() << 24) 
						| (grey_value << 16) 
						| (grey_value << 8)
						| grey_value ; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("ImgMonochrome PAL done.");
		return img;
	}
}
