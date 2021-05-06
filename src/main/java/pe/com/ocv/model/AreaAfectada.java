package pe.com.ocv.model;

public class AreaAfectada {

	private String tipo_area;
	private String desc_area;

	public AreaAfectada() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AreaAfectada(String tipo_area, String desc_area) {
		super();
		this.tipo_area = tipo_area;
		this.desc_area = desc_area;
	}

	public String getTipo_area() {
		return tipo_area;
	}

	public void setTipo_area(String tipo_area) {
		this.tipo_area = tipo_area;
	}

	public String getDesc_area() {
		return desc_area;
	}

	public void setDesc_area(String desc_area) {
		this.desc_area = desc_area;
	}

}
