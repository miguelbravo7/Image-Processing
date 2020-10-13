package main.filters.point.kmeans;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.utils.ImgConvert;
import main.utils.Pixel;

public class KmeansProcessor {
	private static final Logger LOGGER = Logger.getLogger(KmeansProcessor.class.getName());

	private KmeansProcessor() {
		throw new IllegalStateException("Utility class");
	}

	public static BufferedImage renderkmeans(BufferedImage image, int k) {
		// Instanciacion de los puntos a partir de los valores de la imagen
		BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		List<Pixel<Integer>> originalPixels = ImgConvert.toPixelArrayList(image);
		KMeans<Integer, Float> kmeans = new KMeans<>();

		LOGGER.log(Level.FINE, "Searching centroids.");
		KMeansResultado<Integer, Float> resultado = kmeans.calcular(originalPixels, k);

		LOGGER.log(Level.FINE, "Assigning centroids.");
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				Pixel<Float> p = resultado.getCentroide(originalPixels.get(j + i * image.getWidth()));
				res.setRGB(j, i, p.getRGB());
			}
		}

		LOGGER.log(Level.FINE, " Width:{0} Height:{1}\nCENTROIDES:", new Object[] { image.getWidth(), image.getHeight() });
		for (Cluster<Integer, Float> p : resultado.getClusters()) {
			LOGGER.log(Level.FINE, "{0}", p.getCentroide());
		}
		LOGGER.log(Level.FINE, "{0} K-means done.", k);

		return res;
	}
}
