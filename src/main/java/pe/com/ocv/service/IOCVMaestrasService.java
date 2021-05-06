package pe.com.ocv.service;

import pe.com.ocv.model.Respuesta;

public interface IOCVMaestrasService {

	Respuesta devuelveMaestras();

	Respuesta migrarUsuariosAntiguos();

	Respuesta obtenerTitulos();

}