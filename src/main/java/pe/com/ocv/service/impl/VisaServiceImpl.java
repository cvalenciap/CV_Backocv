package pe.com.ocv.service.impl;

import java.lang.reflect.Type;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.dao.IOCVNotificacionDAO;
import pe.com.ocv.dao.IOCVPagoEjecutadoDAO;
import pe.com.ocv.dao.IOCVUtilDAO;
import pe.com.ocv.model.PagoEjecutado;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.model.TipoTarjeta;
import pe.com.ocv.service.IEnchufateService;
import pe.com.ocv.service.IOCVAuditoriaService;
import pe.com.ocv.service.IOCVUtilService;
import pe.com.ocv.service.IVisaService;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.ConstantesDAO;
import pe.com.ocv.util.JsonUtil;
import pe.com.ocv.util.ParametrosUtil;
import pe.com.ocv.util.Util;

@Service
@SuppressWarnings("unchecked")
public class VisaServiceImpl implements IVisaService {

	@Autowired
	private IOCVUtilService utilServ;

	@Autowired
	private IEnchufateService enchufateServ;

	@Autowired
	private IOCVPagoEjecutadoDAO pagoEjecutadoDao;
	
	@Autowired
	private IOCVNotificacionDAO notificacionDao;
	
	@Autowired
	private IOCVAuditoriaService auditoriaService;
	
	@Autowired
	private IOCVUtilDAO utilDao;

