import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImgLinealTransform {

	static public BufferedImage LinealTransform(BufferedImage image, ArrayList<Point> points) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		if(!(points.get(0).x != 0))points.add(0, new Point(0, 0));
		if(!(points.get(points.size()).x != 255))points.add(new Point(255, 255));

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				int red_value = traduceValue(red, points);
				int green_value = traduceValue(green, points);
				int blue_value = traduceValue(blue, points);
				
				pixel = (alpha << 24) 
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
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				
				int pixel = image.getRGB(j, i);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				int red_value = (int) Math.floor(img_hist.norm_colores_acc.get(0).getOrDefault(red, (double) 0) * 255);
				int green_value = (int) Math.floor(img_hist.norm_colores_acc.get(1).getOrDefault(green, (double) 0) * 255);
				int blue_value = (int) Math.floor(img_hist.norm_colores_acc.get(2).getOrDefault(blue, (double) 0) * 255);
				
				pixel = (alpha << 24) 
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
		for(int i = 0; i <= spec.size(); i++) {
			if(spec.get(i).x > point) {
				pendiente = (spec.get(i).y - spec.get(i-1).y) / (spec.get(i).x - spec.get(i-1).x);
				offset = spec.get(i-1).y;
				break;
			}
		}
		return Math.min(pendiente*point + offset, 255);		
	}
}
