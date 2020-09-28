package main.filters.geometric;

import java.awt.image.BufferedImage;

public class ImgFixedRotation {

	static public BufferedImage rotate90(BufferedImage image) {
		System.out.println("90� rotation done.");
		return ImgMirror.vertical(ImgTranspose.transpose(image));
	}

	static public BufferedImage rotate180(BufferedImage image) {
		System.out.println("180� rotation done.");
		return ImgMirror.vertical(ImgMirror.horizontal(image));
	}
	
	static public BufferedImage rotate270(BufferedImage image) {
		System.out.println("270� rotation done.");
		return ImgMirror.horizontal(ImgTranspose.transpose(image));
	}
}