	@Override
	public Respuesta getSession(Map<String, String> requestParm) {
		InputStream _is;
		String tokenSeguridad = "";
		Respuesta respuesta = new Respuesta();
		Map<String, String> dato = new TreeMap<String, String>();
		try {
			// URL Paso1
			String urlToken = requestParm.get("base_uri") + requestParm.get("uri_token");
			URL urlPaso1 = new URL(urlToken);
			HttpURLConnection conPaso1 = (HttpURLConnection) urlPaso1.openConnection();
			conPaso1 = Util.setUrlProperties(conPaso1, requestParm.get("access_key"), requestParm.get("secret_key"),
					Constantes.CONTENT_JSON, Constantes.METHOD_POST, "");

			// Verificamos si la repsuesta es correcta o incorrecta
			if (conPaso1.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = conPaso1.getInputStream();
				StringBuffer responsePaso1 = Util.getResponse(_is);
				tokenSeguridad = responsePaso1.toString();
			} else {
				/* error from server */
				_is = conPaso1.getErrorStream();
				System.out.println("Error: " + Util.getResponse(_is));
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
				respuesta.setnRESP_SP(0);
				respuesta.setbRESP(dato);
				return respuesta;
			}

			// URL Paso2
			String urlSession = requestParm.get("base_uri") + requestParm.get("uri_session")
					+ requestParm.get("merchant_id");
			URL urlPaso2 = new URL(urlSession);
			HttpURLConnection conPaso2 = (HttpURLConnection) urlPaso2.openConnection();

			JsonObject body = new JsonObject();
			double amount = Double.parseDouble(requestParm.get("amount"));
			double comision = Double.parseDouble(requestParm.get("comision"));
			body.addProperty("amount", (amount + comision));

			JsonObject antifraud = new JsonObject();
			antifraud.addProperty("clientIp", "");
			JsonObject merchantDefineData = new JsonObject();
			antifraud.add("merchantDefineData", merchantDefineData);

			body.add("antifraud", antifraud);
			body.addProperty("channel", "web");
			body.addProperty("recurrenceMaxAmount", 0);

			conPaso2 = Util.setUrlProperties(conPaso2, tokenSeguridad, Constantes.CONTENT_JSON, body.toString());

			// Verificamos si la repsuesta es correcta o incorrecta
			if (conPaso2.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = conPaso2.getInputStream();
				StringBuffer responsePaso2 = Util.getResponse(_is);
				System.out.println("respuesta paso 2: " + responsePaso2.toString());

				// Parseamos la respuesta en un objeto JSON
				JsonObject jsonObj = new Gson().fromJson(responsePaso2.toString(), JsonObject.class);
				dato.put("merchantId", requestParm.get("merchant_id"));
				dato.put("sessionKey", jsonObj.get("sessionKey").getAsString());
				dato.put("expirationTime", jsonObj.get("expirationTime").getAsString());
				dato.put("comision", "" + comision);

				// Ponemos los resultados en el objeto Respuesta
				respuesta.setcRESP_SP("Ejecución Correcta");
				respuesta.setnRESP_SP(1);
				respuesta.setbRESP(dato);
			} else {
				/* error from server */
				_is = conPaso2.getErrorStream();
				System.out.println("Error: " + Util.getResponse(_is));
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_SUCCESS);
				respuesta.setbRESP(dato);
				return respuesta;
			}

		} catch (Exception e) {
			respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
			respuesta.setbRESP(dato);
		}
		return respuesta;
	}
	
	/*adecuacion proceso pago*/
	@Override
	public Respuesta getPaymentResponse(Map<String, String> requestParm) throws GmdException {
		PagoEjecutado pago = new PagoEjecutado();
		Respuesta respuesta = new Respuesta();
		String tokenSeguridad = "";
		InputStream _is;
		boolean success = false;
		List<Map<String, Object>> listaRegistros = null;
		boolean flagErrorInsert = false;
		Integer codigoAgencia = 0;
		List<TipoTarjeta> listaTarjeta = new ArrayList<TipoTarjeta>();
		List<PagoEjecutado> listaPrevia = new ArrayList<PagoEjecutado>();
		try {
			/*validacion inicial de registros previos*/
			Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
			listaRegistros = JsonUtil.convertirCadenaJsonALista(requestParm.get("listaRegistros"), listType);
			if(listaRegistros == null || listaRegistros.size() == 0) {
				throw new GmdException("Error obteniendo la lista de registros de los pagos.");
			}
			
			// URL Paso1
			String urlToken = requestParm.get("base_uri") + requestParm.get("uri_token");
			URL urlPaso1 = new URL(urlToken);
			HttpURLConnection conPaso1 = (HttpURLConnection) urlPaso1.openConnection();
			conPaso1 = Util.setUrlProperties(conPaso1, requestParm.get("access_key"), requestParm.get("secret_key"),
					Constantes.CONTENT_JSON, Constantes.METHOD_POST, "");
			// Verificamos si la repsuesta es correcta o incorrecta
			if (conPaso1.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = conPaso1.getInputStream();
				StringBuffer responsePaso1 = Util.getResponse(_is);
				tokenSeguridad = responsePaso1.toString();

			} else {
				/* error from server */
				_is = conPaso1.getErrorStream();
				System.out.println("Error: " + Util.getResponse(_is));
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
				respuesta.setbRESP(pago);
				return respuesta;
			}

			// Paso Authorization
			String url = requestParm.get("base_uri") + requestParm.get("uri_authorization")
					+ requestParm.get("merchant_id");
			URL myURL = new URL(url);
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();

			// Añadimos parametros del servicio
			JsonObject body = new JsonObject();
			body.addProperty("captureType", "manual");
			body.addProperty("channel", requestParm.get("channel"));
			body.addProperty("countable", true);
			JsonObject order = new JsonObject();
			order.addProperty("amount", requestParm.get("amount"));
			order.addProperty("currency", "PEN");
			order.addProperty("purchaseNumber", requestParm.get("liquidacion"));
			order.addProperty("tokenId", requestParm.get("transactionToken"));
			body.add("order", order);

			myURLConnection = Util.setUrlProperties(myURLConnection, tokenSeguridad, Constantes.CONTENT_JSON,
					body.toString());

			// Verificamos si la repsuesta es correcta o incorrecta
			if (myURLConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = myURLConnection.getInputStream();
				success = true;
			} else {
				/* error from server */
				_is = myURLConnection.getErrorStream();
			}

			// Obtenemos la respuesta
			StringBuffer response = Util.getResponse(_is);
			System.out.println("response authorization visa V3: " + response.toString());

			// Parseamos la respuesta en un objeto JSON
			JsonObject jsonObj = new Gson().fromJson(response.toString(), JsonObject.class);
			
			if (success) {
				/*if catch exception insert/update register OCV_SIMBOLO_VAR_REG*/
				flagErrorInsert = true;
				
				/*setear respuesta visa success*/
				respuesta.setcRESP_SP(Constantes.MENSAJE_SUCCESS_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_SUCCESS);
				
				// Obtenemos header de respuesta VISA
				JsonObject header = jsonObj.get("header").getAsJsonObject();

				// Obtenemos dataMap de respuesta VISA
				JsonObject dataMap = jsonObj.get("dataMap").getAsJsonObject();

				// Añadimos los atributos del pago
				pago.setTrxId(header.get("ecoreTransactionUUID").getAsString());
				pago.setErrorCode(dataMap.get("ACTION_CODE").getAsString());
				pago.setErrorMsg(dataMap.get("STATUS").getAsString());
				String trxDate = dataMap.get("TRANSACTION_DATE").getAsString();
				String fecha = "20" + trxDate.substring(0, 2) + "-" + trxDate.substring(2, 4) + "-"
						+ trxDate.substring(4, 6);
				String hora = trxDate.substring(6, 8) + ":" + trxDate.substring(8, 10) + ":" + trxDate.substring(10);
				pago.setFecha(fecha);
				pago.setHora(hora);
				pago.setDescripcion(dataMap.get("ACTION_DESCRIPTION").getAsString());
				pago.setNumOperacion(dataMap.get("TRACE_NUMBER").getAsString());
				String tarjeta = dataMap.get("CARD").getAsString();
				pago.setNumTarjeta(tarjeta.substring(tarjeta.length() - 5));
				pago.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));
				pago.setNisRad(Integer.parseInt(requestParm.get("nis_rad")));
				
				//setear codigo de agencia respuesta
				listaTarjeta = utilDao.obtenerDatosTarjeta(null, dataMap.get("BRAND").getAsString(), null);
				if(listaTarjeta != null && listaTarjeta.size() > 0) {
					codigoAgencia = listaTarjeta.get(0).getCodigoAgencia();
				}
				
				//Obtener parametros enchufate
				Map<String, String> datoCat = new TreeMap<String, String>();
				datoCat.put("categoria", "DATOS_ENCHUFATE");
				Map<String, String> datosEnchufate = (Map<String, String>) utilServ.ObtenerParametrosCategoria(datoCat)
						.getbRESP();
				
				if(datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
					datoCat.put("estado", Constantes.ST_REGIST_OCV_ENCHUFATE);
				} else if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_JOB)) {
					datoCat.put("estado", Constantes.ST_REGIST_OCV);
				} else {
					throw new GmdException("Error al obtener el parámetro FLAG_MODO del proceso.");
				}
				
				/*realizar inserción inicial OCV_SIMBOLO_VAR_REG*/
				for(int i=0; i<listaRegistros.size(); i++) {
					PagoEjecutado registroPrevio = new PagoEjecutado();
					registroPrevio.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));
					registroPrevio.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
					registroPrevio.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
					registroPrevio.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
					registroPrevio.setMonto((Double) listaRegistros.get(i).get("deuda"));
					registroPrevio.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
					registroPrevio.setEstado(datoCat.get("estado"));
					registroPrevio.setCodAgencia(codigoAgencia);
					System.out.println(JsonUtil.convertirObjetoACadenaJson(registroPrevio));
					pagoEjecutadoDao.insertarRegistroPrevio(registroPrevio);
				}
				
				/*insercion registros OCV_PAGO_EJECUTADO*/
				for(int i=0; i<listaRegistros.size(); i++) {
					PagoEjecutado prevHelp = new PagoEjecutado();
					prevHelp = (PagoEjecutado) ParametrosUtil.cloneObject(pago);
					prevHelp.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));
					prevHelp.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
					prevHelp.setMonto((Double) listaRegistros.get(i).get("deuda"));
					prevHelp.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
					prevHelp.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
					prevHelp.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
					prevHelp.setCodAgencia(codigoAgencia);
					pagoEjecutadoDao.insertarPagoEjecutadoSinCorreo(prevHelp);
					listaPrevia.add(prevHelp);
				}
				
				/*envío de correo*/
				// Insertamos el resultado de atributos finales al pago
				pago.setMonto(Double.parseDouble(requestParm.get("amount")));
				pago.setCorreo(requestParm.get("correo"));
				
				//envio de correo por respuesta satisfactoria
				if(pago.getErrorCode().equals(String.valueOf(Constantes.PAYMENT_SUCCESS))) {
					Map<String, Object> respNotificacion = notificacionDao.enviarCorreoxNotificacionGeneral(Constantes.ID_NOTIFICACION_PAGO, pago.getCorreo(), pago.getNumOperacion(), pago.getCorreo(), pago.getNumTarjeta(), ((pago.getFecha()).concat(" ")).concat(pago.getHora()), ParametrosUtil.roundDecimal(pago.getMonto(),2), String.valueOf(pago.getNumLiquidacion()), pago.getTrxId());
					if(Integer.valueOf(respNotificacion.get(ConstantesDAO.COD_RESPUESTA).toString()) != Constantes.ESTADO_SUCCESS) {
						System.err.println("Ocurrió un error en el envío de correo");
					}
				}
				
				/*si FLAG_MODO enchufate ejecuta pago por servicio*/
				if(datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
					// Obtenemos token de Enchufate
					datosEnchufate.put("access_token", enchufateServ.getToken(datosEnchufate));
					
					// Seteamos datos de pago de liquidación
					datosEnchufate.put("url", datosEnchufate.get("base_uri") + datosEnchufate.get("pagar_liqui_uri"));
					datosEnchufate.put("numeroLiquidacion", "" + pago.getNumLiquidacion());
					datosEnchufate.put("nis_rad", "" + pago.getNisRad());
					datosEnchufate.put("numeroTarjeta", pago.getNumTarjeta());
					datosEnchufate.put("fechaPago", pago.getFecha());
					datosEnchufate.put("horaPago", pago.getHora());
					datosEnchufate.put("numOperacionTar", pago.getNumOperacion());

					// Ejecutamos el servicio de pagos de Enchufate
					respuesta = enchufateServ.pagarLiquidacion(datosEnchufate);
				}
				
				/*si FLAG_MODO enchufate ejecuta actualizacion listaPrevia*/
				if(datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE) && respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS) {
					for(PagoEjecutado pagoPrev : listaPrevia) {
						pagoPrev.setEstado(Constantes.ST_PAGADO_OCV);
						pagoEjecutadoDao.actualizarRegistroPrevio(pagoPrev);
					}
				}		
				
			} else {
				/*setear respuesta visa failure*/
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);	
				
				pago.setTrxId(UUID.randomUUID().toString());
				pago.setErrorCode(jsonObj.get("errorCode").getAsString());
				pago.setErrorMsg(jsonObj.get("errorMessage").getAsString());
				/*setear valor de liquidacion*/
				pago.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));

				// Obtenemos data de respuesta error VISA
				JsonObject data = jsonObj.get("data").getAsJsonObject();

				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
				Date now = new Date();
				String fecha = sdfDate.format(now);
				String hora = sdfHour.format(now);
				pago.setFecha(fecha);
				pago.setHora(hora);
				pago.setDescripcion(data.get("ACTION_DESCRIPTION").getAsString());
				//setear valor total monto
				pago.setMonto(Double.parseDouble(requestParm.get("amount")));
				//insercion proceso fallido de visa
				pagoEjecutadoDao.insertarPagoEjecutadoSinCorreo(pago);						
			}
			
			/*adicion de log auditoria pago*/
			int tipoAuditoria = respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS ? Constantes.AUDITORIA_PAGO_SUCCESS : Constantes.AUDITORIA_PAGO_FAILURE;
			auditoriaService.registrarLog(requestParm.get("auth_correo"), requestParm.get("flagChannel"), tipoAuditoria, Integer.parseInt(requestParm.get("nis_rad")));
