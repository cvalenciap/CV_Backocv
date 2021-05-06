package pe.com.ocv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class ConceptoPago {

	private String cod_concepto;
	private String desc_concepto;
	private double monto_concepto;

	public ConceptoPago() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConceptoPago(String cod_concepto, String desc_concepto, double monto_concepto) {
		super();
		this.cod_concepto = cod_concepto;
		this.desc_concepto = desc_concepto;
		this.monto_concepto = monto_concepto;
	}

	public String getCod_concepto() {
		return cod_concepto;
	}

	public void setCod_concepto(String cod_concepto) {
		this.cod_concepto = cod_concepto;
	}

	public String getDesc_concepto() {
		return desc_concepto;
	}

	public void setDesc_concepto(String desc_concepto) {
		this.desc_concepto = desc_concepto;
	}

	public double getMonto_concepto() {
		return monto_concepto;
	}

	public void setMonto_concepto(double monto_concepto) {
		this.monto_concepto = monto_concepto;
	}

}
