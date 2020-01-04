import java.awt.Point;
import java.awt.image.BufferedImage;

public class ImgRotation {

	static public BufferedImage rotate(BufferedImage image, Double degrees) {
		degrees %= 360; 
		
		int min_x, min_y, max_x, max_y;
		
		int alpha = 0 & 0xff;
		int red = 0 & 0xff;
		int green = 0 & 0xff;
		int blue = 0 & 0xff;


		int def = (alpha << 24) 
				| (red << 16) 
				| (green << 8)
				|  blue;
		
		Point poo = new Point(0, 0);
		Point pxo = translatePoint(new Point(image.getWidth(), 0), degrees);
		Point poy = translatePoint(new Point(0, image.getHeight()), degrees);
		Point pxy = translatePoint(new Point(image.getWidth(), image.getHeight()), degrees);
		
		min_x = Math.min(poo.x, Math.min(pxo.x, Math.min(poy.x, pxy.x)));
		min_y = Math.min(poo.y, Math.min(pxo.y, Math.min(poy.y, pxy.y)));
		max_x = Math.max(poo.x, Math.max(pxo.x, Math.max(poy.x, pxy.x)));
		max_y = Math.max(poo.y, Math.max(pxo.y, Math.max(poy.y, pxy.y)));
		
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
				Point og = translatePoint(new Point(j + min_x, i + min_y), - degrees);

				if(og.y >= 0 && og.y < image.getHeight() && og.x >= 0 && og.x < image.getWidth()) {
					img.setRGB(j, i, array[og.y][og.x]);					
				}else {
					img.setRGB(j, i, def);
				}
			}
		}
		
		System.out.println("rotation done.");
		return img;
	}
	
	static Point translatePoint(Point p, double rotation) {
		return new Point((int) (p.x * Math.cos(rotation) - p.y * Math.sin(rotation)),
				(int) (p.x * Math.sin(rotation) + p.y * Math.cos(rotation)));		
	}

}