//			/**/
		} catch (Exception e) {
			//adicion log auditoria pago fallido
			auditoriaService.registrarLog(requestParm.get("auth_correo"), requestParm.get("flagChannel"), Constantes.AUDITORIA_PAGO_FAILURE, Integer.parseInt(requestParm.get("nis_rad")));
			/*insercion OCV_SIMBOLO_VAR_REG*/
			//validación de existencia de registros iniciales
			if(listaRegistros != null && listaRegistros.size() != 0 && flagErrorInsert) {
				//obtener registros de listaPrevia: registros existentes
				for(int i=0; i<listaRegistros.size(); i++) {
					boolean flagExist = false;
					for(PagoEjecutado pagoPrevio : listaPrevia) {
						if(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")) == pagoPrevio.getSimboloVar() && Long.parseLong(requestParm.get("liquidacion")) == pago.getNumLiquidacion()) {
							flagExist = true;
							pagoPrevio.setEstado(Constantes.ST_PAGO_ERROR_OCV);
							pagoEjecutadoDao.actualizarRegistroPrevio(pagoPrevio);
							break;
						}	
					}
					if(!flagExist) {
						PagoEjecutado registroPrevio = new PagoEjecutado();
						registroPrevio.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));
						registroPrevio.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
						registroPrevio.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
						registroPrevio.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
						registroPrevio.setMonto((Double) listaRegistros.get(i).get("deuda"));
						registroPrevio.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
						registroPrevio.setEstado(Constantes.ST_PAGO_ERROR_OCV);
						registroPrevio.setCodAgencia(codigoAgencia);
						System.out.println(JsonUtil.convertirObjetoACadenaJson(registroPrevio));
						pagoEjecutadoDao.insertarRegistroPrevio(registroPrevio);
					} 
				}
			}			
			respuesta.setcRESP_SP("Error realizando el pago: " + e);
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
		}
		respuesta.setbRESP(pago);
		return respuesta;
	}
	
