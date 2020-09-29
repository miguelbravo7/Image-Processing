package main.filters.point.kmeans;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.ImgConvert;
import main.utils.Pixel;

public class Kmeans_Processor {
	private static final Logger LOGGER = Logger.getLogger(Kmeans_Processor.class.getName());

	public static BufferedImage renderkmeans(BufferedImage image, int k) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		ArrayList<Pixel> orderedPixels = new ArrayList<Pixel>(ImgConvert.toPixelArrayList(image));
		ArrayList<Pixel> pixelList = new ArrayList<Pixel>();

		LOGGER.log(Level.FINE, "Searching centroids.");
		KMeans kmeans = new KMeans();
		KMeansResultado resultado = kmeans.calcular(orderedPixels, k);

		pixelList.ensureCapacity(image.getWidth() * image.getHeight());

		LOGGER.log(Level.FINE, "Assigning centroids.");
		for (int i = 0; i < image.getWidth() * image.getHeight(); i++) {
			pixelList.add(resultado.getCentroide(orderedPixels.get(i)));
		}

		LOGGER.log(Level.FINE, " Width:{0} Height:{1}\nCENTROIDES:", new Object[] { image.getWidth(), image.getHeight() });
		for (Cluster p : resultado.getClusters()) {
			LOGGER.log(Level.FINE, p.getCentroide().toString());
		}
		LOGGER.log(Level.FINE, "{0} K-means done.", k);

		return ImgConvert.toBuffImg(pixelList, image.getWidth(), image.getHeight());
	}
}
