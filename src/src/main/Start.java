package main;

import main.gui.Menu;

public class Start {

	public static void main(String[] args) {
		Menu menu = new Menu();
		menu.openImage("src/Windows_XP.png");
		menu.openImage("src/tanque-anterior.tiff");
		menu.openImage("src/tanque-posterior.tiff");
	}

}