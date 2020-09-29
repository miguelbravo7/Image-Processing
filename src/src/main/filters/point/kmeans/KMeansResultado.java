package main.filters.point.kmeans;

import java.util.ArrayList;
import java.util.List;

import main.utils.Pixel;

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

	public Pixel getCentroide(Pixel punto) {
		int clusterIndex = -1;

		while (!clusters.get(++clusterIndex).contains(punto))
			;

		return clusters.get(clusterIndex).getCentroide();
	}

	public Double getOfv() {
		return ofv;
	}
}
