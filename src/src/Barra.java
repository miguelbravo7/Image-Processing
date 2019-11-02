import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

//import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Barra extends JMenuBar implements ActionListener,ItemListener{
	private static final long serialVersionUID = 1L;
	Menu program;
	JFrame frame;
//	private JCheckBox chk;
    JSlider degrees;
    JSlider velocity;
	JTextArea textarea;
	final int DEG_MIN = 0;
	final int DEG_MAX = 255;
	final int DEG_INIT = 127;    //initial frames per second
	final int VEL_MIN = 0;
	final int VEL_MAX = 255;
	final int VEL_INIT = 127;    //initial meters per second

	public Barra(Menu program) {
		this.program = program;
		this.frame = program.frame;
//		
//		// Set up a panel for the buttons
//	    
//	    chk = new JCheckBox("Show trace");
//	    chk.setSelected(true);
//	    chk.addActionListener(new ActCheck());
//	    add(chk);
//	    
//	    degrees = new JSlider(JSlider.HORIZONTAL, DEG_MIN, DEG_MAX, DEG_INIT);
//	    //framesPerSecond.addChangeListener(this);
//
//	    //Turn on labels at major tick marks.
//	    degrees.setMajorTickSpacing(50);
//	    degrees.setMinorTickSpacing(1);
//	    degrees.setPaintTicks(true);
//	    degrees.setPaintLabels(true);
//	    
//	    add(degrees);
//	    
//	    velocity = new JSlider(JSlider.HORIZONTAL, VEL_MIN, VEL_MAX, VEL_INIT);
//
//	    velocity.setMajorTickSpacing(50);
//	    velocity.setMinorTickSpacing(1);
//	    velocity.setPaintTicks(true);
//	    velocity.setPaintLabels(true);
//	    
//	    add(velocity);
//	    
//        JButton button = new JButton();
//
//        button.setText("Info");
//        add(button);
//
//        button.addActionListener(new java.awt.event.ActionListener() {
//            @Override
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                JOptionPane.showMessageDialog(getParent(), "Asignatura: PAI\nPr�ctica: 12\nDescripci�n: Simulador de proyectiles");
//            }
//        });
//        add(textarea = new JTextArea("0"));
        
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

		JMenuItem pal =new JMenuItem("PAL");
		pal.addActionListener(new ActFilterImagePal());
		submenu.add(pal); 		
		JMenuItem neg =new JMenuItem("Negativo");
		neg.addActionListener(new ActFilterImageNeg());
		submenu.add(neg); 		
		JMenuItem kmeans =new JMenuItem("K-means");
		kmeans.addActionListener(new ActFilterImageKmeans());
		submenu.add(kmeans);
		
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

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}