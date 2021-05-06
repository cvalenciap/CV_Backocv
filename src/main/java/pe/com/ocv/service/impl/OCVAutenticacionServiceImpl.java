package pe.com.ocv.service.impl;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import pe.com.ocv.util.Util;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.Encriptacion;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.dao.IOCVAutenticacionDAO;
import pe.com.ocv.model.DatosLogin;
import pe.com.ocv.model.Respuesta;

import org.springframework.stereotype.Service;

import pe.com.ocv.service.IOCVAuditoriaService;
import pe.com.ocv.service.IOCVAutenticacionService;

@Service
public class OCVAutenticacionServiceImpl implements IOCVAutenticacionService {
	
	@Autowired
	private IOCVAuditoriaService auditoriaService;
	
	@Autowired
	private IOCVAutenticacionDAO dao;
	private Encriptacion encriptacion;
	private Util util;

	public OCVAutenticacionServiceImpl() {
		super();
		this.encriptacion = new Encriptacion();
		this.util = new Util();
	}
	
	
//	@SuppressWarnings("unchecked")
//	@Override
//	@Transactional(rollbackFor = { Exception.class })
//	public Respuesta validarClave(Map<String, String> requestParm) {
//		Respuesta respuesta = new Respuesta(0, "");
//		DatosLogin datos = new DatosLogin(); 
//		datos.setFlag_respuesta("B");
//		Boolean flagVersion = true;
//		/*cvalenciap-16/04/2020: inicio verificacion version*/
//		Map<String, String> parametrosVersion = new HashMap<String, String>();
//		String mensajeErrorVersion = "";
//		Integer flagValida = null; 
//		/**/
//		try {
//			/*forzar version erronea y canal movil*/
////			requestParm.put("flagChannel", "2");
////			requestParm.put("versionMovil", "1.0");
//			/**/
//			/*verificacion de validacion de version*/
//			Map<String, String> dato = new TreeMap<String, String>();
//			dato.put(Constantes.CAMP_CATEGORIA, Constantes.CATEGORIA_VERIF_VERSION);
//			parametrosVersion = (Map<String, String>) utilService.ObtenerParametrosCategoria(dato).getbRESP();
//			flagValida = StringUtils.isNotEmpty(parametrosVersion.get("version_valida")) && StringUtils.isNumeric(parametrosVersion.get("version_valida")) ? Integer.valueOf(parametrosVersion.get("version_valida")) : flagValida;
//			mensajeErrorVersion = StringUtils.isNotEmpty(parametrosVersion.get("version_mensaje")) ? parametrosVersion.get("version_mensaje") : mensajeErrorVersion; 
//			Integer idFlagChannel = requestParm.get("flagChannel") != null ? Integer.parseInt(requestParm.get("flagChannel")) : Constantes.FLAG_CHANNEL_APK;
//			if(idFlagChannel.intValue() == Constantes.FLAG_CHANNEL_APK && flagValida != null && flagValida.intValue() == Constantes.ESTADO_SUCCESS) {
//				flagVersion = auditoriaService.verificarVersionMovil(String.valueOf(requestParm.get("versionMovil")));
//			}
//			/**/
//			if(flagVersion) {
//				String vCorreo = requestParm.get("correo");
//				String vClave = requestParm.get("clave");
//
//				Map<String, Object> hmValida = this.dao.validarClave(vCorreo);
//				BigDecimal bdCast = (BigDecimal) hmValida.get("nRESP_SP");
//				datos.setFlag_respuesta((String) hmValida.get("vFLAG"));
//				respuesta.setnRESP_SP(bdCast.intValueExact());
////				/*init intermediate screen*/
////				if (respuesta.getnRESP_SP() == 2) {
////					String[] responseString = ((String) hmValida.get("cRESP_SP")).split("\\|");
////					respuesta.setcRESP_SP(responseString[0]);
////					respuesta.setcRESP_SP2(responseString[1]);
////				} else {
////					respuesta.setcRESP_SP((String) hmValida.get("cRESP_SP"));
////				}
//////				if (respuesta.getnRESP_SP() != 0 ) {
////				if (respuesta.getnRESP_SP() == 1 ) {
////					/*end intermediate screen*/
////					Map<String, Object> hmClave = this.encriptacion.convertirSHA256(vClave);
////					String bdClave = (String) hmValida.get("vCLAVE");
////					String sClave = (String) hmClave.get("vSALIDA");
////
////					if (bdClave.equals(sClave)) {
////						datos.setId_cliente(((BigDecimal) hmValida.get("vID_CLIENTE")).intValue());
////						datos.setNis_rad(((BigDecimal) hmValida.get("vNIS_RAD")).intValue());
////						datos.setAdmin_com(((BigDecimal) hmValida.get("vADMIN_COM")).intValue());
////						datos.setAdmin_etic(((BigDecimal) hmValida.get("vADMIN_ETIC")).intValue());
////						//registro acceso --success
////						auditoriaService.registrarLog(vCorreo, requestParm.get("flagChannel"), Constantes.AUDITORIA_LOG_SUCCESS, Constantes.AUDITORIA_DEFAULT);
////					} else {
////						respuesta.setcRESP_SP("El usuario y/o clave no coinciden");
////						respuesta.setnRESP_SP(0);
////						//registro acceso --failure -- credencialess
////						auditoriaService.registrarLog(vCorreo, requestParm.get("flagChannel"), Constantes.AUDITORIA_LOG_FAILURE, Constantes.AUDITORIA_DEFAULT);
////					}
////				}//else //registro acceso --failure --conexion
//				Map<String, Object> hmClave = this.encriptacion.convertirSHA256(vClave);
//				String bdClave = (String) hmValida.get("vCLAVE");
//				String sClave = (String) hmClave.get("vSALIDA");
//				respuesta.setcRESP_SP((String) hmValida.get("cRESP_SP"));
//				if(StringUtils.isNotEmpty(bdClave)) {
//					if (bdClave.equals(sClave)) {
//						if (respuesta.getnRESP_SP() == 1) {
////							datos.setId_cliente(((BigDecimal) hmValida.get("vID_CLIENTE")).intValue());
////							datos.setNis_rad(((BigDecimal) hmValida.get("vNIS_RAD")).intValue());
////							datos.setAdmin_com(((BigDecimal) hmValida.get("vADMIN_COM")).intValue());
////							datos.setAdmin_etic(((BigDecimal) hmValida.get("vADMIN_ETIC")).intValue());
//							//registro acceso --success
//							auditoriaService.registrarLog(vCorreo, requestParm.get("flagChannel"), Constantes.AUDITORIA_LOG_SUCCESS, Constantes.AUDITORIA_DEFAULT);
//						} else if (respuesta.getnRESP_SP() == 2) {
//							String[] responseString = ((String) hmValida.get("cRESP_SP")).split("\\|");
//							respuesta.setcRESP_SP(responseString[0]);
//							respuesta.setcRESP_SP2(responseString[1]);
//						}
//						datos.setId_cliente(((BigDecimal) hmValida.get("vID_CLIENTE")).intValue());
//						datos.setNis_rad(((BigDecimal) hmValida.get("vNIS_RAD")).intValue());
//						datos.setAdmin_com(((BigDecimal) hmValida.get("vADMIN_COM")).intValue());
//						datos.setAdmin_etic(((BigDecimal) hmValida.get("vADMIN_ETIC")).intValue());
//					} else {
//						respuesta.setcRESP_SP("El usuario y/o clave no coinciden");
//						respuesta.setnRESP_SP(0);
//						//registro acceso --failure -- credencialess
//						auditoriaService.registrarLog(vCorreo, requestParm.get("flagChannel"), Constantes.AUDITORIA_LOG_FAILURE, Constantes.AUDITORIA_DEFAULT);
//					}
//				}				
//			} else {
//				respuesta.setcRESP_SP(mensajeErrorVersion);
//				respuesta.setnRESP_SP(0);
//			}
//			/*cvalenciap-16/04/2020: fin verificacion version*/
//		} catch (Exception e) {
//			System.out.println("OCVAutenticacionServiceImpl.validarClave()");
//			System.err.println("Ocurrió un error: " + e);
//			respuesta.setcRESP_SP("Ocurrió un error: " + e);
//			datos.setFlag_respuesta("B");
//			respuesta.setnRESP_SP(0);
//		}
//		respuesta.setbRESP(datos);
//		return respuesta;
//	}
	
