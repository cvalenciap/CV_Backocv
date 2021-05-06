package pe.com.ocv.dao;

import java.util.Map;

public abstract interface IOCVAutenticacionDAO {
	
	public abstract Map<String, Object> validarClave(String correo);

	public abstract Map<String, Object> validarCorreoRecupClave(String correo);

	public abstract Map<String, Object> actualizaClave(String correo, String clave);
	
	public abstract Map<String, Object> registrarLog(String correo, int operacion, int suministro);
	
}