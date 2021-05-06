package pe.com.ocv.dao;

import java.util.List;
import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.ParametroGC;
import pe.com.ocv.model.TipoTarjeta;

public abstract interface IOCVUtilDAO {

	public abstract void enviarCorreo(String to, String subject, String body);

	public abstract Map<String, Object> obtenerParamCategoria(String categoria);

	public abstract Map<String, Object> obtenerParametrosGC(int pageNumber, int pageSize, String correo);

	public abstract Map<String, Object> insertarParametrosGC(ParametroGC parameter);

	public abstract Map<String, Object> editarParametrosGC(ParametroGC parameter);
	
	/*adecuacion proceso pago*/
	List<TipoTarjeta> obtenerDatosTarjeta(Integer idTarjeta, String descripcion, Integer codigoAgencia) throws GmdException;
	
	/*generacion de numero de liquidacion por sequence*/
	Integer obtenerNumLiquidacionSeq() throws GmdException;
	
//	actualizacion mastercard
	Map<String, Object> obtenerParamCategoriaV2(String categoria) throws GmdException;

}