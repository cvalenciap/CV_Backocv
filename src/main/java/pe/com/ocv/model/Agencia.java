package pe.com.ocv.model;

public class Agencia {

	private int cod_agencia;
	private String nom_entidad;

	public Agencia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Agencia(int cod_agencia, String nom_entidad) {
		super();
		this.cod_agencia = cod_agencia;
		this.nom_entidad = nom_entidad;
	}

	public int getCod_agencia() {
		return cod_agencia;
	}

	public void setCod_agencia(int cod_agencia) {
		this.cod_agencia = cod_agencia;
	}

	public String getNom_entidad() {
		return nom_entidad;
	}

	public void setNom_entidad(String nom_entidad) {
		this.nom_entidad = nom_entidad;
	}

}
