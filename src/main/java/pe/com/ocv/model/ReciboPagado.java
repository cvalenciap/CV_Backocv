package pe.com.ocv.model;

import java.sql.Date;

public class ReciboPagado extends Recibo {

	private double cobrado;
	private String fecha_pago;
	private String nom_agencia;
	private String lugar_pago;
	private String forma_pago;
	private String hora_pago;
	private String estado;
	private String est_rec;

	public ReciboPagado() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReciboPagado(int nis_rad, int sec_nis, int sec_rec, String f_fact, Date mes, String vencimiento,
			String tipo_recibo, String recibo, String nro_factura, double total_fact) {
		super(nis_rad, sec_nis, sec_rec, f_fact, mes, vencimiento, tipo_recibo, recibo, nro_factura, total_fact);
		// TODO Auto-generated constructor stub
	}

	public ReciboPagado(double cobrado, String fecha_pago, String nom_agencia, String lugar_pago, String forma_pago,
			String hora_pago, String estado, String est_rec) {
		super();
		this.cobrado = cobrado;
		this.fecha_pago = fecha_pago;
		this.nom_agencia = nom_agencia;
		this.lugar_pago = lugar_pago;
		this.forma_pago = forma_pago;
		this.hora_pago = hora_pago;
		this.estado = estado;
		this.est_rec = est_rec;
	}

	public double getCobrado() {
		return cobrado;
	}

	public void setCobrado(double cobrado) {
		this.cobrado = cobrado;
	}

	public String getFecha_pago() {
		return fecha_pago;
	}

	public void setFecha_pago(String fecha_pago) {
		this.fecha_pago = fecha_pago;
	}

	public String getNom_agencia() {
		return nom_agencia;
	}

	public void setNom_agencia(String nom_agencia) {
		this.nom_agencia = nom_agencia;
	}

	public String getLugar_pago() {
		return lugar_pago;
	}

	public void setLugar_pago(String lugar_pago) {
		this.lugar_pago = lugar_pago;
	}

	public String getForma_pago() {
		return forma_pago;
	}

	public void setForma_pago(String forma_pago) {
		this.forma_pago = forma_pago;
	}

	public String getHora_pago() {
		return hora_pago;
	}

	public void setHora_pago(String hora_pago) {
		this.hora_pago = hora_pago;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEst_rec() {
		return est_rec;
	}

	public void setEst_rec(String est_rec) {
		this.est_rec = est_rec;
	}
	


}
