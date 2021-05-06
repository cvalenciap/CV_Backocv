package pe.com.ocv.model;

public class PredioAfectado {

	private int cod_municipio;
	private int cod_incidencia;

	public PredioAfectado() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PredioAfectado(int cod_municipio, int cod_incidencia) {
		super();
		this.cod_municipio = cod_municipio;
		this.cod_incidencia = cod_incidencia;
	}

	public int getCod_municipio() {
		return cod_municipio;
	}

	public void setCod_municipio(int cod_municipio) {
		this.cod_municipio = cod_municipio;
	}

	public int getCod_incidencia() {
		return cod_incidencia;
	}

	public void setCod_incidencia(int cod_incidencia) {
		this.cod_incidencia = cod_incidencia;
	}

}
