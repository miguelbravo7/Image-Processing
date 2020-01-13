import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public class ImgRotation {
	static public BufferedImage rotate(BufferedImage image, Double degrees, String function) {
		degrees %= 360; 
		
		int min_x, min_y, max_x, max_y;
		
		int alpha = 0 & 0xff;
		int red = 255 & 0xff;
		int green = 255 & 0xff;
		int blue = 255 & 0xff;


		int def = (alpha << 24) 
				| (red << 16) 
				| (green << 8)
				|  blue;
		
		Utility.Pair<Double, Double> poo = new Utility.Pair<Double, Double>(0d, 0d);
		Utility.Pair<Double, Double> pxo = translatePoint(new Point(image.getWidth(), 0), degrees);
		Utility.Pair<Double, Double> poy = translatePoint(new Point(0, image.getHeight()), degrees);
		Utility.Pair<Double, Double> pxy = translatePoint(new Point(image.getWidth(), image.getHeight()), degrees);
		
		min_x = (int) Math.min(poo.x, Math.min(pxo.x, Math.min(poy.x, pxy.x)));
		min_y = (int) Math.min(poo.y, Math.min(pxo.y, Math.min(poy.y, pxy.y)));
		max_x = (int) Math.max(poo.x, Math.max(pxo.x, Math.max(poy.x, pxy.x)));
		max_y = (int) Math.max(poo.y, Math.max(pxo.y, Math.max(poy.y, pxy.y)));
		
		System.out.println(min_x);
		System.out.println(min_y); 
		System.out.println(max_x);
		System.out.println(max_y);
		
		int n_width = max_x - min_x;
		int n_height = max_y - min_y;
		Integer[][] array = ImgConvert.toIntArray(image);
		
		BufferedImage img = new BufferedImage(n_width, n_height, image.getType());
		
		for (int i = 0; i < n_height; i++) {
			for (int j = 0; j < n_width; j++) {
				Utility.Pair<Double, Double> og = translatePoint(new Point(j + min_x, i + min_y), - degrees);

				if(og.y >= 0 && og.y < image.getHeight() && og.x >= 0 && og.x < image.getWidth()) {
					try {
						img.setRGB(j, i,(int) Utility.methodMap.get(function).invoke(new Object(), array, og.x, og.y, image.getWidth(), image.getHeight()));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						break;
					}					
				}else {
					img.setRGB(j, i, def);
				}
			}
		}
		
		System.out.println(function + " rotation done.");
		return img;
	}
	
	static Utility.Pair<Double, Double> translatePoint(Point p, double rotation) {
		double cos = Math.cos(Math.toRadians(rotation));
		double sin = Math.sin(Math.toRadians(rotation));
		return new Utility.Pair<Double, Double>(p.x * cos - p.y * sin,p.x * sin + p.y * cos);		
	}
	
	static public BufferedImage rotateDirect(BufferedImage image, Double degrees, String function) {
		degrees %= 360; 
		
		int min_x, min_y, max_x, max_y;
				
		Utility.Pair<Double, Double> poo = new Utility.Pair<Double, Double>(0d, 0d);
		Utility.Pair<Double, Double> pxo = translatePoint(new Point(image.getWidth(), 0), degrees);
		Utility.Pair<Double, Double> poy = translatePoint(new Point(0, image.getHeight()), degrees);
		Utility.Pair<Double, Double> pxy = translatePoint(new Point(image.getWidth(), image.getHeight()), degrees);
		
		min_x = (int) Math.min(poo.x, Math.min(pxo.x, Math.min(poy.x, pxy.x)));
		min_y = (int) Math.min(poo.y, Math.min(pxo.y, Math.min(poy.y, pxy.y)));
		max_x = (int) Math.max(poo.x, Math.max(pxo.x, Math.max(poy.x, pxy.x)));
		max_y = (int) Math.max(poo.y, Math.max(pxo.y, Math.max(poy.y, pxy.y)));
		
		System.out.println("min x " + min_x);
		System.out.println("min y " + min_y); 
		System.out.println("max x " + max_x);
		System.out.println("max y " + max_y);
		
		int n_width = max_x - min_x;
		int n_height = max_y - min_y;
		Integer[][] array = ImgConvert.toIntArray(image);
		
		BufferedImage img = new BufferedImage(n_width, n_height, image.getType());
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				Utility.Pair<Double, Double> og = translatePoint(new Point(j, i), degrees);
				og.x += Math.abs(min_x);
				og.y += Math.abs(min_y);
				int x_map = (int) Math.floor(og.x) == n_width ? n_width - 1 : (int) Math.floor(og.x);
				int y_map = (int) Math.floor(og.y) == n_height ? n_height - 1 : (int) Math.floor(og.y);
				try {
					img.setRGB(x_map, y_map,
							(int) Utility.methodMap.get(function).invoke(new Object(), array, j + og.x%1, i + og.y%1, image.getWidth(), image.getHeight()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					break;
				}		
			}
		}
		
		System.out.println(function + " direct rotation done.");
		return img;
	}

}
