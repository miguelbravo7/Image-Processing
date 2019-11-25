import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImgLinealTransform {

	static public BufferedImage LinealTransform(BufferedImage image, ArrayList<Point> points) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		
		traducePoints(points);
		for(Point p : points) {
			System.out.println(p);
		}

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

	private static void traducePoints(ArrayList<Point> points) {
		if(points.get(0).x != 0) {		// y = ax + b 
			double pendiente = (points.get(1).y - points.get(0).y) / (points.get(1).x - points.get(0).x);
			int offset = points.get(0).x - points.get(0).y;
			
			// Looking for slope cutting points
			if(pendiente == 0) {
				points.add(0, new Point(0, offset));
			}else if(pendiente > 0) {
				int cut_point = (int) (-offset / pendiente);
				
				points.add(0, new Point(cut_point, 0));
				if(cut_point > 0)
					points.add(0, new Point(0, 0));				
			}else {
				int cut_point = (int) (255-offset / pendiente);
				
				points.add(0, new Point(cut_point, 255));
				if(cut_point > 0)
					points.add(0, new Point(0, 255));
			}
		}
		int last_point = points.size()-1;
		if(points.get(last_point).x != 255) {
			double pendiente = (points.get(last_point).y - points.get(last_point-1).y) / (points.get(last_point).x - points.get(last_point-1).x);
			int offset = points.get(last_point-1).x - points.get(last_point-1).y;
			
			if(pendiente == 0) {
				points.add(new Point(255, offset));
			}else if(pendiente > 0) {
				int cut_point = (int) (255-offset / pendiente);
				
				points.add(new Point(cut_point, 255));
				if(cut_point < 255)
					points.add(new Point(255, 255));				
			}else {
				int cut_point = (int) (255-offset / pendiente);
				
				points.add(new Point(cut_point, 0));
				if(cut_point < 255)
					points.add(new Point(255, 0));
			}
		}
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

	static private int traduceValue(int point, ArrayList<Point> points) {
		double pendiente = 0, offset = 0;
		for(int i = 0; i <= points.size(); i++) {
			if(points.get(i).x > point) {
				pendiente = (points.get(i).y - points.get(i-1).y) / (double)(points.get(i).x - points.get(i-1).x);
				offset = points.get(i-1).x - points.get(i-1).y;
				break;
			}
		}
		return (int) Math.max(Math.min(pendiente * point + offset, 255), 0);		
	}
}
