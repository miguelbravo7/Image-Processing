package main.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageViewer extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Logger LOGGER = Logger.getLogger(ImageViewer.class.getName());
    transient BufferedImage image;
    JLabel imageCanvas;
    JLabel text;
    Color currentPixel;
    String positionText;
    String imgSizeText;
    String colorText;
    Integer xPosition;
    Integer yPosition;
    int xPressed;
    int yPressed;
    int xReleased;
    int yReleased;

    public ImageViewer(BufferedImage image) {
        super("Image Viewer");
        this.image = image;
        this.text = new JLabel();
        this.imageCanvas = new JLabel();
        this.currentPixel = new Color(0);
        this.xPosition = 0;
        this.yPosition = 0;
        this.imgSizeText = "Tamano " + image.getWidth() + "x" + image.getHeight();
        this.positionText = "Posicion (x:0, y:0)";
        this.colorText = "Colores a: 0 r: 0 g: 0 b: 0";
        updateText();

        this.imageCanvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                currentPixel = new Color(image.getRGB(e.getX(), e.getY()));
                xPosition = e.getX();
                yPosition = e.getY();
                updateText();
            }
        });
        this.imageCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xPressed = e.getX();
                yPressed = e.getY();
                LOGGER.log(Level.FINE, "Mouse pressed; start: x: {0}  y: {1}", new Object[] { xPressed, yPressed });
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                xReleased = e.getX();
                yReleased = e.getY();
                LOGGER.log(Level.FINE, "Mouse released; end: x: {0} y: {1}", new Object[] { xReleased, yReleased });
                releaseDispatcher();
            }
        });
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.initComponents();

        this.add(text, BorderLayout.SOUTH);
        this.setImage(image);
        this.pack();
        this.setLocationByPlatform(true);
    }

    /** Set the image as icon of the image canvas (display it). */
    private void setImage(BufferedImage image) {
        this.imageCanvas.setIcon(new ImageIcon(image)); // BufferedImage extends image
    }

    public void releaseDispatcher() {
        if(Menu.unpressAction != null) {
            Menu.unpressAction.accept(this);
            Menu.unpressAction = null;
        }
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.imageCanvas.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel imagecenter = new JPanel(new GridBagLayout());
        imagecenter.add(this.imageCanvas);
        JScrollPane imagescroll = new JScrollPane(imagecenter);
        add(imagescroll, BorderLayout.CENTER);
        // this.add(imagecenter, BorderLayout.CENTER);

    }

    private void updateText() {
        this.positionText = "Posicion (x:" + this.xPosition + ", y:" + this.yPosition + ")";
        this.colorText = "Colores a: " + this.currentPixel.getAlpha() +
        " r: " + this.currentPixel.getRed() + 
        " g: " + this.currentPixel.getGreen() + 
        " b: " + this.currentPixel.getBlue();

        this.text.setText(this.imgSizeText + "    " + this.positionText + "    " + this.colorText);
    }
}
