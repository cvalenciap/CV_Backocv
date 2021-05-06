package pe.com.ocv.model;

import java.sql.Date;

public class ReciboPendiente extends Recibo {

	private double deuda;
	private double volumen;
	private boolean select;

	public ReciboPendiente() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReciboPendiente(int nis_rad, int sec_nis, int sec_rec, String f_fact, Date mes, String vencimiento,
			String tipo_recibo, String recibo, String nro_factura, double total_fact) {
		super(nis_rad, sec_nis, sec_rec, f_fact, mes, vencimiento, tipo_recibo, recibo, nro_factura, total_fact);
		// TODO Auto-generated constructor stub
	}

	public ReciboPendiente(double deuda, double volumen, boolean select) {
		super();
		this.deuda = deuda;
		this.volumen = volumen;
		this.select = select;
	}

	public double getDeuda() {
		return deuda;
	}

	public void setDeuda(double deuda) {
		this.deuda = deuda;
	}

	public double getVolumen() {
		return volumen;
	}

	public void setVolumen(double volumen) {
		this.volumen = volumen;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

}
