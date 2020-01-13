import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Utility {
	static Map<String, Method> methodMap = createMap();
	
	private static Map<String, Method> createMap() {
	    Map<String, Method> myMap = new HashMap<String, Method>();
	    try {
			myMap.put("Bilinear", Utility.class.getMethod("bilinearInterpolation", Integer[][].class, double.class, double.class, int.class, int.class));
		    myMap.put("Neighbour", Utility.class.getMethod("nearestNeighbour", Integer[][].class, double.class, double.class, int.class, int.class));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	    return myMap;
	}
	
	public static void getAllFiles(File curDir, String level) {
		File[] filesList = curDir.listFiles();
		
		for (File f : filesList) {
			if (f.isDirectory()) {
				System.out.println(f.getName());
				getAllFiles(f, level+" | ");
			}
			if (f.isFile()) {
				System.out.println(level + f.getName());
			}
		}
	}
	
	public static int bilinearInterpolation(Integer[][] color, double x_position, double y_position, int width, int height) {
		int X = (int) Math.floor(x_position);
		int Y = (int) Math.floor(y_position);
		int X_1 = (X + 1 == width ? width - 1 : X + 1);
		int Y_1 = (Y + 1 == height ? height - 1 : Y + 1);
		
		int pixel1 = color[Y][X];
		int pixel2 = color[Y][X_1];
		int pixel3 = color[Y_1][X];
		int pixel4 = color[Y_1][X_1];

		int alpha = (pixel1 >> 24) & 0xff;
		int red1 = (pixel1 >> 16) & 0xff;
		int green1 = (pixel1 >> 8) & 0xff;
		int blue1 = (pixel1) & 0xff;


		int red2 = (pixel2 >> 16) & 0xff;
		int green2 = (pixel2 >> 8) & 0xff;
		int blue2 = (pixel2) & 0xff;


		int red3 = (pixel3 >> 16) & 0xff;
		int green3 = (pixel3 >> 8) & 0xff;
		int blue3 = (pixel3) & 0xff;


		int red4 = (pixel4 >> 16) & 0xff;
		int green4 = (pixel4 >> 8) & 0xff;
		int blue4 = (pixel4) & 0xff;
		
		double p = x_position % 1;
		double q = y_position % 1;
				
		int pixel_color = (alpha << 24) 
				| (interpolate(red1, red2, red3, red4, p, q) << 16) 
				| (interpolate(green1, green2, green3, green4, p, q) << 8)
				| (interpolate(blue1, blue2, blue3, blue4, p, q));
		
		
		return pixel_color;		
	}
	
	private static int interpolate(int A, int B, int C, int D, double p, double q) {

		double R = A + p * (B - A);
		double S = C + p * (D - C);
		
		int color = (int) (R + q * (S - R));
		
		return color;
	}
	
	public static int nearestNeighbour(Integer[][] color, double x_position, double y_position, int width, int height) {
		int w =  Math.round(x_position) == width ? width-1 : (int) Math.round(x_position);
		int h =  Math.round(y_position) == height ? height-1 : (int) Math.round(y_position);
		
		return color[h][w];		
	}
		
	public static class Pair<L,R> {
	  public L x;
	  public R y;

	  public Pair(L left, R right) {
	    assert left != null;
	    assert right != null;

	    this.x = left;
	    this.y = right;
	  }

	  public L getLeft() { return x; }
	  public R getRight() { return y; }

	  @Override
	  public int hashCode() { return x.hashCode() ^ y.hashCode(); }

	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof Pair)) return false;
	    Pair pairo = (Pair) o;
	    return this.x.equals(pairo.getLeft()) && this.y.equals(pairo.getRight());
	  }

	}
}