//	inicio cvalenciap
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Respuesta insertarPagoMovil(Map<String, String> requestParm) throws Exception {
		Respuesta respuesta = new Respuesta(0, "");
		PagoEjecutado pago = new PagoEjecutado();
		try {
			if(requestParm.get("estadoRptVISA").equals(Constantes.REQUEST_CODE_MOVIL_SUCCESS)) {
				pago.setTrxId(requestParm.get("TRANSACTION_ID"));
				pago.setErrorCode(requestParm.get("ACTION_CODE"));
				pago.setErrorMsg(requestParm.get("STATUS"));
				String trxDate = requestParm.get("TRANSACTION_DATE");
				String fecha = "20" + trxDate.substring(0, 2) + "-" + trxDate.substring(2, 4) + "-"
						+ trxDate.substring(4, 6);
				String hora = trxDate.substring(6, 8) + ":" + trxDate.substring(8, 10) + ":" + trxDate.substring(10);
				pago.setFecha(fecha);
				pago.setHora(hora);
				pago.setDescripcion(requestParm.get("ACTION_DESCRIPTION"));
				pago.setNumOperacion(requestParm.get("TRACE_NUMBER"));
				String tarjeta = requestParm.get("CARD");
				pago.setNumTarjeta(tarjeta.substring(tarjeta.length() - 5));
				
				// Valores para enchúfate
				pago.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));
				pago.setNisRad(Integer.parseInt(requestParm.get("nis_rad")));
							
//				verificación de pago satisfactorio
				if(pago.getErrorCode().equals(Constantes.PAYMENT_SUCCESS)) {
					pago.setEstado(Constantes.FLAG_SEND_MAIL);
				}
				
				/*insercion de pago en auditoria*/
				auditoriaService.registrarLog(requestParm.get("correo"), requestParm.get("flagChannel"), Constantes.AUDITORIA_PAGO_SUCCESS, Integer.parseInt(requestParm.get("nis_rad")));
//				daoAudit.registrarLog(requestParm.get("correo"), Util.getValueConstantAuditoria(requestParm.get("flagChannel"), Constantes.AUDITORIA_PAGO), Integer.parseInt(requestParm.get("nis_rad")));
				/**/
				
			}else if(requestParm.get("estadoRptVISA").equals(Constantes.REQUEST_CODE_MOVIL_FAILURE)){
				pago.setTrxId(requestParm.get("TRANSACTION_ID") != null ? requestParm.get("TRANSACTION_ID") : UUID.randomUUID().toString());
				pago.setErrorCode(requestParm.get("ACTION_CODE"));
				pago.setErrorMsg(requestParm.get("STATUS"));

				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
				Date now = new Date();
				String fecha = sdfDate.format(now);
				String hora = sdfHour.format(now);
				pago.setFecha(fecha);
				pago.setHora(hora);
				pago.setDescripcion(requestParm.get("ACTION_DESCRIPTION"));
			}
			
			//Parámetros Correo
			pago.setMonto(Double.parseDouble(requestParm.get("amount")));
			pago.setCorreo(requestParm.get("correo"));			
			
