package main.utils;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Utility {
	private static final Logger LOGGER = Logger.getLogger( Utility.class.getName() );
	public static final Map<String, Method> methodMap = createMap();

	private static Map<String, Method> createMap() {
		Map<String, Method> myMap = new HashMap<String, Method>();
		try {
			myMap.put("Bilinear", Utility.class.getMethod("bilinearInterpolation", Integer[][].class, double.class,
					double.class, int.class, int.class));
			myMap.put("Neighbour", Utility.class.getMethod("nearestNeighbour", Integer[][].class, double.class,
					double.class, int.class, int.class));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return myMap;
	}

	public static void getAllFiles(File curDir, String level) {
		File[] filesList = curDir.listFiles();

		for (File f : filesList) {
			if (f.isDirectory()) {
				LOGGER.log(Level.FINE, f.getName());
				getAllFiles(f, level + " | ");
			}
			if (f.isFile()) {
				LOGGER.log(Level.FINE, "{0}{1}", new Object[]{level, f.getName()});
			}
		}
	}

	public static void imgApply(BufferedImage image, BiConsumer<Integer, Integer> function) {
		// Seen unstable behaviour when implemented on toPixelArrayList
		IntStream.range(0, image.getHeight()).parallel().forEach(i -> 
			IntStream.range(0, image.getWidth()).parallel().forEach(j ->
				function.accept(i, j)
			)
		);
	}

	public static int bilinearInterpolation(Integer[][] color, double xPosition, double yPosition, int width,
			int height) {
		int x = (int) Math.floor(xPosition);
		int y = (int) Math.floor(yPosition);
		int x1 = (x + 1 == width ? width - 1 : x + 1);
		int y1 = (y + 1 == height ? height - 1 : y + 1);

		int pixel1 = color[y][x];
		int pixel2 = color[y][x1];
		int pixel3 = color[y1][x];
		int pixel4 = color[y1][x1];

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

		double p = xPosition % 1;
		double q = yPosition % 1;

		return (alpha << 24) | (interpolate(red1, red2, red3, red4, p, q) << 16)
				| (interpolate(green1, green2, green3, green4, p, q) << 8)
				| (interpolate(blue1, blue2, blue3, blue4, p, q));
	}

	public static int interpolate(int a, int b, int c, int d, double p, double q) {
		double r = a + p * (b - a);
		double s = c + p * (d - c);

		return (int) (r + q * (s - r));
	}

	public static int nearestNeighbour(Integer[][] color, double xPosition, double yPosition, int width, int height) {
		if(xPosition < 0 || yPosition < 0){
			System.out.println(xPosition);
			System.out.println(yPosition);
		}
		int w = Math.round(xPosition) == width ? width - 1 : (int) Math.round(xPosition);
		int h = Math.round(yPosition) == height ? height - 1 : (int) Math.round(yPosition);

		return color[h][w];
	}

	public static float getHue(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		float min = Math.min(Math.min(red, green), blue);
		float max = Math.max(Math.max(red, green), blue);

		if (min == max) {
			return 0f;
		}

		float hue;
		if (max == red) {
			hue = (green - blue) / (max - min);

		} else if (max == green) {
			hue = 2f + (blue - red) / (max - min);

		} else {
			hue = 4f + (red - green) / (max - min);
		}

		hue = hue * 60;
		if (hue < 0)
			hue = hue + 360;

		return hue;
	}

	public static class Pair<L, R> {
		public L x;
		public R y;

		public Pair(L left, R right) {
			assert left != null;
			assert right != null;

			this.x = left;
			this.y = right;
		}

		public L getLeft() {
			return x;
		}

		public R getRight() {
			return y;
		}

		@Override
		public int hashCode() {
			return x.hashCode() ^ y.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Pair))
				return false;
			@SuppressWarnings("unchecked")
			Pair<L, R> pairo = (Pair<L, R>) o;
			return this.x.equals(pairo.getLeft()) && this.y.equals(pairo.getRight());
		}

	}
}
