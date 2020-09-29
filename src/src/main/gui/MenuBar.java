package main.gui;

import main.gui.actions.*;

import main.filters.geometric.*;
import main.filters.point.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	public static Menu program;
	public static JFrame frame;

	public MenuBar(Menu program) {
		this.program = program;
		this.frame = program.frame;

		// Set up a panel for the buttons
		// Create the menu bar.

		// Build the first menu.
		JMenu menu = new JMenu("Archivo");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		this.add(menu);
		menu.add(this.simpleMenuItem("Cargar", new ActLoadFile()));
		menu.add(this.simpleMenuItem("Guardar", new ActSaveFile()));

		JMenu filtros = new JMenu("Filtros");
		JMenu op_punto = new JMenu("Operaciones sobre punto");

		op_punto.add(this.simpleMenuItem("PAL", new PaneAdderAction(() -> Monochrome.renderPal(program.currentImage()), "PAL")));
		op_punto.add(this.simpleMenuItem("Negativo", new PaneAdderAction(() -> Negative.render(program.currentImage()), "Negativa")));
		op_punto.add(this.simpleMenuItem("Ecualizacion", new PaneAdderAction(() -> LinealTransform.ecualize(program.currentImage(), program.currentHist()), "Ecualizada")));
		op_punto.add(this.simpleMenuItem("Correccion de gamma", new ActImageGammaCorrection()));
		op_punto.add(this.simpleMenuItem("K-means", new ActFilterImageKmeans()));
		op_punto.add(this.simpleMenuItem("Perfil de imagen", new SimpleMenuAction(__ -> program.crossSectionFlag = true)));

		JMenu digitalization = new JMenu("Operaciones sobre punto");
		digitalization.add(this.simpleMenuItem("Digitalizacion por shifteo", new ActFilterImageDepthShift()));
		digitalization.add(this.simpleMenuItem("Digitalizacion por region", new ActFilterImageDepthRegion()));

		op_punto.add(digitalization);

		op_punto.add(this.simpleMenuItem("Cambio brillo-contraste", new ActChangeBC()));
		op_punto.add(this.simpleMenuItem("Especificacion de histograma", new ActImgHistSpec()));
		op_punto.add(this.simpleMenuItem("Diferencia de imagen", new ActImgDifference()));
		op_punto.add(this.simpleMenuItem("Regi�n de inter�s", new SimpleMenuAction(__ -> program.subimageFlag = true)));
		op_punto.add(this.simpleMenuItem("Transformaci�n lineal", new ActLinealTransform()));

		filtros.add(op_punto);

		JMenu op_geom = new JMenu("Operaciones geometricas");

		JMenu espejo = new JMenu("Operaciones de espejo");
		espejo.add(this.simpleMenuItem("Espejo vertical", new PaneAdderAction(() -> Mirror.vertical(program.currentImage()), "Vertical")));
		espejo.add(this.simpleMenuItem("Espejo horizontal", new PaneAdderAction(() -> Mirror.horizontal(program.currentImage()), "Horizontal")));
		espejo.add(this.simpleMenuItem("Transpuesta", new PaneAdderAction(() -> Transpose.transpose(program.currentImage()), "Transpuesta")));

		JMenu f_rotations = new JMenu("Rotaciones fixadas");
		f_rotations.add(this.simpleMenuItem("Rotar 90", new PaneAdderAction(() -> FixedRotation.rotate90(program.currentImage()), "90")));
		f_rotations.add(this.simpleMenuItem("Rotar 180", new PaneAdderAction(() -> FixedRotation.rotate180(program.currentImage()), "180")));
		f_rotations.add(this.simpleMenuItem("Rotar 270", new PaneAdderAction(() -> FixedRotation.rotate270(program.currentImage()), "270")));

		espejo.add(f_rotations);

		op_geom.add(espejo);

		JMenu rotar = new JMenu("Rotar");
		rotar.add(this.simpleMenuItem("Vecino mas proximo", new ActGeomRotate("Neighbour")));
		rotar.add(this.simpleMenuItem("Bilinear", new ActGeomRotate("Bilinear")));
		rotar.add(this.simpleMenuItem("Vecino mas proximo Directa", new ActGeomRotateDirect("Neighbour")));
		rotar.add(this.simpleMenuItem("Bilinear Directa", new ActGeomRotateDirect("Bilinear")));

		op_geom.add(rotar);

		JMenu scale = new JMenu("Cambio de escala");
		scale.add(this.simpleMenuItem("Vecino mas proximo", new ActGeomScale("Neighbour")));
		scale.add(this.simpleMenuItem("Bilinear", new ActGeomScale("Bilinear")));

		op_geom.add(scale);

		filtros.add(op_geom);

		this.add(filtros);

		JMenu menu3 = new JMenu("Grafos");
		menu3.add(this.simpleMenuItem("Histograma", new SimpleMenuAction(__ -> program.currentHist().histogram())));
		menu3.add(this.simpleMenuItem("Histograma acumulado", new SimpleMenuAction(__ -> program.currentHist().histogramAcc())));
		menu3.add(this.simpleMenuItem("Histograma normalizado", new SimpleMenuAction(__ -> program.currentHist().normHistogram())));
		menu3.add(this.simpleMenuItem("Histograma normalizado acumulado", new SimpleMenuAction(__ -> program.currentHist().normHistogramAcc())));

		this.add(menu3);

	}

	private JMenuItem simpleMenuItem(String name, ActionListener action){
		JMenuItem menuItem = new JMenuItem(name);		
		menuItem.addActionListener(action);
		return menuItem;
	}

	public class PaneAdderAction implements ActionListener {
		Supplier<BufferedImage> method;
		String tabName;

		public PaneAdderAction(Supplier<BufferedImage> method, String tabName) {
			super();
			this.method = method;
			this.tabName = tabName;
		}
		public void actionPerformed(ActionEvent evt) {
			program.addToPane(this.method.get(), this.tabName);
		}
	}

	public class SimpleMenuAction implements ActionListener {
		Consumer<Void> method;

		public SimpleMenuAction(Consumer<Void> method) {
			super();
			this.method = method;
		}

		public void actionPerformed(ActionEvent evt) {
			this.method.accept(null);
		}
	}
}