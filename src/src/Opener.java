import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Opener {
	FileWriter writer = null;
	private String filename;
	private Boolean debug = true;

	public static void main(String[] args) {
		Opener algo = new Opener("test_img.png");
		algo.JavaWalkBufferedImageTest1();

	}

	private Opener(String filename) {
		this.filename = filename;
	}

	public void ficheroToCSV() {
		BufferedReader reader = null;
		FileWriter writer = null;

		try {

			if (debug)
				System.out.println(filename.replaceAll("\\..+", "") + ".csv");

			reader = new BufferedReader(new FileReader(new File(filename)));
			writer = new FileWriter(filename.replaceAll("\\..+", "") + ".csv");
			String text = null;

			int instances = Integer.parseInt(reader.readLine());
			int dimension = Integer.parseInt(reader.readLine());

			for (int i = 0; i < instances; i++) {
				if ((text = reader.readLine()) == null) {
					writer.close();
					throw new IOException("Error en numero de instancias.");
				}
				String valores = "";
				for (String linea : text.split("\\s+", dimension)) {
					valores += Double.parseDouble(linea.replaceAll(",", ".")) + ", ";
				}
				writer.write(valores.substring(0, valores.length() - 2) + "\n");
				// System.out.println(valores.substring(0, valores.length() - 2));
			}
		} catch (FileNotFoundException e) {
			System.out.println(System.getProperty("user.dir"));
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	public void JavaWalkBufferedImageTest1() {
		try {
			// get the BufferedImage, using the ImageIO class
			BufferedImage image = ImageIO.read(new File(filename));
			marchThroughImage(image);

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	private void marchThroughImage(BufferedImage image) throws IOException {
		int w = image.getWidth();
		int h = image.getHeight();

		try {

			if (debug)
				System.out.println("width x height: " + w + " x " + h);

			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {

					int pixel = image.getRGB(j, i);
					int alpha = (pixel >> 24) & 0xff;
					int red = (pixel >> 16) & 0xff;
					int green = (pixel >> 8) & 0xff;
					int blue = (pixel) & 0xff;

					if (debug)
						System.out.println("(x, y): (" + j + ", " + i + ") " + "argb: " + alpha + ", " + red + ", "
								+ green + ", " + blue + "\n");
				}
			}
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
