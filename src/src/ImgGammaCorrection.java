import java.awt.image.BufferedImage;
import java.util.List;

public class ImgGammaCorrection {

	static public BufferedImage gammaCorrection(BufferedImage image, int gamma) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		// Instanciacion de los puntos a partir de los valores de la imagen
		List<Pixel> pixels = ImgConvert.toPixelArrayList(image);
		
		int linea = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				double red_value = Math.pow(pixels.get(linea).red() / 255, gamma);
				double green_value = Math.pow(pixels.get(linea).green() / 255, gamma);
				double blue_value = Math.pow(pixels.get(linea).blue() / 255, gamma);
				
				int pixel = (pixels.get(linea++).alpha() << 24) 
						| ((int)(red_value*255) << 16) 
						| ((int)(green_value*255) << 8)
						| (int)(blue_value*255) ; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Gamma correction done.");
		return img;
	}

}
