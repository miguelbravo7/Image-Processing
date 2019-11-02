import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImgLinealTransform {

	static public BufferedImage LinealTransform(BufferedImage image, ArrayList<Point> points) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		// Instanciacion de los puntos a partir de los valores de la imagen
		List<Pixel> pixels = ImgConvert.toPixelArrayList(image);
		
		int linea = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int red_value = traduceValue(pixels.get(linea).red(), points);
				int green_value = traduceValue(pixels.get(linea).green(), points);
				int blue_value = traduceValue(pixels.get(linea).blue(), points);
				
				int pixel = (pixels.get(linea++).alpha() << 24) 
						| (red_value << 16) 
						| (green_value << 8)
						| blue_value; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Lineal transform done.");
		return img;
	}

	static public BufferedImage ecualize(BufferedImage image, Histogram img_hist) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		// Instanciacion de los puntos a partir de los valores de la imagen
		List<Pixel> pixels = ImgConvert.toPixelArrayList(image);
		
		int linea = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int red_value = (int) Math.floor(img_hist.norm_colores_acc.get(0).getOrDefault(pixels.get(linea).red(), (double) 0) * 255);
				int green_value = (int) Math.floor(img_hist.norm_colores_acc.get(1).getOrDefault(pixels.get(linea).green(), (double) 0) * 255);
				int blue_value = (int) Math.floor(img_hist.norm_colores_acc.get(2).getOrDefault(pixels.get(linea).blue(), (double) 0) * 255);
				
				int pixel = (pixels.get(linea++).alpha() << 24) 
						| (red_value << 16) 
						| (green_value << 8)
						| blue_value; // pixel

				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Ecualization done.");
		return img;
	}

	static private int traduceValue(int point, ArrayList<Point> spec) {
		int pendiente = 0, offset = 0;
		for(int i = 0; i<spec.size();i++) {
			if(spec.get(i).x > point) {
				pendiente = (spec.get(i).y - spec.get(i-1).y) / (spec.get(i).x - spec.get(i-1).x);
				offset = spec.get(i-1).y;
				break;
			}
		}
		return Math.min(pendiente*point + offset, 255);		
	}
}
