
public class Pixel {
	private Float[] data;
	/**
	 * Constructor de la clase Pixel
	 * @param strings
	 */
	public Pixel(Float[] data) {
		this.data = data;
	}

	/**
	 * 0 - alpha
	 * 1 - red
	 * 2 - green
	 * 3 - blue
	 */
	public float get(int index) {
		return data[index];
	}
	
	public int alpha() {
		return data[0].intValue();
	}
	
	public int red() {
		return data[1].intValue();
	}

	public int green() {
		return data[2].intValue();
	}

	public int blue() {
		return data[3].intValue();
	}

	/**
	 * Metododo para obtener la dimension
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
	
	//Funciones de ayuda

	/**
	 * Funcion de ayuda para obtener la distancia euclidiana
	 * @param destino
	 * @return flotante de precision doble
	 */
	public Double distanciaEuclideana(Pixel destino) {
		Double d = 0d;
		for (int i = 0; i < data.length; i++) {
			d += Math.pow(data[i] - destino.get(i), 2);
		}
		return Math.sqrt(d);
	}

	/**
	 * Metodo que compara las coordenadas del punto
	 * @return booleano
	 */	
	@Override
	public boolean equals(Object obj) {
		Pixel other = (Pixel) obj;
		for (int i = 0; i < data.length; i++) {
			if (data[i] != other.get(i)) {
				return false;
			}
		}
		return true;
	}
}