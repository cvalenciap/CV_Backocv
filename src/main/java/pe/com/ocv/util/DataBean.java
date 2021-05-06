package pe.com.ocv.util;

public class DataBean {
	private String meses;
	private Double consumo;

	public DataBean() {
		super();
	}

	public String getMeses() {
		return this.meses;
	}

	public void setMeses(final String meses) {
		this.meses = meses;
	}

	public Double getConsumo() {
		return this.consumo;
	}

	public void setConsumo(final Double consumo) {
		this.consumo = consumo;
	}
}