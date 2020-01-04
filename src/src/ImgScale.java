import java.awt.image.BufferedImage;

public class ImgScale {

	static public BufferedImage scaleNeighbour(BufferedImage image, double width, double height) {
		int n_width = (int) Math.floor(image.getWidth() * width);
		int n_height = (int) Math.floor(image.getHeight() * height);
		Integer[][] array = ImgConvert.toIntArray(image);
		
		BufferedImage img = new BufferedImage(n_width, n_height, image.getType());
		
		for (int i = 0; i < n_height; i++) {
			for (int j = 0; j < n_width; j++) {

				int h = (int) Math.round(i/height) == image.getHeight() ? image.getHeight()-1 : (int) Math.round(i/height);
				int w = (int) Math.round(j/width) == image.getWidth() ? image.getWidth()-1 : (int) Math.round(j/width);
				img.setRGB(j, i, array[h][w]);
			}
		}
		System.out.println("Neighbour scale done.");
		return img;
	}

	static public BufferedImage scaleBilinear(BufferedImage image, double width, double height) {
		int n_width = (int) Math.floor(image.getWidth() * width);
		int n_height = (int) Math.floor(image.getHeight() * height);
		Integer[][] array = ImgConvert.toIntArray(image);

		BufferedImage img = new BufferedImage(n_width, n_height, image.getType());
		
		for (int i = 0; i < n_height; i++) {
			for (int j = 0; j < n_width; j++) {
				int X = (int) Math.floor(j/width);
				int Y = (int) Math.floor(i/height);
				int X_1 = (X + 1 == image.getWidth() ? image.getWidth() - 1 : X + 1);
				int Y_1 = (Y + 1 == image.getHeight() ? image.getHeight() - 1 : Y + 1);

				int pixel1 = array[Y][X];
				int pixel2 = array[Y][X_1];
				int pixel3 = array[Y_1][X];
				int pixel4 = array[Y_1][X_1];

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
				
				double p = (j / width) % 1;
				double q = (i / height) % 1;
				System.out.println(p);
				

				int pixel = (alpha << 24) 
						| (bilinearInterpolation(red1, red2, red3, red4, p, q) << 16) 
						| (bilinearInterpolation(green1, green2, green3, green4, p, q) << 8)
						| (bilinearInterpolation(blue1, blue2, blue3, blue4, p, q));


				img.setRGB(j, i, pixel);
			}
		}
		System.out.println("Bilinear scale done.");
		return img;
	}
	
	public static int bilinearInterpolation(int A, int B, int C, int D, double x_position, double y_position) {
		double R = A + x_position * (B - A);
		double S = C + x_position * (D - C);
		
		int color = (int) (R + y_position * (S - R));
		
		
		return color;		
	}
}
