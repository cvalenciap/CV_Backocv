package pe.com.ocv.model;

import java.io.Serializable;
import java.util.Date;

public class Auditoria implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public Integer idCanalAuditoria;
	public Integer idCanal;
	public Integer idAuditoria;
	public String desCanal;
	public String desAuditoria;
	public String desCanalCorta;
	public String desAuditoriaCorta;
		
	public String stRegi;
	public Date feUsuaCrea;
	public Date feUsuaModi;
		
	public Integer getIdCanalAuditoria() {
		return idCanalAuditoria;
	}
	public void setIdCanalAuditoria(Integer idCanalAuditoria) {
		this.idCanalAuditoria = idCanalAuditoria;
	}
	public Integer getIdCanal() {
		return idCanal;
	}
	public void setIdCanal(Integer idCanal) {
		this.idCanal = idCanal;
	}
	public Integer getIdAuditoria() {
		return idAuditoria;
	}
	public void setIdAuditoria(Integer idAuditoria) {
		this.idAuditoria = idAuditoria;
	}
	public String getDesCanal() {
		return desCanal;
	}
	public void setDesCanal(String desCanal) {
		this.desCanal = desCanal;
	}
	public String getDesAuditoria() {
		return desAuditoria;
	}
	public void setDesAuditoria(String desAuditoria) {
		this.desAuditoria = desAuditoria;
	}
	public String getStRegi() {
		return stRegi;
	}
	public void setStRegi(String stRegi) {
		this.stRegi = stRegi;
	}
	public Date getFeUsuaCrea() {
		return feUsuaCrea;
	}
	public void setFeUsuaCrea(Date feUsuaCrea) {
		this.feUsuaCrea = feUsuaCrea;
	}
	public Date getFeUsuaModi() {
		return feUsuaModi;
	}
	public void setFeUsuaModi(Date feUsuaModi) {
		this.feUsuaModi = feUsuaModi;
	}
	public String getDesCanalCorta() {
		return desCanalCorta;
	}
	public void setDesCanalCorta(String desCanalCorta) {
		this.desCanalCorta = desCanalCorta;
	}
	public String getDesAuditoriaCorta() {
		return desAuditoriaCorta;
	}
	public void setDesAuditoriaCorta(String desAuditoriaCorta) {
		this.desAuditoriaCorta = desAuditoriaCorta;
	}
}
