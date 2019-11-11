import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public class Menu {
	private static final int WINDOW_SIZE = 800;
	final static String format = "jpg";
	JFrame frame = new JFrame("Editor");
	JTabbedPane tabbedPane = new JTabbedPane();
	File file;
	ArrayList<Histogram> imagehist = new ArrayList<Histogram>();
	ArrayList<BufferedImage> imagelist = new ArrayList<BufferedImage>();
	JLabel text = new JLabel("");

	public Menu() {			
		tabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int tab_index = tabbedPane.getSelectedIndex();
				JLabel label = (JLabel)getComponentImg(tab_index);
				BufferedImage img = imagelist.get(tab_index);
				
				label.addMouseMotionListener(new MouseAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						int pixel = img.getRGB(e.getX(), e.getY());
						
						int red = (pixel >> 16) & 0xff;
						int green = (pixel >> 8) & 0xff;
						int blue = (pixel) & 0xff;
						
						text.setText("Posicion (x:" + e.getX() + "  y:" + e.getY() + ")  Colores r: " + red + " g: " + green + " b: " + blue);
					}
				});
				label.addMouseListener(new MouseAdapter() {
					 public void mousePressed(MouseEvent e) {
						System.out.println("Mouse pressed; start: x: " + e.getX() + "  y: " + e.getY());
				    }

				    public void mouseReleased(MouseEvent e) {
				    	System.out.println("Mouse released; end: x: " + e.getX() + "  y: " + e.getY());
				    }
				});				
			}			
		});
		tabbedPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				if (key.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteImage(tabbedPane.getSelectedIndex());					
				}
			}
		});
		//Interfaz grafica
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WINDOW_SIZE, WINDOW_SIZE);
		frame.setLocation(100, 100);
		
		frame.add(tabbedPane, BorderLayout.CENTER);					
		frame.add(new Barra(this), BorderLayout.NORTH);		
		frame.add(text, BorderLayout.SOUTH);
		
		
		frame.setVisible(true);		

	}
	
	public void addImage(BufferedImage image) {
		addToPane(image, "Ecualized");
	}
	
	public void deleteImage(int tab_index) {
		tabbedPane.remove(tab_index);
		imagehist.remove(tab_index);
		imagelist.remove(tab_index);
	}
	
	public void addPalImage(BufferedImage image) {
		addToPane(ImgMonochrome.renderPal(image), "PAL");
	}
	
	public void addNegativeImage(BufferedImage image) {
		addToPane(ImgNegative.render(image), "Ecualized");
	}
	
	public void addEcualizedImage(BufferedImage image) {
		addToPane(ImgLinealTransform.ecualize(image, imagehist.get(tabbedPane.getSelectedIndex())), "Ecualized");
	}
	
	public void addKMeansImage(BufferedImage image, int k) {
		addToPane(Kmeans_Processor.renderkmeans(image, k), k + "-means");
	}
	
	private void addToPane(	BufferedImage image, String text) {
		tabbedPane.addTab(text, new ImageViewer(image).getContentPane());
		imagehist.add(new Histogram(image));
		imagelist.add(image);
	}

	public void makeHistogram(BufferedImage image) {
		int tab_index = tabbedPane.getSelectedIndex();
		imagehist.get(tab_index).histogram();
	}

	public void makeAccumulatedHistogram(BufferedImage image) {
		int tab_index = tabbedPane.getSelectedIndex();
		imagehist.get(tab_index).histogramAcc();
	}

	public void makeNormHistogram(BufferedImage image) {
		int tab_index = tabbedPane.getSelectedIndex();
		imagehist.get(tab_index).normHistogram();
	}

	public void makeNormAccumulatedHistogram(BufferedImage image) {
		int tab_index = tabbedPane.getSelectedIndex();
		imagehist.get(tab_index).normHistogramAcc();
	}
	private static List<Component> getAllComponents(final Container c) {
	    Component[] comps = c.getComponents();
	    List<Component> compList = new ArrayList<Component>();
	    for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container) {
				compList.addAll(getAllComponents((Container) comp));
			}
	    }
	    return compList;
    }
	
	public Component getComponentImg(int index) {
		List<Component> comp = getAllComponents(tabbedPane.getFocusCycleRootAncestor());
		
		return comp.get(index*3+7);
	}
	
	public void openImage(String filepath) {
		// Creacion de archivos de lectura y escrtura		
		try {
			BufferedImage image = ImageIO.read(file = new File(filepath));
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