//			adecuacion particion proceso de insercion de pago y envio de Correo
//			insercion de registros previos
			Map<String, Object> queryResp = new HashMap<String, Object>();
			List<PagoEjecutado> listaPrevia = pagoEjecutadoDao.obtenerRegistrosPrevios(pago.getNumLiquidacion(), null);
			for(PagoEjecutado pagoPrev : listaPrevia) {
				PagoEjecutado prevHelp = new PagoEjecutado();
				prevHelp = (PagoEjecutado) ParametrosUtil.cloneObject(pago);
				prevHelp.setNumLiquidacion(pagoPrev.getNumLiquidacion());
				prevHelp.setSimboloVar(pagoPrev.getSimboloVar());
				prevHelp.setMonto(pagoPrev.getMonto());
				prevHelp.setFechaEmision(pagoPrev.getFechaEmision());
				prevHelp.setFechaVencimiento(pagoPrev.getFechaVencimiento());
				pagoEjecutadoDao.insertarPagoEjecutadoSinCorreo(prevHelp);
			}
			
			//envio de correo por respuesta satisfactoria
			if(pago.getErrorCode().equals(String.valueOf(Constantes.PAYMENT_SUCCESS)) && pago.getEstado().equals(Constantes.FLAG_SEND_MAIL)) {
				Map<String, Object> respNotificacion = notificacionDao.enviarCorreoxNotificacionGeneral(Constantes.ID_NOTIFICACION_PAGO, pago.getCorreo(), pago.getNumOperacion(), pago.getCorreo(), pago.getNumTarjeta(), ((pago.getFecha()).concat(" ")).concat(pago.getHora()), ParametrosUtil.roundDecimal(pago.getMonto(),2), String.valueOf(pago.getNumLiquidacion()), pago.getTrxId());
				if(Integer.valueOf(respNotificacion.get(ConstantesDAO.COD_RESPUESTA).toString()) != Constantes.ESTADO_SUCCESS) {
					System.err.println("Ocurrió un error en el envío de correo");
				}
			}
			
//			resultado de ejecución query
			respuesta.setcRESP_SP(queryResp.get("cRESP_SP").toString());
			respuesta.setnRESP_SP(Constantes.ESTADO_SUCCESS);
		} catch (Exception e) {
			System.out.println("VisaServiceImpl.insertarPagoMovil()");
			System.err.println("Ocurrió un error en la insercion : " + e);
			respuesta.setcRESP_SP("Ocurrió un error en la insercion : " + e);
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
		}
		respuesta.setbRESP(Constantes.RESPONSE_INSERT_MOVIL);
		return respuesta;
	}
