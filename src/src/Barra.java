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
import javax.swing.JComboBox;
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
	JTextField brillo, contraste;
	JSlider umbral;
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

		JMenu filtros = new JMenu("Filtros");
		JMenu op_punto = new JMenu("Operaciones sobre punto");

		JMenuItem pal = new JMenuItem("PAL");
		pal.addActionListener(new ActFilterImagePal());
		op_punto.add(pal);
		JMenuItem neg = new JMenuItem("Negativo");
		neg.addActionListener(new ActFilterImageNeg());
		op_punto.add(neg);
		JMenuItem ec = new JMenuItem("Ecualizacion");
		ec.addActionListener(new ActFilterImageEc());
		op_punto.add(ec);
		JMenuItem gamma = new JMenuItem("Correccion de gamma");
		gamma.addActionListener(new ActImageGammaCorrection());
		op_punto.add(gamma);
		JMenuItem kmeans = new JMenuItem("K-means");
		kmeans.addActionListener(new ActFilterImageKmeans());
		op_punto.add(kmeans);

		JMenu digitalization = new JMenu("Operaciones sobre punto");
		JMenuItem depth = new JMenuItem("Digitalizacion por shifteo");
		depth.addActionListener(new ActFilterImageDepthShift());
		digitalization.add(depth);
		JMenuItem d_region = new JMenuItem("Digitalizacion por region");
		d_region.addActionListener(new ActFilterImageDepthRegion());
		digitalization.add(d_region);
		op_punto.add(digitalization);
		
		JMenuItem change = new JMenuItem("Cambio brillo-contraste");
		change.addActionListener(new ActChangeBC());
		op_punto.add(change);
		JMenuItem spec = new JMenuItem("Especificacion de histograma");
		spec.addActionListener(new ActImgHistSpec());
		op_punto.add(spec);
		JMenuItem diff = new JMenuItem("Diferencia de imagen");
		diff.addActionListener(new ActImgDifference());
		op_punto.add(diff);
		JMenuItem region = new JMenuItem("Región de interés");
		region.addActionListener(new ActToggleRegion());
		op_punto.add(region);
		JMenuItem linear = new JMenuItem("Transformación lineal");
		linear.addActionListener(new ActLinealTransform());
		op_punto.add(linear);
		
		filtros.add(op_punto);

		JMenu op_geom = new JMenu("Operaciones geometricas");
		JMenu espejo = new JMenu("Operaciones de espejo");
		JMenuItem vertical = new JMenuItem("Espejo vertical");
		vertical.addActionListener(new ActGeomMirrorVertical());
		espejo.add(vertical);
		JMenuItem horizontal = new JMenuItem("Espejo horizontal");
		horizontal.addActionListener(new ActGeomMirrorHorizontal());
		espejo.add(horizontal);
		JMenuItem transpuesta = new JMenuItem("Transpuesta");
		transpuesta.addActionListener(new ActGeomTranspose());
		espejo.add(transpuesta);
		JMenu f_rotations = new JMenu("Rotaciones fixadas");
		JMenuItem rotar90 = new JMenuItem("Rotar 90º");
		rotar90.addActionListener(new ActGeomRotate90());
		f_rotations.add(rotar90);
		JMenuItem rotar180 = new JMenuItem("Rotar 180º");
		rotar180.addActionListener(new ActGeomRotate180());
		f_rotations.add(rotar180);
		JMenuItem rotar270 = new JMenuItem("Rotar 270º");
		rotar270.addActionListener(new ActGeomRotate270());
		f_rotations.add(rotar270);
		espejo.add(f_rotations);
		
		op_geom.add(espejo);

		JMenu rotar = new JMenu("Rotar");
		JMenuItem rneighbour = new JMenuItem("Vecino mas proximo");
		rneighbour.addActionListener(new ActGeomRotate("Neighbour"));
		rotar.add(rneighbour);
		JMenuItem rbilinear = new JMenuItem("Bilinear");
		rbilinear.addActionListener(new ActGeomRotate("Bilinear"));
		rotar.add(rbilinear);
		
		op_geom.add(rotar);

		JMenu scale = new JMenu("Cambio de escala");
		JMenuItem neighbour = new JMenuItem("Vecino mas proximo");
		neighbour.addActionListener(new ActGeomScale("Neighbour"));
		scale.add(neighbour);
		JMenuItem bilinear = new JMenuItem("Bilinear");
		bilinear.addActionListener(new ActGeomScale("Bilinear"));
		scale.add(bilinear);
		
		op_geom.add(scale);
		
		filtros.add(op_geom);

		this.add(filtros);

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
			JTextField gamma = new JTextField(1);

			JFrame popup = new JFrame("Correccion de gamma");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Gamma"));
			panel.add(gamma);
			popup.add(panel);
			
			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					program.addToPane(ImgGammaCorrection.gammaCorrection(program.currentImage(), Double.valueOf(gamma.getText())), "Gamma");
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);
		}
	}

	public class ActFilterImageKmeans implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JTextField means = new JTextField(1);

			JFrame popup = new JFrame("K-means");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Number of means"));
			panel.add(means);
			popup.add(panel);
			
			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					program.addToPane(Kmeans_Processor.renderkmeans(program.currentImage(), Integer.valueOf(means.getText())), means.getText() + "-means");
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);
		}
	}

	public class ActFilterImageDepthShift implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JTextField depth = new JTextField(1);

			JFrame popup = new JFrame("Digitalizacion");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Numero de bits"));
			panel.add(depth);
			popup.add(panel);
			
			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					program.addToPane(ImgColorDepth.colorDepthShift(program.currentImage(), Integer.valueOf(depth.getText())), depth.getText() + " bit depth");
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);
		}
	}

	public class ActFilterImageDepthRegion implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JTextField depth = new JTextField(1);

			JFrame popup = new JFrame("Digitalizacion");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Tamaño de la celda"));
			panel.add(depth);
			popup.add(panel);
			
			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					program.addToPane(ImgColorDepth.colorDepthRegion(program.currentImage(), Integer.valueOf(depth.getText())), depth.getText() + " bit depth");
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);
		}
	}
	
	public class ActChangeBC implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			brillo = new JTextField(program.currentHist().med[0].toString());

			contraste = new JTextField(program.currentHist().dev[0].toString());


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
					program.addToPane(ImgBrightContrast.adjustImg(
							program.currentImage(),
							program.currentHist(),
							Double.valueOf(brillo.getText()),
							Double.valueOf(contraste.getText())),
							"Conversion");
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);
		}
	}
	
	public class ActImgHistSpec implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			ArrayList<String> pestanas = new ArrayList<String>();
			
			for (int i = 0; i < program.tabbedPane.getTabCount(); i++) {
				pestanas.add(program.tabbedPane.getTitleAt(i));
			}

			JComboBox<Object> img1 = new JComboBox<Object>(pestanas.toArray());

			JComboBox<Object> img2 = new JComboBox<Object>(pestanas.toArray());
			
			JFrame popup = new JFrame("Especificacion de histograma.");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Imagen"));
			panel.add(img1);
			popup.add(panel);
			JPanel panel2 = new JPanel(new GridLayout(2, 1));
			panel2.add(new JLabel("Histograma de referencia"));
			panel2.add(img2);
			popup.add(panel2);

			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					popup.dispose();					
					program.addToPane(ImgSpecHist.convertHist(
							program.imagelist.get(img1.getSelectedIndex()),
							program.imagehist.get(img1.getSelectedIndex()),
							program.imagehist.get(img2.getSelectedIndex())),
							"Spec. hist.");
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);			
		}
	}

	public class ActImgDifference implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			ArrayList<String> pestanas = new ArrayList<String>();
			
			for (int i = 0; i < program.tabbedPane.getTabCount(); i++) {
				pestanas.add(program.tabbedPane.getTitleAt(i));
			}

			JComboBox<Object> img1 = new JComboBox<Object>(pestanas.toArray());

			JComboBox<Object> img2 = new JComboBox<Object>(pestanas.toArray());
			
			JFrame popup = new JFrame("Diferencia de imagen.");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Imagen 1"));
			panel.add(img1);
			popup.add(panel);
			JPanel panel2 = new JPanel(new GridLayout(2, 1));
			panel2.add(new JLabel("Imagen 2"));
			panel2.add(img2);
			popup.add(panel2);

			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					popup.dispose();
					
					program.addToPane(ImgDifference.difference(
							program.imagelist.get(img1.getSelectedIndex()),
							program.imagelist.get(img2.getSelectedIndex())),
							"Diferencia");
					umbralPopup();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);			
		}
	}
	
	private void umbralPopup() {
		umbral = new JSlider(JSlider.HORIZONTAL, BRI_MIN, BRI_MAX, 255);
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
		
		JFrame popup_umbral = new JFrame("Diferencia de imagen");
		popup_umbral.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup_umbral.setLayout(new GridLayout(1, 1));
		
		JPanel panel_umbral = new JPanel(new GridLayout(2, 1));
		panel_umbral.add(new JLabel("Umbral"));
		panel_umbral.add(umbral);
		popup_umbral.add(panel_umbral);
		
		popup_umbral.pack();
		popup_umbral.setVisible(true);		
	}

	public class ActLinealTransform implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFrame popup = new JFrame("Diferencia de imagen");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			JPanel panelx = new JPanel();
			JPanel panely = new JPanel();
			panelx.setLayout(new BoxLayout(panelx, BoxLayout.Y_AXIS));
			panely.setLayout(new BoxLayout(panely, BoxLayout.Y_AXIS));
			panelx.add(new JTextField("x:",6));
			panely.add(new JTextField("y:",6));
			popup.setLayout(new FlowLayout());

			JButton addpoint = new JButton("Anadir punto");

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

	public class ActGeomMirrorVertical implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgMirror.vertical(program.currentImage()), "Vertical");
		}
	}

	public class ActGeomMirrorHorizontal implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgMirror.horizontal(program.currentImage()), "Horizontal");
		}
	}

	public class ActGeomTranspose implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgTranspose.transpose(program.currentImage()), "Transpuesta");
		}
	}
	
	public class ActGeomRotate implements ActionListener {
		String function;
		public ActGeomRotate(String function) {
			this.function = function;
		}
		public void actionPerformed(ActionEvent evt) {
			JTextField means = new JTextField(0);

			JFrame popup = new JFrame("Rotacion");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Angulo"));
			panel.add(means);
			popup.add(panel);
			
			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					program.addToPane(ImgRotation.rotate(program.currentImage(), Double.valueOf(means.getText()), function), means.getText() + "º_" + function);
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);
		}
	}

	public class ActGeomRotate90 implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgFixedRotation.rotate90(program.currentImage()), "90º");
		}
	}

	public class ActGeomRotate180 implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgFixedRotation.rotate180(program.currentImage()), "180º");
		}
	}

	public class ActGeomRotate270 implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(ImgFixedRotation.rotate270(program.currentImage()), "270º");
		}
	}

	public class ActGeomScale implements ActionListener {
		String function;
		public ActGeomScale(String function) {
			this.function = function;
		}
		public void actionPerformed(ActionEvent evt) {
			JTextField width = new JTextField();

			JTextField height = new JTextField();


			JFrame popup = new JFrame("Cambio de escala");
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setLayout(new GridLayout(3, 1));

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(new JLabel("Ancho"));
			panel.add(width);
			popup.add(panel);
			JPanel panel2 = new JPanel(new GridLayout(2, 1));
			panel2.add(new JLabel("Alto"));
			panel2.add(height);
			popup.add(panel2);

			JButton okbutton = new JButton("Ok");

			okbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					program.addToPane(ImgScale.scale(
							program.currentImage(),
							Double.valueOf(width.getText()),
							Double.valueOf(height.getText()),
							function),
							"Bilinear");
					popup.dispose();
				}
			});
			popup.add(okbutton);
			popup.pack();
			popup.setVisible(true);
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