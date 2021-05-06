package pe.com.ocv.service;

import java.util.Map;

import pe.com.ocv.model.Respuesta;

public interface IOCVLugaresPagoService {
	
	Respuesta ListaAgencias();

	Respuesta ListaSucursales(Map<String, String> requestParm);
	
}