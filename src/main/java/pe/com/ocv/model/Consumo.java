package pe.com.ocv.model;

public class Consumo {

	private String mes_fact;
	private double volumen;
	private double monto;

	public Consumo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Consumo(String mes_fact, double volumen, double monto) {
		super();
		this.mes_fact = mes_fact;
		this.volumen = volumen;
		this.monto = monto;
	}

	public String getMes_fact() {
		return mes_fact;
	}

	public void setMes_fact(String mes_fact) {
		this.mes_fact = mes_fact;
	}

	public double getVolumen() {
		return volumen;
	}

	public void setVolumen(double volumen) {
		this.volumen = volumen;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

}
