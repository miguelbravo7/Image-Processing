import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Kmeans_Processor {

	static public BufferedImage renderkmeans(BufferedImage image, int k) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		List<Pixel> pixels_ordered = new ArrayList<Pixel>(ImgConvert.toPixelArrayList(image));
		ArrayList<Pixel> pixel_list = new ArrayList<Pixel>();		
		
		System.out.println("Searching centroids.");
		KMeans kmeans = new KMeans();
		KMeansResultado resultado = kmeans.calcular(pixels_ordered, k);
		
		pixel_list.ensureCapacity(image.getWidth()*image.getHeight());
		
		System.out.println("Assigning centroids.");
		for (int i = 0; i < image.getWidth()*image.getHeight(); i++) {
			pixel_list.add(resultado.getCentroide(pixels_ordered.get(i)));	
		}

//		System.out.println(" Width:" + image.getWidth()+" Height:"+image.getHeight() +"\nCENTROIDES:");
//		for (Cluster p : resultado.getClusters()) {
//			System.out.println(p.getCentroide());
//		}
		System.out.println(k +" K-means done.");
		
		return ImgConvert.toBuffImg(pixel_list, image.getWidth(), image.getHeight());
	}
}
