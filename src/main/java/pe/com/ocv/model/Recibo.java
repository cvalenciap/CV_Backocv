package pe.com.ocv.model;

import java.sql.Date;

public class Recibo {

	private int nis_rad;
	private int sec_nis;
	private int sec_rec;
	private String f_fact;
	private Date mes;
	private String vencimiento;
	private String tipo_recibo;
	private String recibo;
	private String nro_factura;
	private double total_fact;

	public Recibo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Recibo(int nis_rad, int sec_nis, int sec_rec, String f_fact, Date mes, String vencimiento,
			String tipo_recibo, String recibo, String nro_factura, double total_fact) {
		super();
		this.nis_rad = nis_rad;
		this.sec_nis = sec_nis;
		this.sec_rec = sec_rec;
		this.f_fact = f_fact;
		this.mes = mes;
		this.vencimiento = vencimiento;
		this.tipo_recibo = tipo_recibo;
		this.recibo = recibo;
		this.nro_factura = nro_factura;
		this.total_fact = total_fact;
	}

	public int getNis_rad() {
		return nis_rad;
	}

	public void setNis_rad(int nis_rad) {
		this.nis_rad = nis_rad;
	}

	public int getSec_nis() {
		return sec_nis;
	}

	public void setSec_nis(int sec_nis) {
		this.sec_nis = sec_nis;
	}

	public int getSec_rec() {
		return sec_rec;
	}

	public void setSec_rec(int sec_rec) {
		this.sec_rec = sec_rec;
	}

	public String getF_fact() {
		return f_fact;
	}

	public void setF_fact(String f_fact) {
		this.f_fact = f_fact;
	}

	public Date getMes() {
		return mes;
	}

	public void setMes(Date mes) {
		this.mes = mes;
	}

	public String getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(String vencimiento) {
		this.vencimiento = vencimiento;
	}

	public String getTipo_recibo() {
		return tipo_recibo;
	}

	public void setTipo_recibo(String tipo_recibo) {
		this.tipo_recibo = tipo_recibo;
	}

	public String getRecibo() {
		return recibo;
	}

	public void setRecibo(String recibo) {
		this.recibo = recibo;
	}

	public String getNro_factura() {
		return nro_factura;
	}

	public void setNro_factura(String nro_factura) {
		this.nro_factura = nro_factura;
	}
	
	public double getTotal_fact() {
		return total_fact;
	}
	
	public void setTotal_fact(double total_fact) {
		this.total_fact = total_fact;
	}

}
