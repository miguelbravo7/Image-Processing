import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Barra extends JMenuBar {
	private static final long serialVersionUID = 1L;
	Menu program;
	JFrame frame;
	JSlider brillo, contraste, umbral;
	JTextArea textarea;
	final int BRI_MIN = 0;
	final int BRI_MAX = 255;
	final int CONT_MIN = 0;
	final int CONT_MAX = 255;

	public Barra(Menu program) {
		this.program = program;
		this.frame = program.frame;

		// Set up a panel for the buttons
		// Create the menu bar.

		// Build the first menu.
		JMenu menu = new JMenu("Archivo");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		this.add(menu);
		JMenuItem cargar = new JMenuItem("Cargar");
		cargar.addActionListener(new ActLoadFile());
		menu.add(cargar);

		JMenuItem guardar = new JMenuItem("Guardar");
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
		JMenuItem gamma = new JMenuItem("Correccion de gamma");
		gamma.addActionListener(new ActImageGammaCorrection());
		submenu.add(gamma);
		JMenuItem kmeans = new JMenuItem("K-means");
		kmeans.addActionListener(new ActFilterImageKmeans());
		submenu.add(kmeans);
		JMenuItem change = new JMenuItem("Cambio brillo-contraste");
		change.addActionListener(new ActChangeBC());
		submenu.add(change);
		JMenuItem diff = new JMenuItem("Diferencia de imagen");
		diff.addActionListener(new ActImgDifference());
		submenu.add(diff);
		JMenuItem region = new JMenuItem("Región de interés");
		region.addActionListener(new ActToggleRegion());
		submenu.add(region);
		JMenuItem linear = new JMenuItem("Transformación lineal");
		linear.addActionListener(new ActLinealTransform());
		submenu.add(linear);

		menu2.add(submenu);
		this.add(menu2);

		JMenu menu3 = new JMenu("Grafos");
		JMenuItem hist = new JMenuItem("Histograma");
		hist.addActionListener(new ActImageHist());
		menu3.add(hist);
		JMenuItem histacc = new JMenuItem("Histograma acumulado");
		histacc.addActionListener(new ActImageHistAcc());
		menu3.add(histacc);
		JMenuItem normhist = new JMenuItem("Histograma normalizado");
		normhist.addActionListener(new ActImageNormHist());
		menu3.add(normhist);
		JMenuItem normhistacc = new JMenuItem("Histograma normalizado acumulado");
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
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println(
						"You chose to open this file: " + (filepath = chooser.getSelectedFile().getAbsolutePath()));
				program.openImage(filepath);
			}
		}
	}

	public class ActSaveFile implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFileChooser chooser = new JFileChooser(".");
			String filepath = "";
			chooser.setControlButtonsAreShown(true);
			chooser.setDialogTitle("Specify a file to save");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println(
						"You chose to save on this folder: " + (filepath = chooser.getSelectedFile().getPath()));
				program.saveImage(program.currentImage(), filepath);
			}
		}
	}

	public class ActFilterImageNeg implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgNegative.render(program.currentImage()), "Negativa");
		}
	}

	public class ActFilterImagePal implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgMonochrome.renderPal(program.currentImage()), "PAL");
		}
	}

	public class ActFilterImageEc implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgLinealTransform.ecualize(program.currentImage(), program.currentHist()), "Ecualizada");
		}
	}

	public class ActImageGammaCorrection implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgGammaCorrection.gammaCorrection(program.currentImage(), 1), "Gamma");
		}
	}

	public class ActFilterImageKmeans implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			int means = 4;
			program.addToPane(Kmeans_Processor.renderkmeans(program.currentImage(), means), means + "-means");
		}
	}

	public class ActChangeBC implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			brillo = new JSlider(JSlider.HORIZONTAL, BRI_MIN, BRI_MAX, program.currentHist().med[0]);
			brillo.setName("Brillo");

			// Turn on labels at major tick marks.
			brillo.setMajorTickSpacing(50);
			brillo.setMinorTickSpacing(1);
			brillo.setPaintTicks(true);
			brillo.setPaintLabels(true);
			brillo.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
				}
			});

			contraste = new JSlider(JSlider.HORIZONTAL, CONT_MIN, CONT_MAX, (program.currentHist().dev[0]).intValue());
			contraste.setName("contraste");

			contraste.setMajorTickSpacing(50);
			contraste.setMinorTickSpacing(1);
			contraste.setPaintTicks(true);
			contraste.setPaintLabels(true);
			contraste.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
				}
			});

			JFrame popup = new JFrame("Cambio brillo-contraste");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Brillo"));
			panel.add(brillo);
			popup.add(panel);
			JPanel panel2 = new JPanel(new GridLayout(2, 1));
			panel2.add(new JLabel("Contraste"));
			panel2.add(contraste);
			popup.add(panel2);

			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					program.addToPane(ImgBrightContrast.adjustImg(program.currentImage(), program.currentHist(),
							brillo.getValue(), contraste.getValue()), "Conversion");
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);
		}
	}

	public class ActImgDifference implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgDifference.difference(program.imagelist.get(0), program.imagelist.get(1)),
					"Diferencia");

			umbral = new JSlider(JSlider.HORIZONTAL, BRI_MIN, BRI_MAX, 175);
			umbral.setName("Brillo");

			// Turn on labels at major tick marks.
			umbral.setMajorTickSpacing(50);
			umbral.setMinorTickSpacing(1);
			umbral.setPaintTicks(true);
			umbral.setPaintLabels(true);
			umbral.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					ImgDifference.changeUmbral(program.currentImage(), umbral.getValue());
					program.doRedraw();
				}
			});

			JFrame popup = new JFrame("Diferencia de imagen");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(1, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Umbral"));
			panel.add(umbral);
			popup.add(panel);

			popup.pack();
			popup.setVisible(true);
		}
	}

	public class ActLinealTransform implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFrame popup = new JFrame("Diferencia de imagen");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			JPanel panelx = new JPanel();
			JPanel panely = new JPanel();
			panelx.setLayout(new BoxLayout(panelx, BoxLayout.Y_AXIS));
			panely.setLayout(new BoxLayout(panely, BoxLayout.Y_AXIS));
			panelx.add(new JTextField(6));
			panely.add(new JTextField(6));
			popup.setLayout(new FlowLayout());

			JButton addpoint = new JButton("Ok");

			addpoint.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panelx.add(new JTextField(6));
					panelx.revalidate();
					panelx.repaint();

					panely.add(new JTextField(6));
					panely.revalidate();
					panely.repaint();
				}
			});
			popup.add(addpoint);
			JPanel panels = new JPanel();
			panels.add(panelx);
			panels.add(panely);

			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					program.addToPane(ImgLinealTransform.LinealTransform(program.currentImage(), iterateOverJTextFields(panelx, panely)), "Trans. lineal");
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.add(panels);

			popup.pack();
			popup.setVisible(true);
		}

		private ArrayList<Point> iterateOverJTextFields(Container xcontainer, Container ycontainer) {
			ArrayList<Integer> x = new ArrayList<Integer>();
			ArrayList<Point> points = new ArrayList<Point>();
			for (Component component : xcontainer.getComponents()) {
				if (component instanceof JTextField) {
					x.add(Integer.parseInt(((JTextField) component).getText()));
				}
			}
			for (Component component : ycontainer.getComponents()) {
				if (component instanceof JTextField) {
					points.add(new Point(x.remove(0), Integer.parseInt(((JTextField) component).getText())));
				}
			}
			return points;
		}
	}

	public class ActToggleRegion implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.subimage_flag = true;
		}
	}

	public class ActImageHist implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.currentHist().histogram();
		}
	}

	public class ActImageHistAcc implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.currentHist().histogramAcc();
		}
	}

	public class ActImageNormHist implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.currentHist().normHistogram();
		}
	}

	public class ActImageNormHistAcc implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.currentHist().normHistogramAcc();
		}
	}
}