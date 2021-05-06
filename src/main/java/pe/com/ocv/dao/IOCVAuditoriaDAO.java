package pe.com.ocv.dao;

import java.util.List;
import java.util.Map;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Auditoria;
import pe.com.ocv.model.ParametroGC;

public interface IOCVAuditoriaDAO {
	
	public abstract List<Auditoria> obtenerRelacionCanalAuditoria(Integer idCanalAuditoria, Integer idCanal, Integer idAuditoria) throws Exception;
	
	public abstract Map<String, Object> registrarLog(String correo, Integer idCanalAuditoria, Integer suministro) throws Exception;
	
	/*cvalenciap-16/04/2020: inicio verificacion version*/
	public abstract List<ParametroGC> compararVersionMovil(String version) throws Exception;
	/*cvalenciap-16/04/2020: fin verificacion version*/
	
	/*gjaramillo-14/08/2020: inicio verificacion version*/
//	public abstract Respuesta compararVersionAPK(String version) throws Exception;
	/*gjaramillo-14/08/2020: fin verificacion version*/
	
	/*verificacion de version APK*/
	Map<String, Object> compararVersionAPK(String version) throws GmdException;
	
	/*verificacion de version IOS*/
	Map<String, Object> compararVersionIOS(String version) throws GmdException;
}
