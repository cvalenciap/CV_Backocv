package pe.com.ocv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class Suministro {

	private int nis_rad;
	private int cod_cli;
	private int sec_cta;
	private String est_sum;
	private String direccion;
	private Double total_deuda;
	private String nom_cliente;

	public Suministro() {
		super();
	}

	public Suministro(int nis_rad, int cod_cli, int sec_cta, String est_sum, String direccion, Double total_deuda,
			String nom_cliente) {
		super();
		this.nis_rad = nis_rad;
		this.cod_cli = cod_cli;
		this.sec_cta = sec_cta;
		this.est_sum = est_sum;
		this.direccion = direccion;
		this.total_deuda = total_deuda;
		this.nom_cliente = nom_cliente;
	}

	public int getNis_rad() {
		return nis_rad;
	}

	public void setNis_rad(int nis_rad) {
		this.nis_rad = nis_rad;
	}

	public int getCod_cli() {
		return cod_cli;
	}

	public void setCod_cli(int cod_cli) {
		this.cod_cli = cod_cli;
	}

	public int getSec_cta() {
		return sec_cta;
	}

	public void setSec_cta(int sec_cta) {
		this.sec_cta = sec_cta;
	}

	public String getEst_sum() {
		return est_sum;
	}

	public void setEst_sum(String est_sum) {
		this.est_sum = est_sum;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Double getTotal_deuda() {
		return total_deuda;
	}

	public void setTotal_deuda(Double total_deuda) {
		this.total_deuda = total_deuda;
	}

	public String getNom_cliente() {
		return nom_cliente;
	}

	public void setNom_cliente(String nom_cliente) {
		this.nom_cliente = nom_cliente;
	}

}