package pe.com.ocv.service;

import java.text.ParseException;
import java.util.Map;

import pe.com.ocv.model.Respuesta;

public interface IOCVRecibosService {
	
	Respuesta ListaRecibosPendientesXNis(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta ListaRecibosPagadosXNis(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta DetalleRecibo(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta DetallePagos(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta ObtenerReciboPDF(Map<String, String> requestParm) throws ParseException, Exception;
	
	Respuesta ValidarReciboAnterior(Map<String, String> requestParm) throws ParseException, Exception;
	
}