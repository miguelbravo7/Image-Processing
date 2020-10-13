package main.filters.point.kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.utils.Pixel;

public class KMeans<T1 extends Number, T2 extends Number> {
	Random random = new Random();

	public KMeansResultado<T1, T2> calcular(List<Pixel<T1>> puntos, Integer k) {
		List<Cluster<T1, T2>> clusters = elegirCentroides(puntos, k);

		while (!finalizo(clusters)) {
			prepararClusters(clusters);
			asignarPuntos(puntos, clusters);
			recalcularCentroides(clusters);
		}

		Double ofv = calcularFuncionObjetivo(clusters);

		return new KMeansResultado<>(clusters, ofv);
	}

	/**
	 * Metodo que inicialica los centroides en su posicion inicial
	 * 
	 * @param puntos
	 * @param k(numero de clases)
	 * @return centroides
	 */
	private List<Cluster<T1, T2>> elegirCentroides(List<Pixel<T1>> puntos, Integer k) {
		ArrayList<Cluster<T1, T2>> centroides = new ArrayList<>();
		ArrayList<Float> maximos = new ArrayList<>();
		ArrayList<Float> minimos = new ArrayList<>();

		// Se buscan los maximos y minimos de cada dimension
		for (int i = 0; i < puntos.get(0).getGrado(); i++) {
			Float min = Float.MAX_VALUE;
			Float max = Float.MIN_VALUE;

			for (Pixel<T1> punto : puntos) {
				min = Math.min(punto.get(i).floatValue(), min);
				max = Math.max(punto.get(i).floatValue(), max);
			}

			maximos.add(max);
			minimos.add(min);
		}

		// Se generan los clusters dentro de la region en la que estan situados los puntos
		for (int i = 0; i < k; i++) {
			Float[] data = new Float[puntos.get(0).getGrado()];
			Arrays.fill(data, 0f);
			for (int d = 0; d < puntos.get(0).getGrado(); d++) {
				data[d] = random.nextFloat() * (maximos.get(d) - minimos.get(d)) + minimos.get(d);
			}

			Cluster<T1, T2> c = new Cluster<>();
			Pixel<T2> centroide = new Pixel<>((T2[]) data);
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
	private boolean finalizo(List<Cluster<T1, T2>> clusters) {
		for (Cluster<T1, T2> cluster : clusters) {
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
	private void prepararClusters(List<Cluster<T1, T2>> clusters) {
		for (Cluster<T1, T2> c : clusters) {
			c.limpiarPuntos();
		}
	}

	/**
	 * Metodo que para cada punto le asigna el cluster mas cercano
	 * 
	 * @param puntos
	 * @param clusters
	 */
	private void asignarPuntos(List<Pixel<T1>> puntos, List<Cluster<T1, T2>> clusters) {
		for (Pixel<T1> punto : puntos) {
			Cluster<T1, T2> masCercano = clusters.get(0);
			Double distanciaMinima = Double.MAX_VALUE;
			for (Cluster<T1, T2> cluster : clusters) {
				Double distancia = punto.distanciaEuclideana(cluster.getCentroide());
				if (distanciaMinima > distancia) {
					distanciaMinima = distancia;
					masCercano = cluster;
				}
			}
			masCercano.addPunto(punto);
		}
	}

	/**
	 * Metodo que evalua que posicion nueva se situara el cluster y se ha dejado de
	 * mover
	 * 
	 * @param clusters
	 */
	private void recalcularCentroides(List<Cluster<T1, T2>> clusters) {
		for (Cluster<T1, T2> cluster : clusters) {
			if (cluster.getPuntos().isEmpty()) {
				cluster.setTermino(true);
				continue;
			}
			Float[] accDist = new Float[cluster.getGrado()];
			Arrays.fill(accDist, 0f);

			for (Pixel<T1> p : cluster.getPuntos()) {
				for (int i = 0; i < p.getGrado(); i++) {
					accDist[i] += p.get(i).floatValue() / cluster.numPuntos();
				}
			}

			// Establecido el nuevo punto se compara con el anterior para ver si se ha
			// movido
			Pixel<T2> nuevoCentroide = new Pixel<>((T2[]) accDist);

			if (nuevoCentroide.equals(cluster.getCentroide())) {
				cluster.setTermino(true);
			} else {
				cluster.setCentroide(nuevoCentroide);
			}
		}
	}

	/**
	 * Metodo que calcula el error acumulado
	 * 
	 * @param clusters
	 * @return double con err. acumulado
	 */
	private Double calcularFuncionObjetivo(List<Cluster<T1, T2>> clusters) {
		Double ofv = 0d;
		for (Cluster<T1, T2> cluster : clusters) {
			for (Pixel<T1> punto : cluster.getPuntos()) {
				ofv += punto.distanciaEuclideana(cluster.getCentroide());
			}
		}
		return ofv;
	}

	public static <T> T castInstance(Object o, Class<T> clazz) {
		try {
			return clazz.cast(o);
		} catch (ClassCastException e) {
			return null;
		}
	}
}
