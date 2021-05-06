package pe.com.ocv.service.impl;

import java.util.Objects;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;

import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import pe.com.ocv.model.DatosAntiFraude;
import pe.com.ocv.model.OCVRegistro;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.model.Usuario;

import java.util.Map;
import pe.com.ocv.util.Util;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.Encriptacion;
import pe.com.ocv.util.ParametrosUtil;

import org.springframework.beans.factory.annotation.Autowired;

import pe.com.ocv.dao.IOCVNotificacionDAO;
import pe.com.ocv.dao.IOCVRegistroDAO;
import org.springframework.stereotype.Service;

import pe.com.ocv.service.IOCVAuditoriaService;
import pe.com.ocv.service.IOCVRegistroService;
import pe.com.ocv.service.IOCVUtilService;

@Service
@SuppressWarnings("unchecked")
public class OCVRegistroServiceImpl implements IOCVRegistroService {

	@Autowired
	private IOCVAuditoriaService auditoriaService;
	
	/*get url parameters*/
	@Autowired
	private IOCVUtilService utilService;
	
	/*dao notification*/
	@Autowired
	private IOCVNotificacionDAO notificacionDao;
	
	@Autowired
	private IOCVRegistroDAO dao;	
	
	private Encriptacion encriptacion;
	private Util util;

