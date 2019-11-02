import java.awt.image.BufferedImage;

public class Start {
	
	public static void main(String[] args) {
		Menu menu = new Menu();
		menu.openImage("Windows_XP.png");
	    menu.imagetype.add(BufferedImage.TYPE_INT_ARGB);	
	}

}