//	fin cvalenciap
	
	/*actualizacion mastecard*/
	@Override
	public Respuesta getPaymentResponseV2(Map<String, String> requestParm) throws GmdException {
		PagoEjecutado pago = new PagoEjecutado();
		Respuesta respuesta = new Respuesta();
		String tokenSeguridad = "";
		InputStream _is;
		boolean success = false;
		List<Map<String, Object>> listaRegistros = null;
		boolean flagErrorInsert = false;
		Integer codigoAgencia = 0;
		List<TipoTarjeta> listaTarjeta = new ArrayList<TipoTarjeta>();
		List<PagoEjecutado> listaPrevia = new ArrayList<PagoEjecutado>();
		Integer liquidacionEnchufate = 0;
		Integer liquidacionJob = 0;
		Integer liquidacionFinal = 0;
		try {
			/*set values liquidacion*/
			liquidacionEnchufate = Integer.valueOf(requestParm.get("liquidacionEnchufate"));
			liquidacionJob = Integer.valueOf(requestParm.get("liquidacionJob"));	
			liquidacionFinal = liquidacionEnchufate == 0 ? liquidacionJob : liquidacionEnchufate;
			
			/*validacion inicial de registros previos*/
			Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
			listaRegistros = JsonUtil.convertirCadenaJsonALista(requestParm.get("listaRegistros"), listType);
			if(listaRegistros == null || listaRegistros.size() == 0) {
				throw new GmdException("Error obteniendo la lista de registros de los pagos.");
			}
			
			// URL Paso1
			String urlToken = requestParm.get("base_uri") + requestParm.get("uri_token");
			URL urlPaso1 = new URL(urlToken);
			HttpURLConnection conPaso1 = (HttpURLConnection) urlPaso1.openConnection();
			conPaso1 = Util.setUrlProperties(conPaso1, requestParm.get("access_key"), requestParm.get("secret_key"),
					Constantes.CONTENT_JSON, Constantes.METHOD_POST, "");
			// Verificamos si la repsuesta es correcta o incorrecta
			if (conPaso1.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = conPaso1.getInputStream();
				StringBuffer responsePaso1 = Util.getResponse(_is);
				tokenSeguridad = responsePaso1.toString();

			} else {
				/* error from server */
				_is = conPaso1.getErrorStream();
				System.out.println("Error: " + Util.getResponse(_is));
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
				respuesta.setbRESP(pago);
				return respuesta;
			}

			// Paso Authorization
			String url = requestParm.get("base_uri") + requestParm.get("uri_authorization")
					+ requestParm.get("merchant_id");
			URL myURL = new URL(url);
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();

			// Añadimos parametros del servicio
			JsonObject body = new JsonObject();
			body.addProperty("captureType", "manual");
			body.addProperty("channel", requestParm.get("channel"));
			body.addProperty("countable", true);
			JsonObject order = new JsonObject();
			order.addProperty("amount", requestParm.get("amount"));
			order.addProperty("currency", "PEN");
			order.addProperty("purchaseNumber", liquidacionFinal);
			order.addProperty("tokenId", requestParm.get("transactionToken"));
			body.add("order", order);

			myURLConnection = Util.setUrlProperties(myURLConnection, tokenSeguridad, Constantes.CONTENT_JSON,
					body.toString());

			// Verificamos si la repsuesta es correcta o incorrecta
			if (myURLConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = myURLConnection.getInputStream();
				success = true;
			} else {
				/* error from server */
				_is = myURLConnection.getErrorStream();
			}

			// Obtenemos la respuesta
			StringBuffer response = Util.getResponse(_is);
			System.out.println("response authorization visa V3: " + response.toString());

			// Parseamos la respuesta en un objeto JSON
			JsonObject jsonObj = new Gson().fromJson(response.toString(), JsonObject.class);
			
			if (success) {
				/*if catch exception insert/update register OCV_SIMBOLO_VAR_REG*/
				flagErrorInsert = true;
				
				/*setear respuesta visa success*/
				respuesta.setcRESP_SP(Constantes.MENSAJE_SUCCESS_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_SUCCESS);
				
				// Obtenemos header de respuesta VISA
				JsonObject header = jsonObj.get("header").getAsJsonObject();

				// Obtenemos dataMap de respuesta VISA
				JsonObject dataMap = jsonObj.get("dataMap").getAsJsonObject();

				// Añadimos los atributos del pago
				pago.setTrxId(header.get("ecoreTransactionUUID").getAsString());
				pago.setErrorCode(dataMap.get("ACTION_CODE").getAsString());
				pago.setErrorMsg(dataMap.get("STATUS").getAsString());
				String trxDate = dataMap.get("TRANSACTION_DATE").getAsString();
				String fecha = "20" + trxDate.substring(0, 2) + "-" + trxDate.substring(2, 4) + "-"
						+ trxDate.substring(4, 6);
				String hora = trxDate.substring(6, 8) + ":" + trxDate.substring(8, 10) + ":" + trxDate.substring(10);
				pago.setFecha(fecha);
				pago.setHora(hora);
				pago.setDescripcion(dataMap.get("ACTION_DESCRIPTION").getAsString());
				pago.setNumOperacion(dataMap.get("TRACE_NUMBER").getAsString());
				String tarjeta = dataMap.get("CARD").getAsString();
				pago.setNumTarjeta(tarjeta.substring(tarjeta.length() - 5));
				pago.setNisRad(Integer.parseInt(requestParm.get("nis_rad")));
				
				//setear codigo de agencia respuesta
				listaTarjeta = utilDao.obtenerDatosTarjeta(null, dataMap.get("BRAND").getAsString(), null);
				if(listaTarjeta != null && listaTarjeta.size() > 0) {
					codigoAgencia = listaTarjeta.get(0).getCodigoAgencia();
				}
				
				//Obtener parametros enchufate
				Map<String, String> datoCat = new TreeMap<String, String>();
				datoCat.put("categoria", "DATOS_ENCHUFATE");
				Map<String, String> datosEnchufate = (Map<String, String>) utilServ.ObtenerParametrosCategoria(datoCat)
						.getbRESP();
				
				/*evaluacion FLAG_MODO*/
				if(datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
					if(dataMap.get("BRAND").getAsString().equals(Constantes.BRAND_VISA)) {
						liquidacionFinal = liquidacionEnchufate;
						datoCat.put("estado", Constantes.ST_REGIST_OCV_ENCHUFATE);
					} else if(dataMap.get("BRAND").getAsString().equals(Constantes.BRAND_MASTERCARD)) {
						liquidacionFinal = liquidacionJob;
						datoCat.put("estado", Constantes.ST_REGIST_OCV);
					} else {
						throw new GmdException("Error al obtener el valor BRAND del proceso de pago.");
					}
				} else if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_JOB)) {
					datoCat.put("estado", Constantes.ST_REGIST_OCV);
					liquidacionFinal = liquidacionJob;
				} else {
					throw new GmdException("Error al obtener el parámetro FLAG_MODO del proceso.");
				}
				pago.setNumLiquidacion(liquidacionFinal);
				/**/
				
				/*realizar inserción inicial OCV_SIMBOLO_VAR_REG*/
				for(int i=0; i<listaRegistros.size(); i++) {
					PagoEjecutado registroPrevio = new PagoEjecutado();
					registroPrevio.setNumLiquidacion(liquidacionFinal);
					registroPrevio.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
					registroPrevio.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
					registroPrevio.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
					registroPrevio.setMonto((Double) listaRegistros.get(i).get("deuda"));
					registroPrevio.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
					registroPrevio.setEstado(datoCat.get("estado"));
					registroPrevio.setCodAgencia(codigoAgencia);
					System.out.println(JsonUtil.convertirObjetoACadenaJson(registroPrevio));
					pagoEjecutadoDao.insertarRegistroPrevio(registroPrevio);
				}
				
				/*insercion registros OCV_PAGO_EJECUTADO*/
				for(int i=0; i<listaRegistros.size(); i++) {
					PagoEjecutado prevHelp = new PagoEjecutado();
					prevHelp = (PagoEjecutado) ParametrosUtil.cloneObject(pago);
					prevHelp.setNumLiquidacion(liquidacionFinal);
					prevHelp.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
					prevHelp.setMonto((Double) listaRegistros.get(i).get("deuda"));
					prevHelp.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
					prevHelp.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
					prevHelp.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
					prevHelp.setCodAgencia(codigoAgencia);
					pagoEjecutadoDao.insertarPagoEjecutadoSinCorreo(prevHelp);
					listaPrevia.add(prevHelp);
				}
				
				/*envío de correo*/
				// Insertamos el resultado de atributos finales al pago
				pago.setMonto(Double.parseDouble(requestParm.get("amount")));
				pago.setCorreo(requestParm.get("correo"));
				
				//envio de correo por respuesta satisfactoria
				if(pago.getErrorCode().equals(String.valueOf(Constantes.PAYMENT_SUCCESS))) {
					Map<String, Object> respNotificacion = notificacionDao.enviarCorreoxNotificacionGeneral(Constantes.ID_NOTIFICACION_PAGO, pago.getCorreo(), pago.getNumOperacion(), pago.getCorreo(), pago.getNumTarjeta(), ((pago.getFecha()).concat(" ")).concat(pago.getHora()), ParametrosUtil.roundDecimal(pago.getMonto(),2), String.valueOf(pago.getNumLiquidacion()), pago.getTrxId());
					if(Integer.valueOf(respNotificacion.get(ConstantesDAO.COD_RESPUESTA).toString()) != Constantes.ESTADO_SUCCESS) {
						System.err.println("Ocurrió un error en el envío de correo");
					}
				}
				
				/*ejecucion enchufate solo para BRAND VISA*/
				if(dataMap.get("BRAND").getAsString().equals(Constantes.BRAND_VISA)) {
					/*si FLAG_MODO enchufate ejecuta pago por servicio*/
					if(datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
						// Obtenemos token de Enchufate
						datosEnchufate.put("access_token", enchufateServ.getToken(datosEnchufate));
						
						// Seteamos datos de pago de liquidación
						datosEnchufate.put("url", datosEnchufate.get("base_uri") + datosEnchufate.get("pagar_liqui_uri"));
						datosEnchufate.put("numeroLiquidacion", "" + pago.getNumLiquidacion());
						datosEnchufate.put("nis_rad", "" + pago.getNisRad());
						datosEnchufate.put("numeroTarjeta", pago.getNumTarjeta());
						datosEnchufate.put("fechaPago", pago.getFecha());
						datosEnchufate.put("horaPago", pago.getHora());
						datosEnchufate.put("numOperacionTar", pago.getNumOperacion());

						// Ejecutamos el servicio de pagos de Enchufate
						respuesta = enchufateServ.pagarLiquidacion(datosEnchufate);
					}
					
					/*si FLAG_MODO enchufate ejecuta actualizacion listaPrevia*/
					if(datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE) && respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS) {
						for(PagoEjecutado pagoPrev : listaPrevia) {
							pagoPrev.setEstado(Constantes.ST_PAGADO_OCV);
							pagoEjecutadoDao.actualizarRegistroPrevio(pagoPrev);
						}
					}
				}
			} else {
				/*setear respuesta visa failure*/
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);	
				
				pago.setTrxId(UUID.randomUUID().toString());
				pago.setErrorCode(jsonObj.get("errorCode").getAsString());
				pago.setErrorMsg(jsonObj.get("errorMessage").getAsString());
				/*setear valor de liquidacion*/
				pago.setNumLiquidacion(liquidacionFinal);

				// Obtenemos data de respuesta error VISA
				JsonObject data = jsonObj.get("data").getAsJsonObject();

				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
				Date now = new Date();
				String fecha = sdfDate.format(now);
				String hora = sdfHour.format(now);
				pago.setFecha(fecha);
				pago.setHora(hora);
				pago.setDescripcion(data.get("ACTION_DESCRIPTION").getAsString());
				//setear valor total monto
				pago.setMonto(Double.parseDouble(requestParm.get("amount")));
				//insercion proceso fallido de visa
				pagoEjecutadoDao.insertarPagoEjecutadoSinCorreo(pago);						
			}
			
			/*adicion de log auditoria pago*/
			int tipoAuditoria = respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS ? Constantes.AUDITORIA_PAGO_SUCCESS : Constantes.AUDITORIA_PAGO_FAILURE;
			auditoriaService.registrarLog(requestParm.get("auth_correo"), requestParm.get("flagChannel"), tipoAuditoria, Integer.parseInt(requestParm.get("nis_rad")));
