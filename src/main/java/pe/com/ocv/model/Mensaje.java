package pe.com.ocv.model;

public class Mensaje {

	private String codMensaje;
	private String descMensaje;

	public Mensaje() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Mensaje(String codMensaje, String descMensaje) {
		super();
		this.codMensaje = codMensaje;
		this.descMensaje = descMensaje;
	}

	public String getCodMensaje() {
		return codMensaje;
	}

	public void setCodMensaje(String codMensaje) {
		this.codMensaje = codMensaje;
	}

	public String getDescMensaje() {
		return descMensaje;
	}

	public void setDescMensaje(String descMensaje) {
		this.descMensaje = descMensaje;
	}

}
