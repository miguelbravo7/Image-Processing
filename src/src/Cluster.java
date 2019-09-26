import java.util.HashSet;
import java.util.Set;

public class Cluster {
	private Set<Pixel> puntos = new HashSet<Pixel>();
	private Pixel centroide;
	private boolean termino = false;

	public Pixel getCentroide() {
		return centroide;
	}

	public void setCentroide(Pixel centroide) {
		this.centroide = centroide;
	}

	public Set<Pixel> getPuntos() {
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
