package pe.com.ocv.dao;

import java.util.Map;

public abstract interface IOCVRecibosDAO {
	
	public abstract Map<String, Object> listaRecibosPendientes(int nis, int pageNumber, int pageSize);

	public abstract Map<String, Object> listaRecibosPagados(int nis, int pageNumber, int pageSize);

	public abstract Map<String, Object> detalleRecibo(String recibo);

	//public abstract Map<String, Object> detallePago(int nis, int sec_nis, String f_fact, int sec_rec);
	
	public abstract Map<String, Object> detallePago(String simbolo_var, String nro_factura);

	public abstract Map<String, Object> obtenerDatosRecibo(int secRec, int nisRad, int secNis, String fFact);

	public abstract Map<String, Object> obtenerParametrosRecibo();
	
	public abstract Map<String, Object> validarReciboAnterior(int nis, String recibo);
	
}