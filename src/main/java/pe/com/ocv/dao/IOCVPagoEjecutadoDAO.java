package pe.com.ocv.dao;

import java.util.List;
import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.PagoEjecutado;

public interface IOCVPagoEjecutadoDAO {
	
	public abstract Map<String, Object> insertarPagoEjecutado(PagoEjecutado pago) throws Exception;
	
	public abstract Map<String, Object> obtenerResultadoPago(String trxID);
	
	public abstract Map<String, Object> enviarCorreoPago(String fecha, String hora, String numOperacion, String numTarjeta, double monto, int nisRad, String correo);
	
//	public abstract Map<String, Object> insertarPagoEjecutadoSinCorreo(PagoEjecutado pago) throws Exception;
	void insertarPagoEjecutadoSinCorreo(PagoEjecutado pago) throws GmdException;
	
//	public abstract Map<String, Object> insertarRegistroPrevio(PagoEjecutado pago) throws Exception;
	/*adecuacion pago ejecutado*/
	void insertarRegistroPrevio(PagoEjecutado pago) throws GmdException;
	
//	public abstract List<PagoEjecutado> obtenerRegistrosPrevios(Long numLiquidacion, Long simboloVar) throws Exception;
	List<PagoEjecutado> obtenerRegistrosPrevios(Long numLiquidacion, Long simboloVar) throws GmdException;
	
//	init intermediate screen
	List<PagoEjecutado> obtenerDatosPago(String trxID) throws GmdException;
//	end intermediate screen
	
	/*adecuacion pago ejecutado*/
	void actualizarRegistroPrevio(PagoEjecutado pago) throws GmdException;
	
	public abstract Map<String, Object> insertarRegistroLiquidacionVisa(String nroLiqui, String nisRad, String json) throws GmdException;
}
