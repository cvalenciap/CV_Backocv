package pe.com.ocv.model;

import java.io.Serializable;

public class TipoTarjeta implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id_tarjeta;
	private String descripcion;
	private int codigoAgencia;

	public TipoTarjeta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TipoTarjeta(int id_tarjeta, String descripcion) {
		super();
		this.id_tarjeta = id_tarjeta;
		this.descripcion = descripcion;
	}

	public int getId_tarjeta() {
		return id_tarjeta;
	}

	public void setId_tarjeta(int id_tarjeta) {
		this.id_tarjeta = id_tarjeta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCodigoAgencia() {
		return codigoAgencia;
	}

	public void setCodigoAgencia(int codigoAgencia) {
		this.codigoAgencia = codigoAgencia;
	}

}
