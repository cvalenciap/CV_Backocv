package pe.com.ocv.model;

import java.io.Serializable;
import java.util.Date;

public class Notificacion implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Integer idNotificacion;
	public String desNotificacion;
	public String htmlMensaje;
	public String stRegi;
	public Date feUsuaCrea;
	public Date feUsuaModi;
	
	public Integer getIdNotificacion() {
		return idNotificacion;
	}
	public void setIdNotificacion(Integer idNotificacion) {
		this.idNotificacion = idNotificacion;
	}
	public String getDesNotificacion() {
		return desNotificacion;
	}
	public void setDesNotificacion(String desNotificacion) {
		this.desNotificacion = desNotificacion;
	}
	public String getHtmlMensaje() {
		return htmlMensaje;
	}
	public void setHtmlMensaje(String htmlMensaje) {
		this.htmlMensaje = htmlMensaje;
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
	
}
