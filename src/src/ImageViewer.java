import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ImageViewer extends JFrame{
	private static final long serialVersionUID = 1L;
    /** Displays the image. */
    JLabel imagecanvas;

    public ImageViewer(BufferedImage image) {
    	super("Image Viewer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		initComponents();

		this.setImage(image);

        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
    }

    /** Set the image as icon of the image canvas (display it). */
    public void setImage(BufferedImage image) {
        imagecanvas.setIcon(new ImageIcon(image));    	// BufferedImage extends image
    }

    public void initComponents() {
	    this.setLayout(new BorderLayout());
	    imagecanvas = new JLabel();
	    imagecanvas.setBorder(new EmptyBorder(5,5,5,5));
	
	    JPanel imagecenter = new JPanel(new GridBagLayout());
	    imagecenter.add(imagecanvas);
	    JScrollPane imagescroll = new JScrollPane(imagecenter);
	    add(imagescroll, BorderLayout.CENTER);
    }
}