	public OCVRegistroServiceImpl() {
		super();
		this.encriptacion = new Encriptacion();
		this.util = new Util();
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Respuesta insertaOCVRegistro(Map<String, String> requestParm) throws ParseException, Exception {
		Respuesta respuesta = new Respuesta(0, "");
		try {
			String vCaptcha = requestParm.get("captcha");

			if (vCaptcha != null) {
				try {
					String url = requestParm.get("url");
					URL obj = new URL(url);
					HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

					con.setRequestMethod("POST");
					con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

					String urlParameters = "secret=" + requestParm.get("private_key") + "&response=" + vCaptcha;

					con.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.writeBytes(urlParameters);
					wr.flush();
					wr.close();

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					JsonObject jsonObj = new Gson().fromJson(response.toString(), JsonObject.class);

					if (!jsonObj.get("success").getAsBoolean()) {
						respuesta.setcRESP_SP("La validación del captcha es incorrecta.");
						respuesta.setnRESP_SP(0);
						return respuesta;
					}

				} catch (Exception e) {
					respuesta.setcRESP_SP("Ocurrió un error: " + e);
					respuesta.setnRESP_SP(0);
					return respuesta;
				}
			}

			OCVRegistro OCVRegistro = new OCVRegistro();
			OCVRegistro.setNis_rad(Integer.parseInt(requestParm.get("nis_rad")));
			OCVRegistro.setRef_cobro(requestParm.get("ref_cobro"));
			OCVRegistro.setTipo_docu(Integer.parseInt(requestParm.get("tipo_doc")));
			OCVRegistro.setNro_doc(requestParm.get("nro_doc"));
			OCVRegistro.setApellido1(requestParm.get("apellido1"));
			OCVRegistro.setApellido2(requestParm.get("apellido2"));
			OCVRegistro.setNombres(requestParm.get("nombres"));
			OCVRegistro.setCorreo(requestParm.get("correo"));
			OCVRegistro.setTelefono1(requestParm.get("telefono1"));
			OCVRegistro.setAcepta_terminos(Integer.parseInt(requestParm.get("acepta_terminos")));
			OCVRegistro.setAcepta_correo(Integer.parseInt(requestParm.get("acepta_noti")));
			OCVRegistro.setAcepta_noti(Integer.parseInt(requestParm.get("acepta_noti")));
			/*add cod verify*/
			OCVRegistro.setCodVerify(ParametrosUtil.generateCodVerify());

			// Valida el correo electrónico
			Respuesta validaCorreo = this.validaCorreo(OCVRegistro.getCorreo());
			respuesta.setnRESP_SP(validaCorreo.getnRESP_SP());
			respuesta.setcRESP_SP(validaCorreo.getcRESP_SP());
			if (respuesta.getnRESP_SP() != 0) {
				// Encripta la clave
				Map<String, Object> encriptaClave = this.encriptacion
						.convertirSHA256(requestParm.get("clave"));
				String sClave = (String) encriptaClave.get("vSALIDA");
				OCVRegistro.setClave(sClave);

				// Registra el usuario
				Map<String, Object> registraUsuario = this.dao.insertaRegistro(OCVRegistro,
						false);
				respuesta.setnRESP_SP(((BigDecimal) registraUsuario.get("nRESP_SP")).intValueExact());
				respuesta.setcRESP_SP((String) registraUsuario.get("cRESP_SP"));
				/*add log audit for register*/
				if(respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS) {
					auditoriaService.registrarLog(requestParm.get("correo"), requestParm.get("flagChannel"), Constantes.AUDITORIA_REGISTRO, Integer.parseInt(requestParm.get("nis_rad")));
				}
				/**/
			}
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.insertaOCVRegistro()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP("");
		return respuesta;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Respuesta actualizaOCVRegistro(Map<String, String> requestParm) throws ParseException, Exception {
		OCVRegistro OCVRegistro = new OCVRegistro();
		Respuesta respuesta = new Respuesta();

		try {
			OCVRegistro.setId_cliente(Integer.parseInt(requestParm.get("id_cliente")));
			OCVRegistro.setTipo_soli(Integer.parseInt(requestParm.get("tipo_solicitante")));
			OCVRegistro.setTipo_docu(Integer.parseInt(requestParm.get("tipo_doc")));
			OCVRegistro.setNro_doc(requestParm.get("nro_doc"));
			OCVRegistro.setApellido1(requestParm.get("apellido1"));
			OCVRegistro.setApellido2(requestParm.get("apellido2"));
			OCVRegistro.setNombres(requestParm.get("nombres"));
			OCVRegistro.setSexo(requestParm.get("sexo"));
			OCVRegistro.setDistrito(requestParm.get("distrito"));
			OCVRegistro.setDireccion(requestParm.get("direccion"));
			OCVRegistro.setFecha_nac(requestParm.get("fecha_nac"));
			OCVRegistro.setTelefono1(requestParm.get("telefono1"));
			OCVRegistro.setTelefono2(requestParm.get("telefono2"));
			OCVRegistro.setAcepta_noti(Integer.parseInt(requestParm.get("acepta_noti")));
			OCVRegistro.setAcepta_correo(Integer.parseInt(requestParm.get("acepta_noti")));

			Map<String, Object> actualizaUsuario = this.dao.actualizaRegistro(OCVRegistro);
			respuesta.setnRESP_SP(((BigDecimal) actualizaUsuario.get("nRESP_SP")).intValue());
			respuesta.setcRESP_SP((String) actualizaUsuario.get("cRESP_SP"));
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.actualizaOCVRegistro()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP("");
		return respuesta;
	}

	@Override
	public Respuesta obtenerDatosCliente(Map<String, String> requestParm) {
		OCVRegistro datosCliente = new OCVRegistro();
		Respuesta respuesta = new Respuesta();
		try {
			int idCliente = Integer.parseInt(requestParm.get("id_cliente"));

			Map<String, Object> queryResp = this.dao.obtenerDatosCliente(idCliente);
			BigDecimal iCast = (BigDecimal) queryResp.get("nRESP_SP");
			if (iCast.intValue() == 1) {
				List<Map<String, Object>> lista = (List<Map<String, Object>>) queryResp.get("IO_CURSOR");
				for (Map<String, Object> map : lista) {
					datosCliente.setTipo_soli(map.get("ID_TIPOSOLICITANTE") == null ? 0
							: ((BigDecimal) map.get("ID_TIPOSOLICITANTE")).intValue());
					datosCliente.setTipo_docu(((BigDecimal) map.get("TIPO_DOCUMENTO")).intValue());
					datosCliente.setNro_doc((String) map.get("NUM_DOCUMENTO"));
					datosCliente.setApellido1(map.get("APELLIDO1") == null ? "" : map.get("APELLIDO1").toString());
					datosCliente.setApellido2(map.get("APELLIDO2") == null ? "" : map.get("APELLIDO2").toString());
					datosCliente.setNombres((String) map.get("NOMBRES"));
					datosCliente.setSexo(map.get("SEXO") == null ? "" : map.get("SEXO").toString());
					datosCliente.setDistrito(map.get("DISTRITO") == null ? "" : map.get("DISTRITO").toString());
					datosCliente.setDireccion(map.get("DIRECCION") == null ? "" : map.get("DIRECCION").toString());
					datosCliente.setFecha_nac(map.get("F_NAC") == null ? "" : map.get("F_NAC").toString());
					datosCliente.setTelefono1((String) map.get("TELEFONO1"));
					datosCliente.setTelefono2(map.get("TELEFONO2") == null ? "" : map.get("TELEFONO2").toString());
					datosCliente.setAcepta_noti(Integer.parseInt((String) map.get("ACEPTANOTIFICACION")));
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iCast.intValue());
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.obtenerDatosCliente()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(datosCliente);
		return respuesta;
	}

	@Override
	public Respuesta validaNis(Map<String, String> requestParm) throws ParseException, Exception {
		Respuesta respuesta = new Respuesta();
		try {
			int nis_rad = Integer.parseInt(requestParm.get("nis_rad"));

			Map<String, Object> map = this.dao.validaNIS(nis_rad);
			respuesta.setnRESP_SP(((BigDecimal) map.get("nRESP_SP")).intValue());
			respuesta.setcRESP_SP((String) map.get("cRESP_SP"));
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.validaNis()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP("");
		return respuesta;
	}

	@Override
	public Respuesta validaCorreo(String correo) {
		Respuesta respuesta = new Respuesta(0, "");

		try {
			Respuesta mapFormato = this.util.validaFormatoCorreo(correo);
			int iCast = mapFormato.getnRESP_SP();
			respuesta.setcRESP_SP(mapFormato.getcRESP_SP());
			respuesta.setnRESP_SP(iCast);
			if (iCast != 0) {
				Map<String, Object> map = this.dao.validaCorreo(correo);
				BigDecimal bdCast = (BigDecimal) map.get("nRESP_SP");
				respuesta.setcRESP_SP((String) map.get("cRESP_SP"));
				respuesta.setnRESP_SP(bdCast.intValue());
			}
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.validaCorreo()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP("");
		return respuesta;
	}

	@Override
	public Respuesta validaRefCobro(Map<String, String> requestParm) {
		Respuesta respuesta = new Respuesta();
		try {
			String vNis = requestParm.get("nis_rad");
			String vRefCob = requestParm.get("ref_cobro");

			Map<String, Object> out = this.dao.validaRefCobro(Integer.parseInt(vNis), vRefCob);
			respuesta.setnRESP_SP(((BigDecimal) out.get("nRESP_SP")).intValue());
			respuesta.setcRESP_SP((String) out.get("cRESP_SP"));
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.validaRefCobro()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP("");
		return respuesta;
	}

	@Override
	public Respuesta validaTipoNroDoc(Map<String, String> requestParm) {
		Respuesta respuesta = new Respuesta();
		OCVRegistro datos = new OCVRegistro();

		try {
			int iTipoDoc = Integer.parseInt(requestParm.get("tipo_doc"));
			int iNisRad = Integer.parseInt(requestParm.get("nis_rad"));
			datos.setNro_doc(requestParm.get("nro_doc"));
			datos.setApellido1("");
			datos.setApellido2("");
			datos.setNombres("");
			datos.setTelefono1("");

			Map<String, Object> queryResp = this.dao.validaTipoNroDoc(iTipoDoc,
					datos.getNro_doc(), iNisRad);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			if (bdCast.intValue() != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					if (!Objects.isNull(map.get("apellido1"))) {
						datos.setApellido1((String) map.get("apellido1"));
					}
					if (!Objects.isNull(map.get("apellido2"))) {
						datos.setApellido2((String) map.get("apellido2"));
					}
					if (!Objects.isNull(map.get("nombres"))) {
						datos.setNombres((String) map.get("nombres"));
					}
					System.out.println("telefono: " + map.get("telefono1"));
					if (!Objects.isNull(map.get("telefono1"))) {
						datos.setTelefono1(map.get("telefono1") + "");
					}
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValue());
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.validaTipoNroDoc()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(datos);
		return respuesta;
	}

	@Override
	public Respuesta actualizarEstadoUsuario(Map<String, String> requestParm) throws ParseException, Exception {
		Respuesta respuesta = new Respuesta();

		try {
			int iIdCliente = Integer.parseInt(requestParm.get("id_cliente"));
			int iIdEstado = Integer.parseInt(requestParm.get("id_estado"));

			Map<String, Object> map = this.dao.actualizarEstadoUsuario(iIdCliente, iIdEstado);
			respuesta.setnRESP_SP(((BigDecimal) map.get("nRESP_SP")).intValue());
			respuesta.setcRESP_SP((String) map.get("cRESP_SP"));
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.actualizarEstadoUsuario()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP("");
		return respuesta;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Respuesta confirmaRegistro(Map<String, String> requestParm) {
		Map<String, Object> datos = new TreeMap<String, Object>();
		Respuesta respuesta = new Respuesta();
		try {
			char vFlag_act = requestParm.get("flag_act").charAt(0);
			String vToken = requestParm.get("token");
			String cRESP_SP = "";
			int nRESP_SP = 0;

			Map<String, Object> queryResp = this.dao.validarToken(vToken, vFlag_act);
			
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			cRESP_SP = (String) queryResp.get("cRESP_SP");
			nRESP_SP = bdCast.intValue();			
			
			if (nRESP_SP != 0) {
				int idPersona = ((BigDecimal) queryResp.get("oID_CLIENTE")).intValue();
				String correo = (String) queryResp.get("oCORREO");

				Map<String, Object> queryResp2 = this.dao.actualizarEstadoUsuario(idPersona,
						Constantes.ESTADO_ACTIVO);
				bdCast = (BigDecimal) queryResp2.get("nRESP_SP");
				cRESP_SP = (String) queryResp2.get("cRESP_SP");
				nRESP_SP = bdCast.intValue();
				datos.put("correo", correo);
			}

			respuesta.setcRESP_SP(cRESP_SP);
			respuesta.setnRESP_SP(nRESP_SP);
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.confirmaRegistro()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(datos);
		return respuesta;
	}
	
//	init intermediate screen
	@Override
	public Respuesta enviarConfirmacionRegistro(Map<String, String> requestParm) throws ParseException, Exception {
		Respuesta respuesta = new Respuesta();
		Usuario usuario = new Usuario();
		try {
			usuario = dao.obtenerDatosUsuario(null, requestParm.get("correo"));
			if(usuario != null) {
				/*get support parameters*/
				Map<String, String> parametrosUrl = new HashMap<String, String>();
				Map<String, String> dato = new TreeMap<String, String>();
				dato.put(Constantes.CAMP_CATEGORIA, Constantes.CATEGORIA_URL_WEB);
				parametrosUrl = (Map<String, String>) utilService.ObtenerParametrosCategoria(dato).getbRESP();
				notificacionDao.enviarCorreoxNotificacionGeneral(Constantes.ID_NOTIFICACION_REGISTRO, usuario.getCorreo(), 
						usuario.getNombre().concat(" ").concat(usuario.getApePaterno()).concat(" ").concat(usuario.getApeMaterno()), usuario.getCorreo(), parametrosUrl.get("URL_OCV_WEB"), usuario.getToken(), null, null, null);
				respuesta.setnRESP_SP(1);
				respuesta.setcRESP_SP("Reenvio de confirmación exitosa.|Hemos enviado un correo a <strong>"+requestParm.get("correo")+"</strong> para validar tu cuenta.");
				/**/
			}else {
				respuesta.setnRESP_SP(0);
				respuesta.setcRESP_SP("Error obteniendo datos del usuario.");
			}			
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.enviarConfirmacionRegistro()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP("");
		return respuesta;
	}
//	end intermediate screen
	
	/*add code verification*/
	@Override
	public Respuesta validarCodigoConfirmacion(Map<String, String> requestParm) throws Exception {
		Respuesta respuesta = new Respuesta();
		Usuario usuario = new Usuario();
		String cRESP_SP = "";
		int nRESP_SP = 0;
		Map<String, Object> datos = new TreeMap<String, Object>();
		try {
			/*force empty value to correo*/
			datos.put("correo", "");
			/**/
			usuario = dao.obtenerDatosUsuario(null, requestParm.get("correo"));
			if(usuario != null) {
				if(usuario.getCodVerify().intValue() == Integer.valueOf(requestParm.get("codeVerify")).intValue()) {
					Map<String, Object> queryResp2 = this.dao.actualizarEstadoUsuario(usuario.getIdCliente(),
							Constantes.ESTADO_ACTIVO);
					BigDecimal bdCast = (BigDecimal) queryResp2.get("nRESP_SP");
					cRESP_SP = (String) queryResp2.get("cRESP_SP");
					nRESP_SP = bdCast.intValue();
//					/**/force without update
//					cRESP_SP = "Correo valido|correo validado correctamente";
//					nRESP_SP = 1;
					/**/
					datos.put("correo", usuario.getCorreo());
				} else {
					cRESP_SP = "Código de verificación incorrecto";
				}
			}else {
				cRESP_SP = "Error obteniendo datos del usuario.";
			}
			respuesta.setnRESP_SP(nRESP_SP);
			respuesta.setcRESP_SP(cRESP_SP);
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.validarCodigoConfirmacion()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP(datos);
		return respuesta;
	}
	
	/*add send code verify*/
	@Override
	public Respuesta enviarCodigoConfirmacion(Map<String, String> requestParm) throws Exception {
		Respuesta respuesta = new Respuesta();
		Usuario usuario = new Usuario();
		try {
			usuario = dao.obtenerDatosUsuario(null, requestParm.get("correo"));
			if(usuario != null) {
				/*generate random code*/
				Integer codeVerify = ParametrosUtil.generateCodVerify();
				/*update code verify in user*/
				dao.actualizarCodigoVerificacion(requestParm.get("correo"), codeVerify);
				/**/
				notificacionDao.enviarCorreoxNotificacionGeneral(Constantes.ID_NOTIFICACION_REGISTRO, usuario.getCorreo(), 
						usuario.getNombre().concat(" ").concat(usuario.getApePaterno()).concat(" ").concat(usuario.getApeMaterno()), usuario.getCorreo(), String.valueOf(codeVerify), null, null, null, null);
				respuesta.setnRESP_SP(1);
				respuesta.setcRESP_SP("Reenvio de confirmación exitosa.|Hemos enviado un correo a <strong>"+requestParm.get("correo")+"</strong> para validar tu cuenta.");
				/**/
			}else {
				respuesta.setnRESP_SP(0);
				respuesta.setcRESP_SP("Error obteniendo datos del usuario.");
			}			
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.enviarConfirmacionRegistro()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP("");
		return respuesta;
	}
	
	@Override
	public Respuesta obtenerDatosClienteAntiFraude(Map<String, String> requestParm) {
		DatosAntiFraude datosAntiFraude = new DatosAntiFraude();
		Respuesta respuesta = new Respuesta();
		try {
			String correoCliente = (String) requestParm.get("correo_cliente");

			Map<String, Object> queryResp = this.dao.obtenerDatosClienteAntiFraude(correoCliente);
			BigDecimal iCast = (BigDecimal) queryResp.get("nRESP_SP");
			if (iCast.intValue() == 1) {
				List<Map<String, Object>> lista = (List<Map<String, Object>>) queryResp.get("IO_CURSOR");
				for (Map<String, Object> map : lista) {
					datosAntiFraude.setTipo_docu(map.get("TIPO_DOCUMENTO") == null ? null
							: ((BigDecimal) map.get("TIPO_DOCUMENTO")).intValue());
					datosAntiFraude.setDes_documento(map.get("DES_DOCUMENTO") == null ? "" : map.get("DES_DOCUMENTO").toString());
					datosAntiFraude.setNro_doc(map.get("NUM_DOCUMENTO") == null ? "" : (String) map.get("NUM_DOCUMENTO"));
					datosAntiFraude.setCorreo(map.get("CORREO") == null ? "" : (String) map.get("CORREO"));
					datosAntiFraude.setCli_frecuente(map.get("CLT_FRECUENTE") == null ? 0
							: ((BigDecimal) map.get("CLT_FRECUENTE")).intValue());
					datosAntiFraude.setCant_dias(map.get("CANT_DIAS") == null ? 0
							: ((BigDecimal) map.get("CANT_DIAS")).intValue());
					datosAntiFraude.setTipo_registro_cliente(Constantes.TIPO_REGISTRO_CLIENTE);
				}
			}
			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iCast.intValue());
		} catch (Exception e) {
			System.out.println("OCVRegistroServiceImpl.obtenerDatosClienteAntiFraude()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(datosAntiFraude);
		return respuesta;
	}

}