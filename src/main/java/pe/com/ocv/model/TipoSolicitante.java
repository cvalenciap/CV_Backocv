package pe.com.ocv.model;

public class TipoSolicitante {
	private int idTipoSolicitante;
	private String tipoSolicitante;
	private String descripcion;

	public TipoSolicitante() {
		super();
	}

	public TipoSolicitante(int idTipoSolicitante, String tipoSolicitante, String descripcion) {
		super();
		this.idTipoSolicitante = idTipoSolicitante;
		this.tipoSolicitante = tipoSolicitante;
		this.descripcion = descripcion;
	}

	public int getIdTipoSolicitante() {
		return idTipoSolicitante;
	}

	public void setIdTipoSolicitante(int id_TipoSolicitante) {
		this.idTipoSolicitante = id_TipoSolicitante;
	}

	public String getTipoSolicitante() {
		return tipoSolicitante;
	}

	public void setTipoSolicitante(String tipoSolicitante) {
		this.tipoSolicitante = tipoSolicitante;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
