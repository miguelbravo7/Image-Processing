package main.utils;

import java.util.Arrays;
import java.util.Iterator;

public class Pixel<T extends Number> implements Iterable<T> {
	private T[] data;

	/**
	 * Constructor de la clase Pixel
	 * 
	 * @param strings
	 */
	public Pixel(T[] data2) {
		this.data = data2;
	}

	/**
	 * 0 - alpha 1 - red 2 - green 3 - blue
	 */
	public T get(int index) {
		return data[index];
	}

	public int getRGB() {
		return (data[0].intValue() << 24) | (data[1].intValue() << 16) | (data[2].intValue() << 8) | data[3].intValue();
	}

	/**
	 * Metododo para obtener la dimension
	 * 
	 * @return dimension en valor entero
	 */
	public int getGrado() {
		return data.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(data[0]);
		for (int i = 1; i < data.length; i++) {
			sb.append(", ");
			sb.append(data[i]);
		}
		return sb.toString();
	}

	/**
	 * Funcion de ayuda para obtener la distancia euclidiana
	 * 
	 * @param destino
	 * @return flotante de precision doble
	 */
	public Double distanciaEuclideana(Pixel<?> destino) {
		Double d = 0d;
		for (int i = 0; i < data.length; i++) {
			Double v1 = data[i].doubleValue();
			Double v2 = destino.get(i).doubleValue();
			d += Math.pow(v1 - v2, 2);
		}
		return Math.sqrt(d);
	}

	/**
	 * Metodo que compara las coordenadas del punto
	 * 
	 * @return booleano
	 */
	@Override
	public boolean equals(Object obj) {
		Pixel<T> other = (Pixel<T>) obj;
		for (int i = 0; i < data.length; i++) {
			if (Math.abs(data[i].floatValue() - other.get(i).floatValue()) > 0.0000001f) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		return Arrays.stream(data).iterator();
	}
}