	/*Adecuación login*/
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Respuesta validarClave(Map<String, String> requestParm) {
		Respuesta respuesta = new Respuesta(0, "");
		DatosLogin datos = new DatosLogin(); 
		datos.setFlag_respuesta("B");
		try {
				String vCorreo = requestParm.get("correo");
				String vClave = requestParm.get("clave");

				Map<String, Object> hmValida = this.dao.validarClave(vCorreo);
				BigDecimal bdCast = (BigDecimal) hmValida.get("nRESP_SP");
				datos.setFlag_respuesta((String) hmValida.get("vFLAG"));
				respuesta.setnRESP_SP(bdCast.intValueExact());
//				/*init intermediate screen*/
				Map<String, Object> hmClave = this.encriptacion.convertirSHA256(vClave);
				String bdClave = (String) hmValida.get("vCLAVE");
				String sClave = (String) hmClave.get("vSALIDA");
				respuesta.setcRESP_SP((String) hmValida.get("cRESP_SP"));
				if(StringUtils.isNotEmpty(bdClave)) {
					if (bdClave.equals(sClave)) {
						if (respuesta.getnRESP_SP() == 1) {
							//registro acceso --success
							auditoriaService.registrarLog(vCorreo, requestParm.get("flagChannel"), Constantes.AUDITORIA_LOG_SUCCESS, Constantes.AUDITORIA_DEFAULT);
						} else if (respuesta.getnRESP_SP() == 2) {
							String[] responseString = ((String) hmValida.get("cRESP_SP")).split("\\|");
							respuesta.setcRESP_SP(responseString[0]);
							respuesta.setcRESP_SP2(responseString[1]);
						}
						datos.setId_cliente(((BigDecimal) hmValida.get("vID_CLIENTE")).intValue());
						datos.setNis_rad(((BigDecimal) hmValida.get("vNIS_RAD")).intValue());
						datos.setAdmin_com(((BigDecimal) hmValida.get("vADMIN_COM")).intValue());
						datos.setAdmin_etic(((BigDecimal) hmValida.get("vADMIN_ETIC")).intValue());
					} else {
						respuesta.setcRESP_SP("El usuario y/o clave no coinciden");
						respuesta.setnRESP_SP(0);
						//registro acceso --failure -- credencialess
						auditoriaService.registrarLog(vCorreo, requestParm.get("flagChannel"), Constantes.AUDITORIA_LOG_FAILURE, Constantes.AUDITORIA_DEFAULT);
					}
				}				
			
			/*cvalenciap-16/04/2020: fin verificacion version*/
		} catch (Exception e) {
			System.out.println("OCVAutenticacionServiceImpl.validarClave()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			datos.setFlag_respuesta("B");
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP(datos);
		return respuesta;
	}

	@Override
	public Respuesta validarCorreoRecupClave(Map<String, String> requestParm) {
		Respuesta respuesta = new Respuesta(0, "");
		try {
			String vCorreo = requestParm.get("correo");
			Respuesta vMapCorreo = this.util.validaFormatoCorreo(vCorreo);
			respuesta.setcRESP_SP(vMapCorreo.getcRESP_SP());
			respuesta.setnRESP_SP(vMapCorreo.getnRESP_SP());

			if (respuesta.getnRESP_SP() != 0) {
				Map<String, Object> vMapNombre = this.dao.validarCorreoRecupClave(vCorreo);
				respuesta.setcRESP_SP((String) vMapNombre.get("cRESP_SP"));
				respuesta.setnRESP_SP(((BigDecimal) vMapNombre.get("nRESP_SP")).intValueExact());
			}
		} catch (Exception e) {
			System.out.println("OCVAutenticacionServiceImpl.validarCorreoRecupClave()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP("");
		return respuesta;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Respuesta actualizarClave(Map<String, String> requestParm) {
		Respuesta respuesta = new Respuesta(0, "");
		try {
			String sCorreo = requestParm.get("correo");
			String sClave = requestParm.get("clave");

			Map<String, Object> encriptaClave = this.encriptacion.convertirSHA256(sClave);
			String claveSalida = (String) encriptaClave.get("vSALIDA");
			Map<String, Object> queryResp = this.dao.actualizaClave(sCorreo, claveSalida);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValue());
		} catch (Exception e) {
			System.out.println("OCVAutenticacionServiceImpl.actualizarClave()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP("");
		return respuesta;
	}
	
	/*add method valid version*/
//	@SuppressWarnings("unchecked")
//	@Override
//	public Respuesta validarVersion(Map<String, String> requestParm) throws GmdException {
//		// Se comenta - gjaramillo-14/08/2020
//		// Respuesta respuesta = new Respuesta(0, "");
//		Respuesta respuesta = new Respuesta();
//		Map<String, String> parametrosVersion = new HashMap<String, String>();
//		Integer flagValida = null;
//		// Se comenta - gjaramillo-14/08/2020
//		/*
//		 * String mensajeErrorVersion = "";  Boolean
//		 * flagVersion = true;
//		 */
//		Map<String, Object> datos = new TreeMap<String, Object>();
//		try {
////			forzar error
////			if(1==1) {
////				throw new Exception("error forzado");
////			}
//			respuesta.setnRESP_SP(Constantes.ESTADO_SUCCESS);
//			/*generic parameters*/
//			Map<String, String> dato = new TreeMap<String, String>();
//			dato.put(Constantes.CAMP_CATEGORIA, Constantes.CATEGORIA_VERIF_VERSION);
//			parametrosVersion = (Map<String, String>) utilService.ObtenerParametrosCategoria(dato).getbRESP();
//			flagValida = StringUtils.isNotEmpty(parametrosVersion.get("version_valida")) && StringUtils.isNumeric(parametrosVersion.get("version_valida")) ? Integer.valueOf(parametrosVersion.get("version_valida")) : flagValida;
//			// Se comenta - gjaramillo-14/08/2020
//			/*
//			 * flagVersion =
//			 * auditoriaService.verificarVersionMovil(String.valueOf(requestParm.get(
//			 * "versionMovil"))); if(!flagVersion) { flagValida = 1; } if(flagValida !=
//			 * null) { datos.put("esObligatorio", flagValida); } else { throw new
//			 * GmdException("Error al obtener parametro de validación."); } if(flagVersion)
//			 * { respuesta.setcRESP_SP(Constantes.MENSAJE_VERSION_VALIDA); } else {
//			 * mensajeErrorVersion =
//			 * StringUtils.isNotEmpty(parametrosVersion.get("version_mensaje")) ?
//			 * parametrosVersion.get("version_mensaje") : mensajeErrorVersion; String[]
//			 * mensajeList = mensajeErrorVersion.split("\\|"); datos.put("codigoError",
//			 * mensajeList[0]); respuesta.setbRESP(datos);
//			 * respuesta.setcRESP_SP(mensajeList[1]); }
//			 */
//			System.out.println("VersionMovil: " + String.valueOf(requestParm.get("versionMovil")));
//			respuesta = auditoriaService.compararVersionAPK(String.valueOf(requestParm.get("versionMovil")));
//			System.out.println("Respuesta - Service");
//			System.out.println("cResp_SP: " + respuesta.getcRESP_SP());
//			System.out.println("nResp_SP: " + respuesta.getnRESP_SP());
//			System.out.println("bResp_SP: " + respuesta.getbRESP());
//		} catch (Exception e) {
//			System.out.println("OCVAutenticacionServiceImpl.validarVersion()");
//			System.err.println("Ocurrió un error: " + e.getMessage());
//			respuesta.setcRESP_SP("Ocurrió un error: " + e.getMessage());
//			respuesta.setnRESP_SP(0);
//			/*set empty values*/
//			datos.put("esObligatorio", "");
//			datos.put("codigoError", "");
//		}
//		respuesta.setbRESP(respuesta.getbRESP());
//		//respuesta.setbRESP(datos);
//		return respuesta;
//	}
	
	/*update method valida version */
	@Override
	public Respuesta validarVersiones(Map<String, String> requestParm) throws GmdException {
		Respuesta respuesta = new Respuesta();
		Map<String, Object> datos = new TreeMap<String, Object>();
		try {
			System.out.println("VersionMovil: " + String.valueOf(requestParm.get("versionMovil")));
			System.out.println("CanalIngreso: " + String.valueOf(requestParm.get("flagChannel")));
			Integer idFlagChannel = requestParm.get("flagChannel") != null ? Integer.parseInt(requestParm.get("flagChannel")) : Constantes.FLAG_CHANNEL_APK;
			switch (idFlagChannel) {
				case Constantes.FLAG_CHANNEL_APK:
					respuesta = auditoriaService.compararVersionAPK(String.valueOf(requestParm.get("versionMovil")));
					break;
				case Constantes.FLAG_CHANNEL_IOS:
					respuesta = auditoriaService.compararVersionIOS(String.valueOf(requestParm.get("versionMovil")));
					break;
				default:
					throw new GmdException("Canal de ingreso a la validación no permitido.");
			}
			System.out.println("Respuesta - Service");
			System.out.println("cResp_SP: " + respuesta.getcRESP_SP());
			System.out.println("nResp_SP: " + respuesta.getnRESP_SP());
			System.out.println("bResp_SP: " + respuesta.getbRESP());
		} catch (Exception e) {
			System.out.println("OCVAutenticacionServiceImpl.validarVersion()");
			System.err.println("Ocurrió un error: " + e.getMessage());
			respuesta.setcRESP_SP("Ocurrió un error: " + e.getMessage());
			respuesta.setnRESP_SP(0);
			/*set empty values*/
			datos.put("esObligatorio", "");
			datos.put("codigoError", "");
		}
		return respuesta;
	}
	

}