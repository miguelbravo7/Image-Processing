import java.util.ArrayList;
import java.util.List;

public class Cluster {
	private List<Pixel> puntos = new ArrayList<Pixel>();
	private Pixel centroide;
	private boolean termino = false;

	public Pixel getCentroide() {
		return centroide;
	}

	public void setCentroide(Pixel centroide) {
		this.centroide = centroide;
	}

	public List<Pixel> getPuntos() {
		return puntos;
	}

	public boolean isTermino() {
		return termino;
	}

	public void setTermino(boolean termino) {
		this.termino = termino;
	}

	public void limpiarPuntos() {
		puntos.clear();
	}
	
	public boolean contains(Pixel punto) {
		return puntos.contains(punto);
	}

	@Override
	public String toString() {
		return centroide.toString();
	}
}
