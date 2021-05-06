package pe.com.ocv.model;

import java.util.ArrayList;
import java.util.List;

public class Incidencia {

	private int cod_incidencia;
	private String nom_incidencia;
	private String tipo_incidencia;
	private String estado_incidencia;
	private String fecha_inicio;
	private String fecha_estimada_sol;
	private String observacion;
	private int nis_rad;
	private double latitud;
	private double longitud;
	private List<AreaAfectada> areas_afectadas;

	public Incidencia() {
		super();
		// TODO Auto-generated constructor stub
		this.areas_afectadas = new ArrayList<AreaAfectada>();
	}

	public Incidencia(int cod_incidencia, String nom_incidencia, String tipo_incidencia, String estado_incidencia,
			String fecha_inicio, String fecha_estimada_sol, String observacion, int nis_rad, double latitud, double longitud,
			List<AreaAfectada> areas_afectadas) {
		super();
		this.cod_incidencia = cod_incidencia;
		this.nom_incidencia = nom_incidencia;
		this.tipo_incidencia = tipo_incidencia;
		this.estado_incidencia = estado_incidencia;
		this.fecha_inicio = fecha_inicio;
		this.fecha_estimada_sol = fecha_estimada_sol;
		this.observacion = observacion;
		this.nis_rad = nis_rad;
		this.areas_afectadas = areas_afectadas;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public int getCod_incidencia() {
		return cod_incidencia;
	}

	public void setCod_incidencia(int cod_incidencia) {
		this.cod_incidencia = cod_incidencia;
	}

	public String getNom_incidencia() {
		return nom_incidencia;
	}

	public void setNom_incidencia(String nom_incidencia) {
		this.nom_incidencia = nom_incidencia;
	}

	public String getTipo_incidencia() {
		return tipo_incidencia;
	}

	public void setTipo_incidencia(String tipo_incidencia) {
		this.tipo_incidencia = tipo_incidencia;
	}

	public String getEstado_incidencia() {
		return estado_incidencia;
	}

	public void setEstado_incidencia(String estado_incidencia) {
		this.estado_incidencia = estado_incidencia;
	}

	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getFecha_estimada_sol() {
		return fecha_estimada_sol;
	}

	public void setFecha_estimada_sol(String fecha_estimada_sol) {
		this.fecha_estimada_sol = fecha_estimada_sol;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public int getNis_rad() {
		return nis_rad;
	}

	public void setNis_rad(int nis_rad) {
		this.nis_rad = nis_rad;
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

	public List<AreaAfectada> getAreas_afectadas() {
		return areas_afectadas;
	}

	public void setAreas_afectadas(List<AreaAfectada> areas_afectadas) {
		this.areas_afectadas = areas_afectadas;
	}

}
