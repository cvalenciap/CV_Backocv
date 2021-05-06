package pe.com.ocv.model;

public class TipoDocumento {
	private int idTipoDocumento;
	private String tipoDocumento;
	private String descripcion;

	public TipoDocumento() {
		super();
	}

	public TipoDocumento(int id_TipoDocumento, String tipoDocumento, String descripcion) {
		super();
		this.idTipoDocumento = id_TipoDocumento;
		this.tipoDocumento = tipoDocumento;
		this.descripcion = descripcion;
	}

	public int getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(int id_TipoDocumento) {
		this.idTipoDocumento = id_TipoDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
