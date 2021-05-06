package pe.com.ocv.service;

import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;

public interface IOCVPagoEjecutadoService {

	Respuesta ObtenerResultadoPago(Map<String, String> requestParm);
	
	Respuesta EnviarCorreoPago(Map<String, String> requestParm);
	
	/*init intermediate screen*/
	Respuesta obtenerDatosPago(Map<String, String> requestParm) throws Exception;
	/*end intermediate screen*/
	
	/*adecuacion proceso pago*/
	Respuesta getPaymentResponseMovil(Map<String, String> requestParm) throws GmdException;
	
	/*adecuacion mastercard pago movil*/
	Respuesta getPaymentResponseMovilV2(Map<String, String> requestParm) throws GmdException;
	
	Respuesta insertarRegistroLiquidacionVisa(String requestParm) throws GmdException;
}
