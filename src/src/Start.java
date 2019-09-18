import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

public class Start {
	final static String format = "png";
	final static String filename = "Windows_XP";

	public static void main(String[] args) throws IOException {
		// Creacion de archivos de lectura y escrtura
		// get the BufferedImage, using the ImageIO class
		BufferedImage image = ImageIO.read(new File(filename+"." + format));
		// create buffered image object img
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Instanciacion de los puntos a partir de los valores de la imagen
		Set<Punto> pixels = new HashSet<Punto>();
		List<Punto> pixels_ordered = new ArrayList<Punto>();

		int w = image.getWidth();
		int h = image.getHeight();


		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				Punto p = new Punto(new Float[] { (float) alpha, (float) red, (float) green, (float) blue });
				pixels.add(p);
				pixels_ordered.add(p);
			}
		}

		int linea = 0, k= 4;
		KMeans kmeans = new KMeans();
		List<Punto> pixel_list = new ArrayList<Punto>();
		pixel_list.addAll(pixels);
		KMeansResultado resultado = kmeans.calcular(pixel_list, k);

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				Punto centroide = resultado.getCentroide(pixels_ordered.get(linea++));

				int pixel = ((int)centroide.get(0) << 24) 
						| ((int)centroide.get(1) << 16) 
						| ((int)centroide.get(2) << 8)
						| (int)centroide.get(3); // pixel

				img.setRGB(j, i, pixel);
			}
		}
		
		System.out.println(" Width:" + img.getWidth()+" Height:"+img.getHeight() +"\nCENTROIDES:");
		for (Cluster p : resultado.getClusters()) {
			System.out.println(p.getCentroide());
		}
		

		File f = null;
		try {
			f = new File(filename + k +"_k."+ format);
			ImageIO.write(img, format, f);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

		System.out.println("Terminado.");
	}
}