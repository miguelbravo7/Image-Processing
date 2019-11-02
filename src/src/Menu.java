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
import javax.swing.JTextField;

public class Menu {
	private static final int WINDOW_SIZE = 800;
	final static String format = "jpg";
	JFrame frame = new JFrame("Editor");
	JTabbedPane tabbedPane = new JTabbedPane();
	File file;
	BufferedImage image;
	ArrayList<Integer> imagetype = new ArrayList<Integer>();
	JLabel text = new JLabel("");

	public Menu() {			
		//acciones de la pestaña
		tabbedPane.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
		});
		tabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				((JLabel)getComponentImg(tabbedPane.getSelectedIndex())).addMouseMotionListener(new MouseAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						text.setText("Posicion x:" + e.getX() + "  y:" + e.getY() + "  " );
					}
				});
				
			}
			
		});
		tabbedPane.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent key) {
			if (key.getKeyCode() == KeyEvent.VK_DELETE) {
				imagetype.remove(tabbedPane.getSelectedIndex());
				tabbedPane.remove(tabbedPane.getSelectedIndex());
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
		if(imagetype.get(tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_INT_ARGB) {
			new Histogram(ImgConvert.toPixelArrayList(image), 3).histogram();
		}
		if(imagetype.get(tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_BYTE_GRAY)
			new Histogram(ImgConvert.toPixelArrayList(image), 1).histogram();
	}

	public void makeAccumulatedHistogram(BufferedImage image) {
		if(imagetype.get(tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_INT_ARGB)
			new Histogram(ImgConvert.toPixelArrayList(image), 3).histogramAcc();
		if(imagetype.get(tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_BYTE_GRAY)
			new Histogram(ImgConvert.toPixelArrayList(image), 1).histogramAcc();
	}

	public void makeNormHistogram(BufferedImage image) {
		if(imagetype.get(tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_INT_ARGB)
			new Histogram(ImgConvert.toPixelArrayList(image), 3).normHistogram();
		if(imagetype.get(tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_BYTE_GRAY)
			new Histogram(ImgConvert.toPixelArrayList(image), 1).normHistogram();
	}

	public void makeNormAccumulatedHistogram(BufferedImage image) {
		if(imagetype.get(tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_INT_ARGB)
			new Histogram(ImgConvert.toPixelArrayList(image), 3).normHistogramAcc();
		if(imagetype.get(tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_BYTE_GRAY)
			new Histogram(ImgConvert.toPixelArrayList(image), 1).normHistogramAcc();
	}
	private static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
          compList.add(comp);
          if (comp instanceof Container) {
            compList.addAll(getAllComponents((Container) comp));
          }


            if(comp instanceof JTextField){
                 System.out.println(comp);
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