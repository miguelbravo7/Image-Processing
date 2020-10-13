package main.filters.point.kmeans;

import java.util.HashSet;
import java.util.Set;

import main.utils.Pixel;

public class Cluster<T1 extends Number, T2 extends Number> {
	private Set<Pixel<T1>> puntos = new HashSet<>();
	private Pixel<T2> centroide;
	private int pointCount = 0;
	private boolean termino = false;

	public Pixel<T2> getCentroide() {
		return centroide;
	}

	public void setCentroide(Pixel<T2> centroide) {
		this.centroide = centroide;
	}

	public int getGrado() {
		return centroide.getGrado();
	}

	public Set<Pixel<T1>> getPuntos() {
		return puntos;
	}

	public void addPunto(Pixel<T1> punto) {
		puntos.add(punto);
		pointCount++;
	}

	public Integer numPuntos() {
		return pointCount;
	}

	public boolean isTermino() {
		return termino;
	}

	public void setTermino(boolean termino) {
		this.termino = termino;
	}

	public void limpiarPuntos() {
		puntos.clear();
		pointCount = 0;
	}

	public boolean contains(Pixel<T1> punto) {
		return puntos.contains(punto);
	}

	@Override
	public String toString() {
		return centroide.toString();
	}
}
