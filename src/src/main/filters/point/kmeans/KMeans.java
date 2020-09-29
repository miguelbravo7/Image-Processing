package main.filters.point.kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.utils.Pixel;

public class KMeans {

	public KMeansResultado calcular(List<Pixel> puntos, Integer k) {
		List<Cluster> clusters = elegirCentroides(puntos, k);

		while (!finalizo(clusters)) {
			prepararClusters(clusters);
			asignarPuntos(puntos, clusters);
			recalcularCentroides(clusters);
		}

		Double ofv = calcularFuncionObjetivo(clusters);

		return new KMeansResultado(clusters, ofv);
	}

	/**
	 * Metodo que inicialica los centroides en su posicion inicial
	 * 
	 * @param puntos
	 * @param k(numero de clases)
	 * @return centroides
	 */
	private List<Cluster> elegirCentroides(List<Pixel> puntos, Integer k) {
		ArrayList<Cluster> centroides = new ArrayList<Cluster>();

		ArrayList<Float> maximos = new ArrayList<Float>();
		ArrayList<Float> minimos = new ArrayList<Float>();
		// se buscan los maximos y minimos de cada dimension

		for (int i = 0; i < puntos.get(0).getGrado(); i++) {
			Float min = 255f;
			Float max = 0f;

			for (Pixel punto : puntos) {
				min = min > punto.get(i) ? punto.get(0) : min;
				max = max < punto.get(i) ? punto.get(i) : max;
			}

			maximos.add(max);
			minimos.add(min);
		}

		// Se generan los clusters dentro de la region en la que estan situados los
		// puntos
		Random random = new Random();

		for (int i = 0; i < k; i++) {
			Float[] data = new Float[puntos.get(0).getGrado()];
			Arrays.fill(data, 0f);
			for (int d = 0; d < puntos.get(0).getGrado(); d++) {
				data[d] = random.nextFloat() * (maximos.get(d) - minimos.get(d)) + minimos.get(d);
			}

			Cluster c = new Cluster();
			Pixel centroide = new Pixel(data);
			c.setCentroide(centroide);
			centroides.add(c);
		}

		return centroides;
	}

	/**
	 * Metodo para comprobar si todos los clusters estan establecidos
	 * 
	 * @param clusters
	 * @return boolean (posicion final en todos los clusters)
	 */
	private boolean finalizo(List<Cluster> clusters) {
		for (Cluster cluster : clusters) {
			if (!cluster.isTermino()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Metodo que vacia los puntos asignados anteriormente al cluster
	 * 
	 * @param clusters
	 */
	private void prepararClusters(List<Cluster> clusters) {
		for (Cluster c : clusters) {
			c.limpiarPuntos();
		}
	}

	/**
	 * Metodo que para cada punto le asigna el cluster mas cercano
	 * 
	 * @param puntos
	 * @param clusters
	 */
	private void asignarPuntos(List<Pixel> puntos, List<Cluster> clusters) {
		for (Pixel punto : puntos) {
			Cluster masCercano = clusters.get(0);
			Double distanciaMinima = Double.MAX_VALUE;
			for (Cluster cluster : clusters) {
				Double distancia = punto.distanciaEuclideana(cluster.getCentroide());
				if (distanciaMinima > distancia) {
					distanciaMinima = distancia;
					masCercano = cluster;
				}
			}
			masCercano.getPuntos().add(punto);
		}
	}

	/**
	 * Metodo que evalua que posicion nueva se situara el cluster y se ha dejado de
	 * mover
	 * 
	 * @param clusters
	 */
	private void recalcularCentroides(List<Cluster> clusters) {
		for (Cluster c : clusters) {
			if (c.getPuntos().isEmpty()) {
				c.setTermino(true);
				continue;
			}

			Float[] d = new Float[4];
			Arrays.fill(d, 0f);

			for (Pixel p : c.getPuntos()) {
				for (int i = 0; i < p.getGrado(); i++) {
					d[i] += (p.get(i) / c.getPuntos().size());
				}
			}

			// Establecido el nuevo punto se compara con el anterior para ver si se ha
			// movido
			Pixel nuevoCentroide = new Pixel(d);

			if (nuevoCentroide.equals(c.getCentroide())) {
				c.setTermino(true);
			} else {
				c.setCentroide(nuevoCentroide);
			}
		}
	}

	/**
	 * Metodo que calcula el error acumulado
	 * 
	 * @param clusters
	 * @return double con err. acumulado
	 */
	private Double calcularFuncionObjetivo(List<Cluster> clusters) {
		Double ofv = 0d;

		for (Cluster cluster : clusters) {
			for (Pixel punto : cluster.getPuntos()) {
				ofv += punto.distanciaEuclideana(cluster.getCentroide());
			}
		}

		return ofv;
	}

}
