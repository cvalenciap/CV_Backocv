package pe.com.ocv.service;

import java.text.ParseException;
import java.util.Map;

import pe.com.ocv.model.Respuesta;

public interface IOCVIncidenciaService {

	Respuesta ListaMunicipiosAfectados() throws ParseException, Exception;
	
	Respuesta ListaIncidenciasSuministro(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta ListaIncidenciasMunicipio(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta ListaAreasAfectadas(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta ObtenerPredioAfectado(Map<String, String> requestParm) throws ParseException, Exception;
	
	Respuesta ListaIncidencias() throws ParseException, Exception;

}