import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

public class Menu {
	private static final int WINDOW_SIZE = 800;
	final static String format = "jpg";
	JFrame frame = new JFrame("Editor");
	JTabbedPane tabbedPane = new JTabbedPane();
	File file;
	BufferedImage image;

	public Menu() {								
		//Interfaz grafica		new JLabel(new ImageIcon(image))
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WINDOW_SIZE, WINDOW_SIZE);
		frame.setLocation(100, 100);
		frame.add(tabbedPane, BorderLayout.CENTER);	
		Buttons botones = new Buttons(this);
		
		//Create the menu bar.
		JMenuBar menuBar = new JMenuBar();
		
		//Build the first menu.
		JMenu menu = new JMenu("Archivo");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(menu);
		menu.add(botones.getButtonLoad());
		menu.add(botones.getButtonSave());

		JMenu menu2 = new JMenu("Filtros");
		JMenu submenu = new JMenu("Operaciones sobre punto");
		submenu.add(botones.getButtonsFilterPal());
		submenu.add(botones.getButtonsFilterNeg());
		submenu.add(botones.getButtonsFilterKmeans());
		menu2.add(submenu);
		
		menuBar.add(menu2);
		
		menuBar.add(botones.getButtonsHistogram());
		
		
		frame.add(menuBar, BorderLayout.NORTH);
		frame.setVisible(true);		

	}
	
	public void addImage(BufferedImage image) {
		tabbedPane.addTab("Image", new ImageViewer(image).getContentPane());
	}
	
	public void addPalImage(BufferedImage image) {
		tabbedPane.addTab("PAL Monochrome", new ImageViewer(ImgMonochrome.renderPal(image)).getContentPane());
	}
	
	public void addNegativeImage(BufferedImage image) {
		tabbedPane.addTab("Negative", new ImageViewer(ImgNegative.render(image)).getContentPane());
	}
	
	public void addKMeansImage(BufferedImage image, int k) {
		tabbedPane.addTab(k + "-means", new ImageViewer(Kmeans_Processor.renderkmeans(image, k)).getContentPane());
	}

	public void makeHistogram(BufferedImage image) {
		System.out.println(image.getType());
		new Histogram(ImgConvert.toPixelArrayList(image), 3);
		new Histogram(ImgConvert.toPixelArrayList(image), 1);
	}
	
	public void openImage(String filepath) {
		// Creacion de archivos de lectura y escrtura		
		try {
			image = ImageIO.read(file = new File(filepath));
			addImage(image);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void saveImage(BufferedImage image, String filepath) {
		try {
			file = new File(filepath + "_result."+ format);
			ImageIO.write(image, format, file);
		} catch (IOException e) {	
			Utility.getAllFiles(file, "");		
			e.printStackTrace();
		}
	}
}