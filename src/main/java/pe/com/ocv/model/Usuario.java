package pe.com.ocv.model;

import java.io.Serializable;

public class Usuario implements Serializable {
	
	public static final long serialVersionUID = 1L;
	
	Integer idCliente;
	Long numSuministro;
	String refCobro;
	Integer tipoDocumento;
	String numDocumento;
	String apePaterno;
	String apeMaterno;
	String nombre;
	String correo;
	String token;
	Integer estado;
	Integer codVerify;
	
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public Long getNumSuministro() {
		return numSuministro;
	}
	public void setNumSuministro(Long numSuministro) {
		this.numSuministro = numSuministro;
	}
	public String getRefCobro() {
		return refCobro;
	}
	public void setRefCobro(String refCobro) {
		this.refCobro = refCobro;
	}
	public Integer getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(Integer tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNumDocumento() {
		return numDocumento;
	}
	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}
	public String getApePaterno() {
		return apePaterno;
	}
	public void setApePaterno(String apePaterno) {
		this.apePaterno = apePaterno;
	}
	public String getApeMaterno() {
		return apeMaterno;
	}
	public void setApeMaterno(String apeMaterno) {
		this.apeMaterno = apeMaterno;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Integer getCodVerify() {
		return codVerify;
	}
	public void setCodVerify(Integer codVerify) {
		this.codVerify = codVerify;
	}	
}
