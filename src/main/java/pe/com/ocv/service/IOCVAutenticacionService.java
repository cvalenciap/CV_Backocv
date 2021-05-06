package pe.com.ocv.service;

import java.text.ParseException;
import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;

public interface IOCVAutenticacionService {

	public Respuesta validarClave(Map<String, String> requestParm);

	public Respuesta validarCorreoRecupClave(Map<String, String> requestParm);

	public Respuesta actualizarClave(Map<String, String> requestParm) throws ParseException, Exception;
	
	/*add method valid version*/
//	Respuesta validarVersion(Map<String, String> requestParm) throws GmdException;
	
	/*update method valid version*/
	Respuesta validarVersiones(Map<String, String> requestParm) throws GmdException;

}