package pe.com.ocv.model;

public class DatosLogin {

	private int id_cliente;
	private int nis_rad;
	private int admin_com;
	private int admin_etic;
	private String flag_respuesta;

	public DatosLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DatosLogin(int id_cliente, int nis_rad, int admin_com, int admin_etic, String flag_respuesta) {
		super();
		this.id_cliente = id_cliente;
		this.nis_rad = nis_rad;
		this.admin_com = admin_com;
		this.admin_etic = admin_etic;
		this.flag_respuesta = flag_respuesta;
	}

	public int getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}

	public int getNis_rad() {
		return nis_rad;
	}

	public void setNis_rad(int nis_rad) {
		this.nis_rad = nis_rad;
	}

	public int getAdmin_com() {
		return admin_com;
	}

	public void setAdmin_com(int admin_com) {
		this.admin_com = admin_com;
	}

	public int getAdmin_etic() {
		return admin_etic;
	}

	public void setAdmin_etic(int admin_etic) {
		this.admin_etic = admin_etic;
	}

	public String getFlag_respuesta() {
		return flag_respuesta;
	}

	public void setFlag_respuesta(String flag_respuesta) {
		this.flag_respuesta = flag_respuesta;
	}

}
