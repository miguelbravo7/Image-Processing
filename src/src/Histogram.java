import java.util.ArrayList;

public class Histogram implements Runnable{
	ArrayList<Pixel> image = new ArrayList<Pixel>();
	int[][] valores = new int[256][3];
	Integer tamaño=0, accr=0, accg=0 ,accb=0;
	
	public Histogram(ArrayList<Pixel> imagen) {
		
		this.image = (ArrayList<Pixel>) imagen.clone();
		
		Thread thread = new Thread(this, "histogram");
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	public String toString() {
		return "Media: r " + (accr/tamaño) + ",g " + (accg/tamaño) + ",b " + (accb/tamaño);
	}

	@Override
	public void run() {
		tamaño = image.size();
		
		for(Pixel pixel : image) {
			valores[(int) pixel.get(1)][0]++;
			valores[(int) pixel.get(2)][1]++;
			valores[(int) pixel.get(3)][2]++;
		}
		
		{
			int value = 0;
			for (int[] pixel : valores) {
				accr += pixel[0] * value;
				accg += pixel[1] * value;
				accb += pixel[2] * value++;
			}
		}		
		image.clear();
	}
}
