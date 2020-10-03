package main.gui;

import main.graphs.*;
import main.utils.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public final class Menu{
	public static final JTabbedPane tabbedPane = new JTabbedPane();
	public static final JFrame MENU_FRAME = new JFrame();
	public static Consumer<ImageViewer> unpressAction = null;
	public static final List<Histogram> imagehist = new ArrayList<>();
	public static final List<BufferedImage> imagelist = new ArrayList<>();
	private static final String FORMAT = "jpg";
	private static Integer imgCount = 0;
	private static final int WINDOW_SIZE = 800;

	private Menu() {
		throw new IllegalStateException("Utility class");
	}

	static {
		tabbedPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				if (key.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteFromPane(tabbedPane.getSelectedIndex());
				}
			}
		});
		// Interfaz grafica
		MENU_FRAME.setTitle("Editor");
		MENU_FRAME.setLayout(new BorderLayout());
		MENU_FRAME.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		MENU_FRAME.setSize(WINDOW_SIZE, WINDOW_SIZE);
		MENU_FRAME.setLocation(100, 100);

		MENU_FRAME.add(tabbedPane, BorderLayout.CENTER);
		MENU_FRAME.add(new MenuBar(), BorderLayout.NORTH);

		MENU_FRAME.setVisible(true);

	}

	public static void deleteFromPane(int tabIndex) {
		tabbedPane.remove(tabIndex);
		imagehist.remove(tabIndex);
		imagelist.remove(tabIndex);
	}

	public static void addToPane(BufferedImage image, String text) {
		imagelist.add(image);
		try {
			imagehist.add(new Histogram(image));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tabbedPane.addTab(text + "_" + imgCount++, new ImageViewer(image));
	}

	public static Histogram currentHist() {
		return imagehist.get(tabbedPane.getSelectedIndex());
	}

	public static BufferedImage currentImage() {
		return imagelist.get(tabbedPane.getSelectedIndex());
	}
	
	public BufferedImage setcurrentImage(BufferedImage img) {
		return imagelist.set(tabbedPane.getSelectedIndex(), img);
	}
	
	private static List<Component> getAllComponents(final Container c) {
	    Component[] comps = c.getComponents();
	    List<Component> compList = new ArrayList<>();
	    for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container) {
				compList.addAll(getAllComponents((Container) comp));
			}
	    }
	    return compList;
    }
	
	private static Component getComponentImg(int index) {
		List<Component> comp = getAllComponents(tabbedPane.getFocusCycleRootAncestor());
		List<JLabel> labels = new ArrayList<>();
		for(Component c : comp) {
			if (c instanceof JLabel) {
				labels.add((JLabel) c);				
			}
		}
		return labels.get(index);
	}
	
	public static void openImage(String filepath) {
		BufferedImage image = openTiffCompatibleImg(new File(filepath));
		addToPane(image, "Imagen");
	}
	
	private static BufferedImage openTiffCompatibleImg(File file) {
		BufferedImage tiff = null;
		try {
			try (InputStream is = new FileInputStream(file)) {
				try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(is)) {
					Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
					if (iterator == null || !iterator.hasNext()) {
						throw new RuntimeException("Image file format not supported by jai ImageIO: " + file.getAbsolutePath());
					}					
					// We are just looking for the first reader compatible:
					ImageReader reader = iterator.next();
					reader.setInput(imageInputStream);
					
					int numPage = reader.getNumImages(true);
					
					// it uses to put new png files, close to original example n0_.tiff will be in /png/n0_0.png					
					int val = IntStream.range(0, numPage).filter(v -> {
						try {
							return reader.read(v) != null;
						} catch (IOException e) {
							e.printStackTrace();
						}
						return false;
					}).findFirst().orElse(-1);
					tiff = reader.read(val);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	    BufferedImage convertedImg = new BufferedImage(tiff.getWidth(), tiff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	    convertedImg.getGraphics().drawImage(tiff, 0, 0, null);
		return convertedImg;
	}
	
	public static void saveImage(BufferedImage image, String filepath) {
		File file = new File("");
		try {
			file = new File(filepath + "_result."+ FORMAT);
			ImageIO.write(image, FORMAT, file);
		} catch (IOException e) {	
			Utility.getAllFiles(file, "");		
			e.printStackTrace();
		}
	}
	
	public static void doRedraw(){
        ((JComponent) getComponentImg(tabbedPane.getSelectedIndex())).getTopLevelAncestor().revalidate();
        ((JComponent) getComponentImg(tabbedPane.getSelectedIndex())).getTopLevelAncestor().repaint();
    }
}