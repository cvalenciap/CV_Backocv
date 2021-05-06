package pe.com.ocv.service.impl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import pe.com.ocv.service.IOCVPagoEjecutadoService;
import pe.com.ocv.service.IOCVUtilService;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.ConstantesDAO;
import pe.com.ocv.util.JsonUtil;
import pe.com.ocv.util.ParametrosUtil;

@Service
public class OCVPagoEjecutadoServiceImpl implements IOCVPagoEjecutadoService {

	@Autowired
	private IOCVUtilDAO utilDao;

	@Autowired
	private IOCVPagoEjecutadoDAO dao;

	@Autowired
	private IOCVNotificacionDAO notificacionDao;

	@Autowired
	private IOCVAuditoriaService auditoriaService;

	@Autowired
	private IEnchufateService enchufateServ;

	/* get support parameters */
	@Autowired
	private IOCVUtilService utilService;
	/**/

	@SuppressWarnings("unchecked")
	@Override
	public Respuesta ObtenerResultadoPago(Map<String, String> requestParm) {
		Respuesta respuesta = new Respuesta();
		Map<String, Object> codigos = new TreeMap<String, Object>();
		try {
			Map<String, Object> queryResp = dao.obtenerResultadoPago(requestParm.get("trxID"));
			BigDecimal nresp = (BigDecimal) queryResp.get("nRESP_SP");
			if (nresp.intValueExact() == 1) {
				codigos.put("cod_visa", ((BigDecimal) queryResp.get("oCOD_VISA")).intValueExact());
				codigos.put("cod_enchufate", ((BigDecimal) queryResp.get("oCOD_ENCHUFATE")).intValueExact());
			}
			/* get support parameters */
			Map<String, String> parametrosSoporte = new HashMap<String, String>();
			Map<String, String> dato = new TreeMap<String, String>();
			dato.put(Constantes.CAMP_CATEGORIA, Constantes.CATEGORIA_SOPORTE);
			parametrosSoporte = (Map<String, String>) utilService.ObtenerParametrosCategoria(dato).getbRESP();
			codigos.put("correo_soporte", parametrosSoporte.get("correo_soporte"));
			codigos.put("numero_soporte", parametrosSoporte.get("numero_soporte"));
			/**/
			respuesta.setnRESP_SP(nresp.intValueExact());
//			respuesta.setnRESP_SP(1);
//			codigos.put("cod_visa", 0);
//			codigos.put("cod_enchufate", 0);
			respuesta.setcRESP_SP(queryResp.get("cRESP_SP").toString());
		} catch (Exception e) {
			System.out.println("OCVPagoEjecutadoServiceImpl.ObtenerResultadoPago()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(codigos);
		return respuesta;
	}

	@Override
	public Respuesta EnviarCorreoPago(Map<String, String> requestParm) {
		Respuesta respuesta = new Respuesta();
		try {
			String fecha = requestParm.get("fecha");
			String hora = requestParm.get("hora");
			String numOperacion = requestParm.get("TRACE_NUMBER");
			String numTarjeta = requestParm.get("CARD");
			double monto = Double.parseDouble(requestParm.get("ammount"));
			int nisRad = Integer.parseInt(requestParm.get("nis_rad"));
			String correo = requestParm.get("correo");
			Map<String, Object> queryResp = dao.enviarCorreoPago(fecha, hora, numOperacion, numTarjeta, monto, nisRad,
					correo);
			respuesta.setnRESP_SP(((BigDecimal) queryResp.get("nRESP_SP")).intValueExact());
			respuesta.setcRESP_SP(queryResp.get("cRESP_SP").toString());
		} catch (Exception e) {
			System.out.println("OCVPagoEjecutadoServiceImpl.EnviarCorreoPago()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		return respuesta;
	}

	/* init intermediate screen */
	@Override
	public Respuesta obtenerDatosPago(Map<String, String> requestParm) throws GmdException {
		Respuesta respuesta = new Respuesta();
		PagoEjecutado pagoReturn = new PagoEjecutado();
		Double total = 0.0;
		try {
			String trxID = requestParm.get("trxID");
//			String correo = requestParm.get("correo");
			List<PagoEjecutado> listaPago = dao.obtenerDatosPago(trxID);
			if (listaPago.size() == 0 || !listaPago.get(0).getErrorCode().equals(Constantes.PAYMENT_SUCCESS)) {
				throw new GmdException("No se encontraron pagos relacionados al código de transacción");
			}
			pagoReturn = listaPago.get(0);
			for (PagoEjecutado pay : listaPago) {
				total += pay.getMonto();
			}
			pagoReturn.setMonto(total);
			respuesta.setbRESP(pagoReturn);
			respuesta.setnRESP_SP(Constantes.ESTADO_SUCCESS);
			respuesta.setcRESP_SP(
					"Transacción de pago completada con éxito. Se remitió el detalle de la operación a la dirección de correo consignada durante el proceso.");
		} catch (Exception e) {
			System.out.println("OCVPagoEjecutadoServiceImpl.obtenerDatosPago()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e.getMessage());
			respuesta.setnRESP_SP(0);
		}
		return respuesta;
	}
	/* end intermediate screen */

	/* adecuacion proceso pago */
	@SuppressWarnings("unchecked")
	@Override
	public Respuesta getPaymentResponseMovil(Map<String, String> requestParm) throws GmdException {
		Respuesta respuesta = new Respuesta();
		Map<String, Object> respuestaLiquidacion = null;
		PagoEjecutado pago = new PagoEjecutado();
		boolean success = false;
		List<Map<String, Object>> listaRegistros = null;
		boolean flagErrorInsert = false;
		Integer codigoAgencia = 0;
		List<TipoTarjeta> listaTarjeta = new ArrayList<TipoTarjeta>();
		List<PagoEjecutado> listaPrevia = new ArrayList<PagoEjecutado>();
		try {
			/* validacion inicial de registros previos */
			Type listType = new TypeToken<List<Map<String, Object>>>() {
			}.getType();
			listaRegistros = JsonUtil.convertirCadenaJsonALista(requestParm.get("listaRegistros"), listType);
			if (listaRegistros == null || listaRegistros.size() == 0) {
				throw new GmdException("Error obteniendo la lista de registros de los pagos.");
			}

			/* validacion de valor de operacion visa */
			if (requestParm.get("ACTION_CODE").equals(Constantes.PAYMENT_SUCCESS)) {
				success = true;
			}

			if (success) {
				/* if catch exception insert/update register OCV_SIMBOLO_VAR_REG */
				flagErrorInsert = true;

				/* setear respuesta success */
				respuesta.setcRESP_SP(Constantes.MENSAJE_SUCCESS_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_SUCCESS);

//				//setear atributos pago
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

				// setear codigo de agencia respuesta
				listaTarjeta = utilDao.obtenerDatosTarjeta(null, requestParm.get("BRAND"), null);
				if (listaTarjeta != null && listaTarjeta.size() > 0) {
					codigoAgencia = listaTarjeta.get(0).getCodigoAgencia();
				}

//				listaTarjeta = utilDao.obtenerDatosTarjeta(null, requestParm.get("tipoTarjeta").toLowerCase(), null);
//				if(listaTarjeta != null && listaTarjeta.size() > 0) {
//					codigoAgencia = listaTarjeta.get(0).getCodigoAgencia();
//				}

				// Obtener parametros enchufate
				Map<String, String> datoCat = new TreeMap<String, String>();
				datoCat.put("categoria", "DATOS_ENCHUFATE");
				Map<String, String> datosEnchufate = (Map<String, String>) utilService
						.ObtenerParametrosCategoria(datoCat).getbRESP();

				if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
					datoCat.put("estado", Constantes.ST_REGIST_OCV_ENCHUFATE);
				} else if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_JOB)) {
					datoCat.put("estado", Constantes.ST_REGIST_OCV);
				} else {
					throw new GmdException("Error al obtener el parámetro FLAG_MODO del proceso.");
				}

				/* realizar inserción inicial OCV_SIMBOLO_VAR_REG */
				for (int i = 0; i < listaRegistros.size(); i++) {
					PagoEjecutado registroPrevio = new PagoEjecutado();
					registroPrevio.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));
					registroPrevio.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
					registroPrevio.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(
							listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
					registroPrevio.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(
							listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
					registroPrevio.setMonto((Double) listaRegistros.get(i).get("deuda"));
					registroPrevio.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
					registroPrevio.setEstado(datoCat.get("estado"));
					registroPrevio.setCodAgencia(codigoAgencia);
					System.out.println(JsonUtil.convertirObjetoACadenaJson(registroPrevio));
					dao.insertarRegistroPrevio(registroPrevio);
				}

//				/*insercion registros OCV_PAGO_EJECUTADO*/
				for (int i = 0; i < listaRegistros.size(); i++) {
					PagoEjecutado prevHelp = new PagoEjecutado();
					prevHelp = (PagoEjecutado) ParametrosUtil.cloneObject(pago);
					prevHelp.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));
					prevHelp.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
					prevHelp.setMonto((Double) listaRegistros.get(i).get("deuda"));
					prevHelp.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(
							listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
					prevHelp.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(
							listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
					prevHelp.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
					prevHelp.setCodAgencia(codigoAgencia);
					dao.insertarPagoEjecutadoSinCorreo(prevHelp);
					listaPrevia.add(prevHelp);
				}

				/* envío de correo */
				// Insertamos el resultado de atributos finales al pago
				pago.setMonto(Double.parseDouble(requestParm.get("amount")));
				pago.setCorreo(requestParm.get("correo"));

				// envio de correo por respuesta satisfactoria
				if (pago.getErrorCode().equals(String.valueOf(Constantes.PAYMENT_SUCCESS))) {
					Map<String, Object> respNotificacion = notificacionDao.enviarCorreoxNotificacionGeneral(
							Constantes.ID_NOTIFICACION_PAGO, pago.getCorreo(), pago.getNumOperacion(), pago.getCorreo(),
							pago.getNumTarjeta(), ((pago.getFecha()).concat(" ")).concat(pago.getHora()),
							ParametrosUtil.roundDecimal(pago.getMonto(), 2), String.valueOf(pago.getNumLiquidacion()),
							pago.getTrxId());
					if (Integer.valueOf(respNotificacion.get(ConstantesDAO.COD_RESPUESTA)
							.toString()) != Constantes.ESTADO_SUCCESS) {
						System.err.println("Ocurrió un error en el envío de correo");
					}
				}

				/* si FLAG_MODO enchufate ejecuta pago por servicio */
				if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
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

				/* si FLAG_MODO enchufate ejecuta actualizacion listaPrevia */
				if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)
						&& respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS) {
					for (PagoEjecutado pagoPrev : listaPrevia) {
						pagoPrev.setEstado(Constantes.ST_PAGADO_OCV);
						dao.actualizarRegistroPrevio(pagoPrev);
					}
				}
			} else {
				/* setear respuesta visa failure */
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);

				pago.setTrxId(requestParm.get("TRANSACTION_ID") != null ? requestParm.get("TRANSACTION_ID")
						: UUID.randomUUID().toString());
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
				// setear valor total monto
				pago.setMonto(Double.parseDouble(requestParm.get("amount")));
				// insercion proceso fallido de visa
				dao.insertarPagoEjecutadoSinCorreo(pago);
			}

			/* adicion de log auditoria pago */
			int tipoAuditoria = respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS ? Constantes.AUDITORIA_PAGO_SUCCESS
					: Constantes.AUDITORIA_PAGO_FAILURE;
			auditoriaService.registrarLog(requestParm.get("auth_correo"), requestParm.get("flagChannel"), tipoAuditoria,
					Integer.parseInt(requestParm.get("nis_rad")));
//			/**/
		} catch (Exception e) {
			// adicion log auditoria pago fallido
			auditoriaService.registrarLog(requestParm.get("auth_correo"), requestParm.get("flagChannel"),
					Constantes.AUDITORIA_PAGO_FAILURE, Integer.parseInt(requestParm.get("nis_rad")));
			/* insercion OCV_SIMBOLO_VAR_REG */
			// validación de existencia de registros iniciales
			if (listaRegistros != null && listaRegistros.size() != 0 && flagErrorInsert) {
				// obtener registros de listaPrevia: registros existentes
				for (int i = 0; i < listaRegistros.size(); i++) {
					boolean flagExist = false;
					for (PagoEjecutado pagoPrevio : listaPrevia) {
						if (Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")) == pagoPrevio
								.getSimboloVar()
								&& Long.parseLong(requestParm.get("liquidacion")) == pago.getNumLiquidacion()) {
							flagExist = true;
							pagoPrevio.setEstado(Constantes.ST_PAGO_ERROR_OCV);
							dao.actualizarRegistroPrevio(pagoPrevio);
							break;
						}
					}
					if (!flagExist) {
						PagoEjecutado registroPrevio = new PagoEjecutado();
						registroPrevio.setNumLiquidacion(Long.parseLong(requestParm.get("liquidacion")));
						registroPrevio.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
						registroPrevio.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(
								listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
						registroPrevio.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(
								listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
						registroPrevio.setMonto((Double) listaRegistros.get(i).get("deuda"));
						registroPrevio.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
						registroPrevio.setEstado(Constantes.ST_PAGO_ERROR_OCV);
						registroPrevio.setCodAgencia(codigoAgencia);
						System.out.println(JsonUtil.convertirObjetoACadenaJson(registroPrevio));
						dao.insertarRegistroPrevio(registroPrevio);
					}
				}
			}
			respuesta.setcRESP_SP("Error realizando el pago: " + e);
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
		}
		respuesta.setbRESP(pago);
		return respuesta;
	}

	/* adecuacion mastercard proceso pago movil */
	@SuppressWarnings("unchecked")
	@Override
	public Respuesta getPaymentResponseMovilV2(Map<String, String> requestParm) throws GmdException {
		PagoEjecutado pago = new PagoEjecutado();
		Respuesta respuesta = new Respuesta();
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
			/* set values liquidacion */
			liquidacionEnchufate = Integer.valueOf(requestParm.get("liquidacionEnchufate"));
			liquidacionJob = Integer.valueOf(requestParm.get("liquidacionJob"));
			liquidacionFinal = liquidacionEnchufate == 0 ? liquidacionJob : liquidacionEnchufate;

			/* validacion inicial de registros previos */
			Type listType = new TypeToken<List<Map<String, Object>>>() {
			}.getType();
			listaRegistros = JsonUtil.convertirCadenaJsonALista(requestParm.get("listaRegistros"), listType);
			if (listaRegistros == null || listaRegistros.size() == 0) {
				throw new GmdException("Error obteniendo la lista de registros de los pagos.");
			}

			/* validacion de valor de operacion visa */
			if (requestParm.get("ACTION_CODE").equals(Constantes.PAYMENT_SUCCESS)) {
				success = true;
			}

			if (success) {
				/* if catch exception insert/update register OCV_SIMBOLO_VAR_REG */
				flagErrorInsert = true;

				/* setear respuesta success */
				respuesta.setcRESP_SP(Constantes.MENSAJE_SUCCESS_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_SUCCESS);

//				//setear atributos pago
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
				pago.setNisRad(Integer.parseInt(requestParm.get("nis_rad")));

				// setear codigo de agencia respuesta
				listaTarjeta = utilDao.obtenerDatosTarjeta(null, requestParm.get("BRAND"), null);
				if (listaTarjeta != null && listaTarjeta.size() > 0) {
					codigoAgencia = listaTarjeta.get(0).getCodigoAgencia();
				}

				// Obtener parametros enchufate
				Map<String, String> datoCat = new TreeMap<String, String>();
				datoCat.put("categoria", "DATOS_ENCHUFATE");
				Map<String, String> datosEnchufate = (Map<String, String>) utilService
						.ObtenerParametrosCategoria(datoCat).getbRESP();

				/* evaluacion FLAG_MODO */
				if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
					if (requestParm.get("BRAND").equals(Constantes.BRAND_VISA)) {
						liquidacionFinal = liquidacionEnchufate;
						datoCat.put("estado", Constantes.ST_REGIST_OCV_ENCHUFATE);
					} else if (requestParm.get("BRAND").equals(Constantes.BRAND_MASTERCARD)) {
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

				/* realizar inserción inicial OCV_SIMBOLO_VAR_REG */
				for (int i = 0; i < listaRegistros.size(); i++) {
					PagoEjecutado registroPrevio = new PagoEjecutado();
					registroPrevio.setNumLiquidacion(liquidacionFinal);
					registroPrevio.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
					registroPrevio.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(
							listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
					registroPrevio.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(
							listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
					registroPrevio.setMonto((Double) listaRegistros.get(i).get("deuda"));
					registroPrevio.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
					registroPrevio.setEstado(datoCat.get("estado"));
					registroPrevio.setCodAgencia(codigoAgencia);
					System.out.println(JsonUtil.convertirObjetoACadenaJson(registroPrevio));
					dao.insertarRegistroPrevio(registroPrevio);
				}

//				/*insercion registros OCV_PAGO_EJECUTADO*/
				for (int i = 0; i < listaRegistros.size(); i++) {
					PagoEjecutado prevHelp = new PagoEjecutado();
					prevHelp = (PagoEjecutado) ParametrosUtil.cloneObject(pago);
					prevHelp.setNumLiquidacion(liquidacionFinal);
					prevHelp.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
					prevHelp.setMonto((Double) listaRegistros.get(i).get("deuda"));
					prevHelp.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(
							listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
					prevHelp.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(
							listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
					prevHelp.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
					prevHelp.setCodAgencia(codigoAgencia);
					dao.insertarPagoEjecutadoSinCorreo(prevHelp);
					listaPrevia.add(prevHelp);
				}

				/* envío de correo */
				// Insertamos el resultado de atributos finales al pago
				pago.setMonto(Double.parseDouble(requestParm.get("amount")));
				pago.setCorreo(requestParm.get("correo"));

				// envio de correo por respuesta satisfactoria
				if (pago.getErrorCode().equals(String.valueOf(Constantes.PAYMENT_SUCCESS))) {
					Map<String, Object> respNotificacion = notificacionDao.enviarCorreoxNotificacionGeneral(
							Constantes.ID_NOTIFICACION_PAGO, pago.getCorreo(), pago.getNumOperacion(), pago.getCorreo(),
							pago.getNumTarjeta(), ((pago.getFecha()).concat(" ")).concat(pago.getHora()),
							ParametrosUtil.roundDecimal(pago.getMonto(), 2), String.valueOf(pago.getNumLiquidacion()),
							pago.getTrxId());
					if (Integer.valueOf(respNotificacion.get(ConstantesDAO.COD_RESPUESTA)
							.toString()) != Constantes.ESTADO_SUCCESS) {
						System.err.println("Ocurrió un error en el envío de correo");
					}
				}

				/* ejecucion enchufate solo para BRAND VISA */
				if (requestParm.get("BRAND").equals(Constantes.BRAND_VISA)) {
					/* si FLAG_MODO enchufate ejecuta pago por servicio */
					if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
						// Obtenemos token de Enchufate
						datosEnchufate.put("access_token", enchufateServ.getToken(datosEnchufate));

						// Seteamos datos de pago de liquidación
						datosEnchufate.put("url",
								datosEnchufate.get("base_uri") + datosEnchufate.get("pagar_liqui_uri"));
						datosEnchufate.put("numeroLiquidacion", "" + pago.getNumLiquidacion());
						datosEnchufate.put("nis_rad", "" + pago.getNisRad());
						datosEnchufate.put("numeroTarjeta", pago.getNumTarjeta());
						datosEnchufate.put("fechaPago", pago.getFecha());
						datosEnchufate.put("horaPago", pago.getHora());
						datosEnchufate.put("numOperacionTar", pago.getNumOperacion());

						// Ejecutamos el servicio de pagos de Enchufate
						respuesta = enchufateServ.pagarLiquidacion(datosEnchufate);
					}

					/* si FLAG_MODO enchufate ejecuta actualizacion listaPrevia */
					if (datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)
							&& respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS) {
						for (PagoEjecutado pagoPrev : listaPrevia) {
							pagoPrev.setEstado(Constantes.ST_PAGADO_OCV);
							dao.actualizarRegistroPrevio(pagoPrev);
						}
					}
				}
			} else {
				/* setear respuesta visa failure */
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_VISA);
				respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);

				pago.setTrxId(requestParm.get("TRANSACTION_ID") != null ? requestParm.get("TRANSACTION_ID")
						: UUID.randomUUID().toString());
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
				// setear valor total monto
				pago.setMonto(Double.parseDouble(requestParm.get("amount")));
				// insercion proceso fallido de visa
				dao.insertarPagoEjecutadoSinCorreo(pago);
			}

			/* adicion de log auditoria pago */
			int tipoAuditoria = respuesta.getnRESP_SP() == Constantes.ESTADO_SUCCESS ? Constantes.AUDITORIA_PAGO_SUCCESS
					: Constantes.AUDITORIA_PAGO_FAILURE;
			auditoriaService.registrarLog(requestParm.get("auth_correo"), requestParm.get("flagChannel"), tipoAuditoria,
					Integer.parseInt(requestParm.get("nis_rad")));
//			/**/
		} catch (Exception e) {
			// adicion log auditoria pago fallido
			auditoriaService.registrarLog(requestParm.get("auth_correo"), requestParm.get("flagChannel"),
					Constantes.AUDITORIA_PAGO_FAILURE, Integer.parseInt(requestParm.get("nis_rad")));
			/* insercion OCV_SIMBOLO_VAR_REG */
			// validación de existencia de registros iniciales
			if (listaRegistros != null && listaRegistros.size() != 0 && flagErrorInsert) {
				// obtener registros de listaPrevia: registros existentes
				for (int i = 0; i < listaRegistros.size(); i++) {
					boolean flagExist = false;
					for (PagoEjecutado pagoPrevio : listaPrevia) {
						if (Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")) == pagoPrevio
								.getSimboloVar() && liquidacionFinal == pago.getNumLiquidacion()) {
							flagExist = true;
							pagoPrevio.setEstado(Constantes.ST_PAGO_ERROR_OCV);
							dao.actualizarRegistroPrevio(pagoPrevio);
							break;
						}
					}
					if (!flagExist) {
						PagoEjecutado registroPrevio = new PagoEjecutado();
						registroPrevio.setNumLiquidacion(liquidacionFinal);
						registroPrevio.setSimboloVar(Long.parseLong((String) listaRegistros.get(i).get("numeroDoc")));
						registroPrevio.setFechaEmision(ParametrosUtil.convertObjectToDateFormat(
								listaRegistros.get(i).get("fechaEmision"), Constantes.FORMAT_DATE_ENCHUFATE));
						registroPrevio.setFechaVencimiento(ParametrosUtil.convertObjectToDateFormat(
								listaRegistros.get(i).get("fechaVencimiento"), Constantes.FORMAT_DATE_ENCHUFATE));
						registroPrevio.setMonto((Double) listaRegistros.get(i).get("deuda"));
						registroPrevio.setCanal(Integer.valueOf(requestParm.get("flagChannel")));
						registroPrevio.setEstado(Constantes.ST_PAGO_ERROR_OCV);
						registroPrevio.setCodAgencia(codigoAgencia);
						System.out.println(JsonUtil.convertirObjetoACadenaJson(registroPrevio));
						dao.insertarRegistroPrevio(registroPrevio);
					}
				}
			}
			respuesta.setcRESP_SP("Error realizando el pago: " + e);
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
		}
		respuesta.setbRESP(pago);
		return respuesta;
	}

	public Respuesta insertarRegistroLiquidacionVisa(String requestParm) throws GmdException {
		Map<String, Object> respuestaLiquidacion = null;
		Respuesta respuesta = new Respuesta();

		try {

			// Object of array
			JsonParser jsonParser = new JsonParser();
			JsonObject gsonObj = (JsonObject) jsonParser.parse(requestParm);
			// gsonObj.getAsJsonObject(requestParm);
			String liquidacion = " ";
			String nis_rad = "0";
			
			if(!gsonObj.get("numeroLiquidacion").getAsString().equals("")){
				liquidacion = gsonObj.get("numeroLiquidacion").getAsString();
			}
			
			if(!gsonObj.get("nisRad").getAsString().equals("")){
				nis_rad = gsonObj.get("nisRad").getAsString();
			}
			
			respuestaLiquidacion = dao.insertarRegistroLiquidacionVisa(liquidacion,
					nis_rad, requestParm);
			// System.out.println("Json: " + stringJson);
			respuesta.setcRESP_SP((String) respuestaLiquidacion.get("cRESP_SP"));
			respuesta.setnRESP_SP((int) ((BigDecimal) respuestaLiquidacion.get("nRESP_SP")).longValue());
			if (respuesta.getnRESP_SP() == 0) {
				return respuesta;
			}
		} catch (Exception e) {
			respuesta.setcRESP_SP("Error al registrar la liquidación visa: " + e);
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
		}

		return respuesta;
	}

}
