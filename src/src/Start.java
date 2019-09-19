import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Start {
	final static String format = "png";
	final static String filename = "Windows_XP";

	public static void main(String[] args) throws IOException {
		// Creacion de archivos de lectura y escrtura
		// get the BufferedImage, using the ImageIO class
		BufferedImage image = ImageIO.read(new File(filename+"." + format));
		
		Histogram histograma = new Histogram(ImgConvert.toPixelArrayList(image));
		System.out.println(histograma);
		
		//BufferedImage result = Kmeans_Processor.renderkmeans(image, 4);
//		BufferedImage result = ImgScalar.render(image,0.222f,0.707f,0.071f);
//
//		File file = null;
//		try {
//			file = new File(filename + "_result."+ format);
//			ImageIO.write(result, format, file);
//		} catch (IOException e) {
//			System.out.println("Error: " + e);
//		}

		System.out.println("Terminado.");
	}
}