import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

//import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Barra extends JMenuBar {
	private static final long serialVersionUID = 1L;
	Menu program;
	JFrame frame;
    JSlider brillo;
    JSlider contraste;
	JTextArea textarea;
	final int BRI_MIN = 0;
	final int BRI_MAX = 255;
	final int BRI_INIT = 127;    // brillo inicial
	final int CONT_MIN = 0;
	final int CONT_MAX = 255;
	final int CONT_INIT = 127;         // contraste inicial

	public Barra(Menu program) {
		this.program = program;
		this.frame = program.frame;
		
		// Set up a panel for the buttons        
        //Create the menu bar.
		
		//Build the first menu.
		JMenu menu = new JMenu("Archivo");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		this.add(menu);
		JMenuItem cargar  =new JMenuItem("Cargar");
		cargar.addActionListener(new ActLoadFile());
		menu.add(cargar);

		JMenuItem guardar =new JMenuItem("Guardar");
		guardar.addActionListener(new ActSaveFile());
		menu.add(guardar);

		JMenu menu2 = new JMenu("Filtros");
		JMenu submenu = new JMenu("Operaciones sobre punto");

		JMenuItem pal = new JMenuItem("PAL");
		pal.addActionListener(new ActFilterImagePal());
		submenu.add(pal); 		
		JMenuItem neg = new JMenuItem("Negativo");
		neg.addActionListener(new ActFilterImageNeg());
		submenu.add(neg); 		
		JMenuItem ec = new JMenuItem("Ecualizacion");
		ec.addActionListener(new ActFilterImageEc());
		submenu.add(ec); 		
		JMenuItem kmeans = new JMenuItem("K-means");
		kmeans.addActionListener(new ActFilterImageKmeans());
		submenu.add(kmeans);	
		JMenuItem change = new JMenuItem("Cambio brillo-contraste");
		change.addActionListener(new ActChangeBC());
		submenu.add(change);
		
		menu2.add(submenu);		
		this.add(menu2);


		JMenu menu3 = new JMenu("Grafos");
		JMenuItem hist =new JMenuItem("Histograma");
		hist.addActionListener(new ActImageHist());
		menu3.add(hist);
		JMenuItem histacc =new JMenuItem("Histograma acumulado");
		histacc.addActionListener(new ActImageHistAcc());
		menu3.add(histacc);
		JMenuItem normhist =new JMenuItem("Histograma normalizado");
		normhist.addActionListener(new ActImageNormHist());
		menu3.add(normhist);
		JMenuItem normhistacc =new JMenuItem("Histograma normalizado acumulado");
		normhistacc.addActionListener(new ActImageNormHistAcc());
		menu3.add(normhistacc);
		this.add(menu3);
        
	}
	
	public class ActLoadFile implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFileChooser chooser = new JFileChooser(".");
		    String filepath = "";
		    chooser.setFileFilter(new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"));
		    int returnVal = chooser.showOpenDialog(frame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to open this file: " + (filepath = chooser.getSelectedFile().getAbsolutePath()));
		       program.openImage(filepath);
		       program.imagetype.add(BufferedImage.TYPE_INT_ARGB);	
		    }
		}
	}

	public class ActSaveFile implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFileChooser chooser = new JFileChooser(".");
		    String filepath = "";
		    chooser.setControlButtonsAreShown( true );
		    chooser.setDialogTitle("Specify a file to save");  
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    int returnVal = chooser.showSaveDialog(frame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to save on this folder: " + (filepath = chooser.getSelectedFile().getPath()));
		       program.saveImage(currentImage(), filepath);
		    }
		}
	}

	public class ActFilterImageNeg implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
		    program.addNegativeImage(currentImage());
		    if(program.imagetype.get(program.tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_BYTE_GRAY)
		    	program.imagetype.add(BufferedImage.TYPE_BYTE_GRAY);
		    else
		    	program.imagetype.add(BufferedImage.TYPE_INT_ARGB);
		}
	}

	public class ActFilterImagePal implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
		    program.addPalImage(currentImage());
		    program.imagetype.add(BufferedImage.TYPE_BYTE_GRAY);
		}
	}

	public class ActFilterImageEc implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
		    program.addEcualizedImage(currentImage());
		    if(program.imagetype.get(program.tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_BYTE_GRAY)
		    	program.imagetype.add(BufferedImage.TYPE_BYTE_GRAY);
		    else
		    	program.imagetype.add(BufferedImage.TYPE_INT_ARGB);
		}
	}

	public class ActFilterImageKmeans implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			int means = 4;
		    program.addKMeansImage(currentImage(), means);
		    if(program.imagetype.get(program.tabbedPane.getSelectedIndex()) == BufferedImage.TYPE_BYTE_GRAY)
		    	program.imagetype.add(BufferedImage.TYPE_BYTE_GRAY);
		    else
		    	program.imagetype.add(BufferedImage.TYPE_INT_ARGB);
		}
	}

	public class ActChangeBC implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			brillo = new JSlider(JSlider.HORIZONTAL, BRI_MIN, BRI_MAX, BRI_INIT);
		    brillo.setName("Brillo");

		    //Turn on labels at major tick marks.
			brillo.setMajorTickSpacing(50);
			brillo.setMinorTickSpacing(1);
			brillo.setPaintTicks(true);
			brillo.setPaintLabels(true);
			brillo.addChangeListener(new ChangeListener(){
		          public void stateChanged(ChangeEvent e) {
		              System.out.println(brillo.getValue());
		          }
		    });
		    		    
		    contraste = new JSlider(JSlider.HORIZONTAL, CONT_MIN, CONT_MAX, CONT_INIT);
		    contraste.setName("contraste");

		    contraste.setMajorTickSpacing(50);
		    contraste.setMinorTickSpacing(1);
		    contraste.setPaintTicks(true);
		    contraste.setPaintLabels(true);
		    contraste.addChangeListener(new ChangeListener(){
		          public void stateChanged(ChangeEvent e) {
		              System.out.println(contraste.getValue());
		          }
		    });
		    
		    JFrame sliders = new JFrame("Cambio brillo-contraste");
		    sliders.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    sliders.setLayout(new GridLayout(2, 1));
		      JPanel panel = new JPanel(new GridLayout(2, 1));
		      panel.add(new JLabel("Brillo"));
		      panel.add(brillo);
		      sliders.add(panel);
		      JPanel panel2 = new JPanel(new GridLayout(2, 1));
		      panel2.add(new JLabel("Contraste"));
		      panel2.add(contraste);
		      sliders.add(panel2);
		    sliders.pack();
		    sliders.setVisible(true);
		}
	}

	public class ActImageHist implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
		    program.makeHistogram(currentImage());
		}
	}

	public class ActImageHistAcc implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
		    program.makeAccumulatedHistogram(currentImage());
		}
	}

	public class ActImageNormHist implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
		    program.makeNormHistogram(currentImage());
		}
	}

	public class ActImageNormHistAcc implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
		    program.makeNormAccumulatedHistogram(currentImage());
		}
	}
	
	private BufferedImage getImageFromComponent(Component component) {
	    BufferedImage awtImage = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics g = awtImage.getGraphics();
	    component.printAll(g);
		return awtImage;
	}
	
	private BufferedImage currentImage() {
		return getImageFromComponent(program.getComponentImg(program.tabbedPane.getSelectedIndex()));
	}
}