import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JComponent;
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
	boolean subimage_flag;
	int acc_x, acc_y;
	Integer image_count = 0;

	public Menu() {			
		tabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

				int tab_index = tabbedPane.getSelectedIndex();
				if(tab_index != -1) {
					JLabel label = (JLabel)getComponentImg(tab_index);
					BufferedImage img = imagelist.get(tab_index);
					if (label.getMouseListeners().length == 0) {
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
								if(subimage_flag) {
									System.out.println("Mouse pressed; start: x: " + (acc_x = e.getX()) + "  y: " + (acc_y = e.getY()));							 
								}
							}
							
							public void mouseReleased(MouseEvent e) {
								if(subimage_flag) {
									System.out.println("Mouse released; end: x: " + e.getX() + "  y: " + e.getY());
									addToPane(img.getSubimage(acc_x, acc_y, e.getX() - acc_x , e.getY() - acc_y), "Imagen recortada");
									subimage_flag = false;							 
								}
							}
						});									
					}					
				}
			}			
		});
		tabbedPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				if (key.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteFromPane(tabbedPane.getSelectedIndex());					
				}
			}
		});
		// Interfaz grafica
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WINDOW_SIZE, WINDOW_SIZE);
		frame.setLocation(100, 100);
		
		frame.add(tabbedPane, BorderLayout.CENTER);					
		frame.add(new Barra(this), BorderLayout.NORTH);		
		frame.add(text, BorderLayout.SOUTH);
		
		
		frame.setVisible(true);		

	}
	
	public void deleteFromPane(int tab_index) {
		tabbedPane.remove(tab_index);
		imagehist.remove(tab_index);
		imagelist.remove(tab_index);
	}
	
	public void addToPane(BufferedImage image, String text) {
		System.out.println(image.getType());
		imagelist.add(image);
		imagehist.add(new Histogram(image));
		tabbedPane.addTab(text + "_" + image_count++, new ImageViewer(image).getContentPane());
	}
	
	public Histogram currentHist() {
		return imagehist.get(tabbedPane.getSelectedIndex());
	}
	
	public BufferedImage currentImage() {
		return imagelist.get(tabbedPane.getSelectedIndex());
	}
	
	public BufferedImage setcurrentImage(BufferedImage img) {
		return imagelist.set(tabbedPane.getSelectedIndex(), img);
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
		List<JLabel> labels = new ArrayList<JLabel>();
		for(Component c : comp) {
			if (c instanceof JLabel) {
				labels.add((JLabel) c);				
			}
		}
		return labels.get(index);
	}
	
	public void openImage(String filepath) {
		BufferedImage image = open_image_tiff_compatible(new File(filepath));
		addToPane(image, "Imagen");
	}
	
	private BufferedImage open_image_tiff_compatible(File file) {
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
	
	public void saveImage(BufferedImage image, String filepath) {
		try {
			file = new File(filepath + "_result."+ format);
			ImageIO.write(image, format, file);
		} catch (IOException e) {	
			Utility.getAllFiles(file, "");		
			e.printStackTrace();
		}
	}
	
	public void doRedraw(){
        ((JComponent) getComponentImg(tabbedPane.getSelectedIndex())).getTopLevelAncestor().revalidate();
        ((JComponent) getComponentImg(tabbedPane.getSelectedIndex())).getTopLevelAncestor().repaint();
    }
}