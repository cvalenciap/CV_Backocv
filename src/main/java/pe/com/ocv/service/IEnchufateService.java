package pe.com.ocv.service;

import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;

public interface IEnchufateService {

	public String getToken(Map<String, String> requestParm);

//	public Respuesta generarLiquidacion(Map<String, Object> requestParm);
	
	public Respuesta generarLiquidacion(Map<String, Object> requestParm) throws GmdException;

//	public Respuesta pagarLiquidacion(Map<String, String> requestParm);
	
	public Respuesta pagarLiquidacion(Map<String, String> requestParm) throws GmdException;
	
	/*actualizacion de generacion de liquidacion*/
	Respuesta generarLiquidacion2(Map<String, Object> requestParm) throws GmdException;
	
}
