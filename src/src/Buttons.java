import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Buttons extends JPanel{
	private static final long serialVersionUID = 1L;
	Menu program;
	JFrame frame;
	private JCheckBox chk;
    JSlider degrees;
    JSlider velocity;
	JTextArea textarea;
	final int DEG_MIN = 0;
	final int DEG_MAX = 255;
	final int DEG_INIT = 127;    //initial frames per second
	final int VEL_MIN = 0;
	final int VEL_MAX = 255;
	final int VEL_INIT = 127;    //initial meters per second

	public Buttons(Menu program) {
		this.program = program;
		this.frame = program.frame;
		
		// Set up a panel for the buttons
		JButton btnS = new JButton("Cargar");
	    add(btnS);
	    btnS.addActionListener(new ActLoadFile());
		JButton btnP = new JButton("Guardar");
	    add(btnP);
	    btnP.addActionListener(new ActSaveFile());
		JButton btnC = new JButton("filtros");
	    add(btnC);
	    btnC.addActionListener(new ActFilterImage());
	    
	    chk = new JCheckBox("Show trace");
	    chk.setSelected(true);
	    chk.addActionListener(new ActCheck());
	    add(chk);
	    
	    degrees = new JSlider(JSlider.HORIZONTAL, DEG_MIN, DEG_MAX, DEG_INIT);
	    //framesPerSecond.addChangeListener(this);

	    //Turn on labels at major tick marks.
	    degrees.setMajorTickSpacing(50);
	    degrees.setMinorTickSpacing(1);
	    degrees.setPaintTicks(true);
	    degrees.setPaintLabels(true);
	    
	    add(degrees);
	    
	    velocity = new JSlider(JSlider.HORIZONTAL, VEL_MIN, VEL_MAX, VEL_INIT);

	    velocity.setMajorTickSpacing(50);
	    velocity.setMinorTickSpacing(1);
	    velocity.setPaintTicks(true);
	    velocity.setPaintLabels(true);
	    
	    add(velocity);
	    
        JButton button = new JButton();

        button.setText("Info");
        add(button);

        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(getParent(), "Asignatura: PAI\nPráctica: 12\nDescripción: Simulador de proyectiles");
            }
        });
        add(textarea = new JTextArea("0"));
        
	}

	public class ActLoadFile implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFileChooser chooser = new JFileChooser(".");
		    String filepath = "";
		    chooser.setFileFilter(new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"));
		    int returnVal = chooser.showOpenDialog(frame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to open this file: " +
		            (filepath = chooser.getSelectedFile().getAbsolutePath()));
		    }
		    program.openImage(filepath);			
		}
	}

	public class ActSaveFile implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFileChooser chooser = new JFileChooser(".");
		    String filepath = "";
		    chooser.setControlButtonsAreShown( false );
		    chooser.setFileFilter(new FolderFilter());
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    int returnVal = chooser.showOpenDialog(frame);
		    if(returnVal == JFileChooser.DIRECTORIES_ONLY) {
		       System.out.println("You chose to save on this folder: " +
		            (filepath = chooser.getSelectedFile().getAbsolutePath()));
		    }
		    Component drawpanel = program.tabbedPane.getSelectedComponent();
		    BufferedImage awtImage = new BufferedImage(drawpanel.getWidth(), drawpanel.getHeight(), BufferedImage.TYPE_INT_RGB);
		    Graphics g = awtImage.getGraphics();
		    drawpanel.printAll(g);
		    program.saveImage(awtImage, filepath);
		}
		private class FolderFilter extends javax.swing.filechooser.FileFilter {
			@Override
			public boolean accept( File file ) {
				return file.isDirectory();
			}
			
			@Override
			public String getDescription() {
				return "Selecciona el directorio de guardado";
			}
		}
	}

	public class ActFilterImage implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			
		}
	}

	public class ActCheck implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        	
        }
	}
}