package main.filters.point.kmeans;

import java.util.ArrayList;
import java.util.List;

import main.utils.Pixel;

public class KMeansResultado<T1 extends Number, T2 extends Number> {
	private List<Cluster<T1, T2>> clusters = new ArrayList<>();
	private Double ofv;

	public KMeansResultado(List<Cluster<T1, T2>> clusters, Double ofv) {
		this.ofv = ofv;
		this.clusters = clusters;
	}

	public List<Cluster<T1, T2>> getClusters() {
		return clusters;
	}

	public Pixel<T2> getCentroide(Pixel<T1> punto) {
		int clusterIndex = 0;

		while (!clusters.get(clusterIndex).contains(punto))
			clusterIndex++;

		return clusters.get(clusterIndex).getCentroide();
	}

	public Double getOfv() {
		return ofv;
	}
}
