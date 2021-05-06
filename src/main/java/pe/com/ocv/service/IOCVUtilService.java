package pe.com.ocv.service;

import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;

public interface IOCVUtilService {
	
	/*actualizacion obtener parametos por categoria*/
	Respuesta obtenerParametrosCategoriaV2(Map<String, String> requestParm) throws GmdException;
	
	Respuesta ObtenerParametrosCategoria(Map<String, String> requestParm);
	
	Respuesta ObtenerParametrosGC(Map<String, String> requestParm);
	
	Respuesta InsertarParametrosGC(Map<String, String> requestParm);
	
	Respuesta EditarParametrosGC(Map<String, String> requestParm);

}
