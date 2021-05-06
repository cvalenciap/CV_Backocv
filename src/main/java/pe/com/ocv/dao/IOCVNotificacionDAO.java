package pe.com.ocv.dao;

import java.util.Map;

import pe.com.ocv.model.Notificacion;
import pe.com.ocv.model.PagoEjecutado;

public interface IOCVNotificacionDAO {
	
	public abstract Map<String, Object> enviarCorreoxNotificacion(Integer idNotificacion, PagoEjecutado Pago) throws Exception;
	
	public abstract Map<String, Object> enviarCorreoxNotificacionGeneral(Integer idNotificacion, String correo, String var1, String var2, String var3, String var4, String var5, String var6, String var7) throws Exception;
	
	public abstract Notificacion obtenerNotificacion(Integer idNotificacion) throws Exception;

}
