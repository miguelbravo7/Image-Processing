import java.awt.image.BufferedImage;

public class ImgFixedRotation {

	static public BufferedImage rotate90(BufferedImage image) {
		System.out.println("90º rotation done.");
		return ImgMirror.vertical(ImgTranspose.transpose(image));
	}

	static public BufferedImage rotate180(BufferedImage image) {
		System.out.println("180º rotation done.");
		return ImgMirror.vertical(ImgMirror.horizontal(image));
	}
	
	static public BufferedImage rotate270(BufferedImage image) {
		System.out.println("270º rotation done.");
		return ImgMirror.horizontal(ImgTranspose.transpose(image));
	}
}
