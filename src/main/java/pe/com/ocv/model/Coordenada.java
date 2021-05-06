package pe.com.ocv.model;

public class Coordenada {

	private double latitud;
	private double longitud;

	public Coordenada() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Coordenada(double latitud, double longitud) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

}
