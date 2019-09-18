import java.util.ArrayList;
import java.util.List;

public class KMeansResultado {
	private List<Cluster> clusters = new ArrayList<Cluster>();
	private Double ofv;

	public KMeansResultado(List<Cluster> clusters, Double ofv) {
		this.ofv = ofv;
		this.clusters = clusters;
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	public Punto getCentroide(Punto punto) {
		int cluster_index = -1;
		
		while(!clusters.get(++cluster_index).contains(punto));
			
		return clusters.get(cluster_index).getCentroide();
	}

	public Double getOfv() {
		return ofv;
	}
}
