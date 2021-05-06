package pe.com.ocv.model;

public class Sucursal {

	private String nom_recaudador;
	private String dir_recaudador;
	private String distrito;
	private String tip_recaudador;
	private double latitud;
	private double longitud;

	public Sucursal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Sucursal(String nom_recaudador, String dir_recaudador, String distrito, String tip_recaudador,
			double latitud, double longitud) {
		super();
		this.nom_recaudador = nom_recaudador;
		this.dir_recaudador = dir_recaudador;
		this.distrito = distrito;
		this.tip_recaudador = tip_recaudador;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public String getNom_recaudador() {
		return nom_recaudador;
	}

	public void setNom_recaudador(String nom_recaudador) {
		this.nom_recaudador = nom_recaudador;
	}

	public String getDir_recaudador() {
		return dir_recaudador;
	}

	public void setDir_recaudador(String dir_recaudador) {
		this.dir_recaudador = dir_recaudador;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getTip_recaudador() {
		return tip_recaudador;
	}

	public void setTip_recaudador(String tip_recaudador) {
		this.tip_recaudador = tip_recaudador;
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