//			/**/
		} catch (Exception e) {
			//adicion log auditoria pago fallido
			auditoriaService.registrarLog(requestParm.get("auth_correo"), requestParm.get("flagChannel"), Constantes.AUDITORIA_PAGO_FAILURE, Integer.parseInt(requestParm.get("nis_rad")));
			/*insercion OCV_SIMBOLO_VAR_REG*/
			//validación de existencia de registros iniciales
			if(listaRegistros != null && listaRegistros.size() != 0 && flagErrorInsert) {
				//obtener registros de listaPrevia: registros existentes
				for(int i=0; i<listaRegistros.size(); i++) {
					boolean flagExist = false;
					for(PagoEjecutado pagoPrevio : listaPrevia) {
						if(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")) == pagoPrevio.getSimboloVar() && liquidacionFinal == pago.getNumLiquidacion()) {
							flagExist = true;
							pagoPrevio.setEstado(Constantes.ST_PAGO_ERROR_OCV);
							pagoEjecutadoDao.actualizarRegistroPrevio(pagoPrevio);
							break;
						}	
					}
					if(!flagExist) {
						PagoEjecutado registroPrevio = new PagoEjecutado();
						registroPrevio.setNumLiquidacion(liquidacionFinal);
						registroPrevio.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
						registroPrevio.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
						registroPrevio.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
						registroPrevio.setMonto((Double) listaRegistros.get(i).get("deuda"));
						registroPrevio.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
						registroPrevio.setEstado(Constantes.ST_PAGO_ERROR_OCV);
						registroPrevio.setCodAgencia(codigoAgencia);
						System.out.println(JsonUtil.convertirObjetoACadenaJson(registroPrevio));
						pagoEjecutadoDao.insertarRegistroPrevio(registroPrevio);
					} 
				}
			}			
			respuesta.setcRESP_SP("Error realizando el pago: " + e);
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
		}
		respuesta.setbRESP(pago);
		return respuesta;
	}
	
}
