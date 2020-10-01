package main.gui;

import main.gui.actions.*;

import main.filters.geometric.*;
import main.filters.point.*;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public final class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	public static Menu program;

	public MenuBar(Menu program) {
		this.program = program;

		JMenu menu = new JMenu("Archivo");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		this.add(menu);
		menu.add(this.simpleMenuItem("Cargar", new ActLoadFile()));
		menu.add(this.simpleMenuItem("Guardar", new ActSaveFile()));

		JMenu filtros = new JMenu("Filtros");
		JMenu pointOperations = new JMenu("Operaciones sobre punto");

		pointOperations.add(this.simpleMenuItem("PAL", new PaneAdderAction(() -> Monochrome.renderPal(program.currentImage()), "PAL")));
		pointOperations.add(this.simpleMenuItem("Negativo", new PaneAdderAction(() -> Negative.render(program.currentImage()), "Negativa")));
		pointOperations.add(this.simpleMenuItem("Ecualizacion", new PaneAdderAction(() -> LinealTransform.ecualize(program.currentImage(), program.currentHist()), "Ecualizada")));
		pointOperations.add(this.simpleMenuItem("Correccion de gamma", new ActImageGammaCorrection()));
		pointOperations.add(this.simpleMenuItem("K-means", new ActFilterImageKmeans()));
		pointOperations.add(this.simpleMenuItem("Perfil de imagen", new SimpleMenuAction(unused -> Menu.unpressAction = panel -> CrossSection.profile(panel.image, new Point(panel.xPressed, panel.yPressed), new Point(panel.xReleased, panel.yReleased)))));
		
		JMenu digitalization = new JMenu("Operaciones sobre punto");
		digitalization.add(this.simpleMenuItem("Digitalizacion por shifteo", new ActFilterImageDepthShift()));
		digitalization.add(this.simpleMenuItem("Digitalizacion por region", new ActFilterImageDepthRegion()));
		
		pointOperations.add(digitalization);
		
		pointOperations.add(this.simpleMenuItem("Cambio brillo-contraste", new ActChangeBC()));
		pointOperations.add(this.simpleMenuItem("Especificacion de histograma", new ActImgHistSpec()));
		pointOperations.add(this.simpleMenuItem("Diferencia de imagen", new ActImgDifference()));
		pointOperations.add(this.simpleMenuItem("Region de interes", new SimpleMenuAction(unused -> Menu.unpressAction = panel -> program.addToPane(panel.image.getSubimage(panel.xPressed, panel.yPressed, panel.xReleased - panel.xPressed, panel.yReleased - panel.yPressed), "Imagen recortada"))));
		pointOperations.add(this.simpleMenuItem("Transformacion lineal", new ActLinealTransform()));

		filtros.add(pointOperations);

		JMenu geomOperations = new JMenu("Operaciones geometricas");

		JMenu espejo = new JMenu("Operaciones de espejo");
		espejo.add(this.simpleMenuItem("Espejo vertical", new PaneAdderAction(() -> Mirror.vertical(program.currentImage()), "Vertical")));
		espejo.add(this.simpleMenuItem("Espejo horizontal", new PaneAdderAction(() -> Mirror.horizontal(program.currentImage()), "Horizontal")));
		espejo.add(this.simpleMenuItem("Transpuesta", new PaneAdderAction(() -> Transpose.transpose(program.currentImage()), "Transpuesta")));

		JMenu fRotations = new JMenu("Rotaciones fixadas");
		fRotations.add(this.simpleMenuItem("Rotar 90", new PaneAdderAction(() -> FixedRotation.rotate90(program.currentImage()), "90")));
		fRotations.add(this.simpleMenuItem("Rotar 180", new PaneAdderAction(() -> FixedRotation.rotate180(program.currentImage()), "180")));
		fRotations.add(this.simpleMenuItem("Rotar 270", new PaneAdderAction(() -> FixedRotation.rotate270(program.currentImage()), "270")));

		espejo.add(fRotations);

		geomOperations.add(espejo);

		JMenu rotar = new JMenu("Rotar");
		rotar.add(this.simpleMenuItem("Vecino mas proximo", new ActGeomRotate("Neighbour")));
		rotar.add(this.simpleMenuItem("Bilinear", new ActGeomRotate("Bilinear")));
		rotar.add(this.simpleMenuItem("Vecino mas proximo Directa", new ActGeomRotateDirect("Neighbour")));
		rotar.add(this.simpleMenuItem("Bilinear Directa", new ActGeomRotateDirect("Bilinear")));

		geomOperations.add(rotar);

		JMenu scale = new JMenu("Cambio de escala");
		scale.add(this.simpleMenuItem("Vecino mas proximo", new ActGeomScale("Neighbour")));
		scale.add(this.simpleMenuItem("Bilinear", new ActGeomScale("Bilinear")));

		geomOperations.add(scale);

		filtros.add(geomOperations);

		this.add(filtros);

		JMenu menu3 = new JMenu("Grafos");
		menu3.add(this.simpleMenuItem("Histograma", new SimpleMenuAction(unused -> program.currentHist().histogram())));
		menu3.add(this.simpleMenuItem("Histograma acumulado", new SimpleMenuAction(unused -> program.currentHist().histogramAcc())));
		menu3.add(this.simpleMenuItem("Histograma normalizado", new SimpleMenuAction(unused -> program.currentHist().normHistogram())));
		menu3.add(this.simpleMenuItem("Histograma normalizado acumulado", new SimpleMenuAction(unused -> program.currentHist().normHistogramAcc())));

		this.add(menu3);

	}

	private JMenuItem simpleMenuItem(String name, ActionListener action){
		JMenuItem menuItem = new JMenuItem(name);		
		menuItem.addActionListener(action);
		return menuItem;
	}

	private final class PaneAdderAction implements ActionListener {
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

	private final class SimpleMenuAction implements ActionListener {
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