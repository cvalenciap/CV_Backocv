package pe.com.ocv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Respuesta {

	private int nRESP_SP;
	private String cRESP_SP;
	private String cRESP_SP2;
	private Object bRESP;
	@JsonInclude(Include.NON_DEFAULT)
	private int total;
	@JsonInclude(Include.NON_DEFAULT)
	private double comisionVisa;

	public Respuesta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Respuesta(int nRESP_SP, String cRESP_SP, String cRESP_SP2, Object bRESP, int total, double comisionVisa) {
		super();
		this.nRESP_SP = nRESP_SP;
		this.cRESP_SP = cRESP_SP;
		this.cRESP_SP2 = cRESP_SP2;
		this.bRESP = bRESP;
		this.total = total;
		this.comisionVisa = comisionVisa;
	}

	public Respuesta(int nRESP_SP, String cRESP_SP) {
		super();
		this.nRESP_SP = nRESP_SP;
		this.cRESP_SP = cRESP_SP;
	}

	public int getnRESP_SP() {
		return nRESP_SP;
	}

	public void setnRESP_SP(int nRESP_SP) {
		this.nRESP_SP = nRESP_SP;
	}

	public String getcRESP_SP() {
		return cRESP_SP;
	}

	public void setcRESP_SP(String cRESP_SP) {
		this.cRESP_SP = cRESP_SP;
	}
	
	public String getcRESP_SP2() {
		return cRESP_SP2;
	}
	
	public void setcRESP_SP2(String cRESP_SP2) {
		this.cRESP_SP2 = cRESP_SP2;
	}

	public Object getbRESP() {
		return bRESP;
	}

	public void setbRESP(Object bRESP) {
		this.bRESP = bRESP;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public double getComisionVisa() {
		return comisionVisa;
	}
	
	public void setComisionVisa(double comisionVisa) {
		this.comisionVisa = comisionVisa;
	}

}
