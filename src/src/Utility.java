import java.io.File;

public class Utility {
	
	public static void getAllFiles(File curDir, String level) {
		File[] filesList = curDir.listFiles();
		
		for (File f : filesList) {
			if (f.isDirectory()) {
				System.out.println(f.getName());
				getAllFiles(f, level+" | ");
			}
			if (f.isFile()) {
				System.out.println(level + f.getName());
			}
		}
	}
}
