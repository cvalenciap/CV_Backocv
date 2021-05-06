package pe.com.ocv.service;

import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;

public interface IOCVAuditoriaService {
	
	/*adecuacion pago ejecutado*/
//	public Map<String, Object> registrarLog(String correo, String flagChannel, Integer tipoLog, Integer suministro) throws Exception;
	Map<String, Object> registrarLog(String correo, String flagChannel, Integer tipoLog, Integer suministro) throws GmdException;
	
	/*cvalenciap-16/04/2020: inicio verificacion version*/
	public Boolean verificarVersionMovil(String version) throws Exception;
	/*cvalenciap-16/04/2020: fin verificacion version*/
	
	/*gjaramillo-14/08/2020: inicio verificacion version*/
	public Respuesta compararVersionAPK(String version) throws GmdException;
	/*gjaramillo-14/08/2020: fin verificacion version*/
	
	/*verificacion de version IOS*/
	Respuesta compararVersionIOS(String version) throws GmdException;
}
