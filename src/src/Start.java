import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Start {
	private static final int WINDOW_SIZE = 800;
	final static String format = "png";
	final static String filename = "Windows_XP";

	public static void main(String[] args) {
		// Creacion de archivos de lectura y escrtura
		// get the BufferedImage, using the ImageIO class
		BufferedImage image = null;
		File file = null;
		try {
			image = ImageIO.read(file = new File(filename+"." + format));
			file = new File(filename + "_result."+ format);
			
		} catch (IOException e) {
			System.out.println("Error: " + e);
			Utility.getAllFiles(file, "");
		}
		
		Histogram histograma = new Histogram(ImgConvert.toPixelArrayList(image));
//		System.out.println(histograma);
		
		BufferedImage result = image;
		
//		result = ImgScalar.render(result, 0.299f, 0.587f, 0.114f);		
//		new ImageViewer(result);
//		
//		result = ImgNegative.render(result);
//		new ImageViewer(result);
//		result = Kmeans_Processor.renderkmeans(result, 4);
//		result = ImgConvert.toBuffImg(ImgConvert.toPixelArrayList(image), image.getWidth(), image.getHeight());
		
		
		//Interfaz grafica
		JFrame frame = new JFrame(filename);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WINDOW_SIZE, WINDOW_SIZE);
		frame.setLocation(100, 100);
//		frame.add(new JLabel(new ImageIcon(result)), BorderLayout.CENTER);
		


//		frame.setVisible(true);
//		try {
//			Process process = new ProcessBuilder("explorer.exe", "/select,C:\\directory\\selectedFile").start();
//			new BufferedReader(new InputStreamReader(process.getInputStream()));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		try {
			ImageIO.write(result, format, file);
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		System.out.println("Terminado.");
	}
}