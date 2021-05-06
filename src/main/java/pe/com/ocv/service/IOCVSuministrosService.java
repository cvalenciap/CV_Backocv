package pe.com.ocv.service;

import java.text.ParseException;
import java.util.Map;

import pe.com.ocv.model.Respuesta;

public interface IOCVSuministrosService {
	
	Respuesta ListaNisXCliente(Map<String, String> requestParm);

	Respuesta CabeceraXNis(Map<String, String> requestParm);

	Respuesta HistoricoConsumo(Map<String, String> requestParm) throws ParseException, Exception;
	
}