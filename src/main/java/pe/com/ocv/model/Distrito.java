package pe.com.ocv.model;

public class Distrito {
	private int codDist;
	private String desDist;

	public Distrito() {
		super();
	}

	public Distrito(int cod_Dist, String des_Dist) {
		super();
		this.codDist = cod_Dist;
		this.desDist = des_Dist;
	}

	public int getCodDist() {
		return codDist;
	}

	public void setCodDist(int cod_Dist) {
		this.codDist = cod_Dist;
	}

	public String getDesDist() {
		return desDist;
	}

	public void setDesDist(String des_Dist) {
		this.desDist = des_Dist;
	}

}
