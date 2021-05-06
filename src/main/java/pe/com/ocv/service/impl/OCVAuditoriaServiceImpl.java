package pe.com.ocv.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.dao.IOCVAuditoriaDAO;
import pe.com.ocv.model.Auditoria;
import pe.com.ocv.model.ParametroGC;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVAuditoriaService;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.ConstantesDAO;

@Service
public class OCVAuditoriaServiceImpl implements IOCVAuditoriaService {

	@Autowired
	private IOCVAuditoriaDAO auditoriaDao;

//	@Override
//	public Map<String, Object> registrarLog(String correo, String flagChannel, Integer tipoLog, Integer suministro) throws Exception{
//		Map<String, Object> out = null;
//		Integer idFlagChannel = flagChannel != null ? Integer.parseInt(flagChannel) : Constantes.FLAG_CHANNEL_APK; 
//		try {
//			List<Auditoria> listaResult = auditoriaDao.obtenerRelacionCanalAuditoria(null, idFlagChannel, tipoLog);
//			if(listaResult.size() > 0) {
//				out = auditoriaDao.registrarLog(correo, listaResult.get(0).getIdCanalAuditoria(), suministro);
//			}
//		}catch(Exception e) {
//			System.out.println(e + " ERROR AL REGISTRAR LOG\u00d3N");
//		}
//		return out;
//	}
	/* adecuacion pago ejecutado */
	@Override
	public Map<String, Object> registrarLog(String correo, String flagChannel, Integer tipoLog, Integer suministro)
			throws GmdException {
		Map<String, Object> out = null;
		Integer idFlagChannel = flagChannel != null ? Integer.parseInt(flagChannel) : Constantes.FLAG_CHANNEL_APK;
		try {
			List<Auditoria> listaResult = auditoriaDao.obtenerRelacionCanalAuditoria(null, idFlagChannel, tipoLog);
			if (listaResult.size() > 0) {
				out = auditoriaDao.registrarLog(correo, listaResult.get(0).getIdCanalAuditoria(), suministro);
			}
		} catch (Exception e) {
			System.out.println(e + " ERROR AL REGISTRAR LOG\u00d3N");
			throw new GmdException(e);
		}
		return out;
	}

	/* cvalenciap-16/04/2020: inicio verificacion version */
	@Override
	public Boolean verificarVersionMovil(String version) throws Exception {
		Boolean flagVerify = false;
		try {
			List<ParametroGC> listaResult = auditoriaDao.compararVersionMovil(version);
			if (listaResult.size() > 0) {
				flagVerify = true;
			}
		} catch (Exception e) {
			System.out.println(e + " ERROR AL VERIFICAR VERSION MOVIL\u00d3N");
		}
		return flagVerify;
	}
	/* cvalenciap-16/04/2020: fin verificacion version */

	/* gjaramillo-14/08/2020: inicio verificacion version */
//	@Override
//	public Respuesta compararVersionAPK(String version) throws Exception {
//		Respuesta respuesta = null;
//		try {
//			respuesta = new Respuesta();
//			respuesta = auditoriaDao.compararVersionAPK(version);
//		} catch (Exception e) {
//			System.out.println(e + " ERROR AL VERIFICAR VERSION MOVIL\u00d3N");
//		}
//		return respuesta;
//	}
	/* gjaramillo-14/08/2020: fin verificacion version */
	
	/*verificacion de version APK*/
	@Override
	public Respuesta compararVersionAPK(String version) throws GmdException {
		Respuesta respuesta = new Respuesta();
		Map<String, Object> out = new HashMap<String, Object>();
		Map<String, Object> datos = new TreeMap<String, Object>();
		try {
			out = auditoriaDao.compararVersionAPK(version);
			datos.put("codigoError", (String) out.get(ConstantesDAO.CCODIGOERROR));
			datos.put("esObligatorio", (Integer) out.get(ConstantesDAO.NESOBLIGATORIO));
			respuesta.setcRESP_SP((String) out.get(ConstantesDAO.MENSAJE_RESPUESTA));
			respuesta.setnRESP_SP((Integer) out.get(ConstantesDAO.COD_RESPUESTA));
			respuesta.setbRESP(datos);
		} catch (Exception excepcion) {
			System.out.println(excepcion + " ERROR AL VERIFICAR VERSION MOVIL APK\\u00d3N");
			throw new GmdException(excepcion);
		}
		return respuesta;
	}
	
	/*verificacion de version IOS*/
	@Override
	public Respuesta compararVersionIOS(String version) throws GmdException {
		Respuesta respuesta = new Respuesta();
		Map<String, Object> out = new HashMap<String, Object>();
		Map<String, Object> datos = new TreeMap<String, Object>();
		try {
			out = auditoriaDao.compararVersionIOS(version);
			datos.put("codigoError", (String) out.get(ConstantesDAO.CCODIGOERROR));
			datos.put("esObligatorio", (Integer) out.get(ConstantesDAO.NESOBLIGATORIO));
			respuesta.setcRESP_SP((String) out.get(ConstantesDAO.MENSAJE_RESPUESTA));
			respuesta.setnRESP_SP((Integer) out.get(ConstantesDAO.COD_RESPUESTA));
			respuesta.setbRESP(datos);
		} catch (Exception excepcion) {
			System.out.println(excepcion + " ERROR AL VERIFICAR VERSION MOVIL IOS\\u00d3N");
			throw new GmdException(excepcion);
		}
		return respuesta;
	}
}
