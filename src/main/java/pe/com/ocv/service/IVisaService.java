package pe.com.ocv.service;

import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;

public interface IVisaService {

	public Respuesta getSession(Map<String, String> requestParm);

//	public Respuesta getPaymentResponse(Map<String, String> requestParm);
	
	/*adecuacion proceso pago*/
	Respuesta getPaymentResponse(Map<String, String> requestParm) throws GmdException;
	
//	inicio cvalenciap
	public Respuesta insertarPagoMovil(Map<String, String> requestParm) throws Exception;
//	fin cvalenciap
	
	/*adecuacion mastercard*/
	Respuesta getPaymentResponseV2(Map<String, String> requestParm) throws GmdException;
	

}
