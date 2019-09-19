import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Kmeans_Processor {

	static public BufferedImage renderkmeans(BufferedImage image, int k) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Instanciacion de los puntos a partir de los valores de la imagen
		Set<Pixel> pixels = new HashSet<Pixel>();
		List<Pixel> pixels_ordered = new ArrayList<Pixel>();

		int width = image.getWidth();
		int height = image.getHeight();


		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				Pixel p = new Pixel(new Float[] { (float) alpha, (float) red, (float) green, (float) blue });
				pixels.add(p);
				pixels_ordered.add(p);
			}
		}
		
		KMeans kmeans = new KMeans();
		ArrayList<Pixel> pixel_list = new ArrayList<Pixel>();
		pixel_list.addAll(pixels);
		KMeansResultado resultado = kmeans.calcular(pixel_list, k);
		
		for (int i = 0; i < width*height; i++) {
			pixel_list.add(resultado.getCentroide(pixels_ordered.get(i++)));			
		}

		ImgConvert.toBuffImg(pixel_list, width, height);
//		System.out.println(" Width:" + img.getWidth()+" Height:"+img.getHeight() +"\nCENTROIDES:");
//		for (Cluster p : resultado.getClusters()) {
//			System.out.println(p.getCentroide());
//		}
		return img;
	}
}
