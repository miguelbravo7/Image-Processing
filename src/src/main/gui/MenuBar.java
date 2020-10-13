package main.gui;

import main.gui.actions.*;
import main.utils.Utility;
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

	public MenuBar() {
		JMenu menu = new JMenu("Archivo");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		this.add(menu);
		menu.add(this.simpleMenuItem("Cargar", new ActLoadFile()));
		menu.add(this.simpleMenuItem("Guardar", new ActSaveFile()));

		JMenu filtros = new JMenu("Filtros");
		JMenu pointOperations = new JMenu("Operaciones sobre punto");

		pointOperations.add(this.simpleMenuItem("PAL", new PaneAdderAction(() -> Monochrome.renderPal(Menu.currentImage()), "PAL")));
		pointOperations.add(this.simpleMenuItem("Negativo", new PaneAdderAction(() -> Negative.render(Menu.currentImage()), "Negativa")));
		pointOperations.add(this.simpleMenuItem("Ecualizacion", new PaneAdderAction(() -> LinealTransform.ecualize(Menu.currentImage(), Menu.currentHist()), "Ecualizada")));
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
		pointOperations.add(this.simpleMenuItem("Region de interes", new SimpleMenuAction(unused -> Menu.unpressAction = panel -> Menu.addImageToPane(panel.image.getSubimage(panel.xPressed, panel.yPressed, panel.xReleased - panel.xPressed, panel.yReleased - panel.yPressed), "Imagen recortada"))));
		pointOperations.add(this.simpleMenuItem("Transformacion lineal", new ActLinealTransform()));

		filtros.add(pointOperations);

		JMenu geomOperations = new JMenu("Operaciones geometricas");

		JMenu espejo = new JMenu("Operaciones de espejo");
		espejo.add(this.simpleMenuItem("Espejo vertical", new PaneAdderAction(() -> Mirror.vertical(Menu.currentImage()), "Vertical")));
		espejo.add(this.simpleMenuItem("Espejo horizontal", new PaneAdderAction(() -> Mirror.horizontal(Menu.currentImage()), "Horizontal")));
		espejo.add(this.simpleMenuItem("Transpuesta", new PaneAdderAction(() -> Transpose.transpose(Menu.currentImage()), "Transpuesta")));

		JMenu fRotations = new JMenu("Rotaciones fixadas");
		fRotations.add(this.simpleMenuItem("Rotar 90", new PaneAdderAction(() -> FixedRotation.rotate90(Menu.currentImage()), "90")));
		fRotations.add(this.simpleMenuItem("Rotar 180", new PaneAdderAction(() -> FixedRotation.rotate180(Menu.currentImage()), "180")));
		fRotations.add(this.simpleMenuItem("Rotar 270", new PaneAdderAction(() -> FixedRotation.rotate270(Menu.currentImage()), "270")));

		espejo.add(fRotations);

		geomOperations.add(espejo);

		JMenu rotar = new JMenu("Rotar");
		rotar.add(this.simpleMenuItem("Vecino mas proximo", new ActGeomRotate(Utility.NEIGHBOUR)));
		rotar.add(this.simpleMenuItem("Bilinear", new ActGeomRotate(Utility.BILINEAR)));
		rotar.add(this.simpleMenuItem("Vecino mas proximo Directa", new ActGeomRotateDirect(Utility.NEIGHBOUR)));
		rotar.add(this.simpleMenuItem("Bilinear Directa", new ActGeomRotateDirect(Utility.BILINEAR)));

		geomOperations.add(rotar);

		JMenu scale = new JMenu("Cambio de escala");
		scale.add(this.simpleMenuItem("Vecino mas proximo", new ActGeomScale(Utility.NEIGHBOUR)));
		scale.add(this.simpleMenuItem("Bilinear", new ActGeomScale(Utility.BILINEAR)));

		geomOperations.add(scale);

		filtros.add(geomOperations);

		this.add(filtros);

		JMenu menu3 = new JMenu("Grafos");
		menu3.add(this.simpleMenuItem("Histograma", new SimpleMenuAction(unused -> Menu.currentHist().histogram())));
		menu3.add(this.simpleMenuItem("Histograma acumulado", new SimpleMenuAction(unused -> Menu.currentHist().histogramAcc())));
		menu3.add(this.simpleMenuItem("Histograma normalizado", new SimpleMenuAction(unused -> Menu.currentHist().normHistogram())));
		menu3.add(this.simpleMenuItem("Histograma normalizado acumulado", new SimpleMenuAction(unused -> Menu.currentHist().normHistogramAcc())));

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
			Menu.addImageToPane(this.method.get(), this.tabName);
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