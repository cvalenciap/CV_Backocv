package pe.com.ocv.model;

import java.util.ArrayList;
import java.util.List;

public class Municipio {

	private int cod_municipio;
	private String nom_municipio;
	private int nro_incidencias;
	private boolean mapa;
	private List<Incidencia> incidencias;

	public Municipio() {
		super();
		// TODO Auto-generated constructor stub
		incidencias = new ArrayList<Incidencia>();
	}

	public Municipio(int cod_municipio, String nom_municipio, int nro_incidencias, boolean mapa, List<Incidencia> incidencias) {
		super();
		this.cod_municipio = cod_municipio;
		this.nom_municipio = nom_municipio;
		this.nro_incidencias = nro_incidencias;
		this.mapa = mapa;
		this.incidencias = incidencias;
	}

	public int getCod_municipio() {
		return cod_municipio;
	}

	public void setCod_municipio(int cod_municipio) {
		this.cod_municipio = cod_municipio;
	}

	public String getNom_municipio() {
		return nom_municipio;
	}

	public void setNom_municipio(String nom_municipio) {
		this.nom_municipio = nom_municipio;
	}

	public int getNro_incidencias() {
		return nro_incidencias;
	}

	public void setNro_incidencias(int nro_incidencias) {
		this.nro_incidencias = nro_incidencias;
	}
	
	public boolean isMapa() {
		return mapa;
	}
	
	public void setMapa(boolean mapa) {
		this.mapa = mapa;
	}

	public List<Incidencia> getIncidencias() {
		return incidencias;
	}

	public void setIncidencias(List<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}

}
