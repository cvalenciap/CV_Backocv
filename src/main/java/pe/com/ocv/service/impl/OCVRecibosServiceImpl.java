package pe.com.ocv.service.impl;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.sql.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.ocv.service.IOCVRecibosService;
import pe.com.ocv.dao.IOCVRecibosDAO;
import pe.com.ocv.model.ConceptoPago;
import pe.com.ocv.model.DetallePago;
import pe.com.ocv.model.ReciboPagado;
import pe.com.ocv.model.ReciboPendiente;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.util.DataBean;

@Service
@SuppressWarnings("unchecked")
public class OCVRecibosServiceImpl implements IOCVRecibosService {

	@Autowired
	private IOCVRecibosDAO dao;

	public OCVRecibosServiceImpl() {
		super();
	}

	@Override
	public Respuesta ListaRecibosPendientesXNis(Map<String, String> requestParm) throws ParseException, Exception {
		List<ReciboPendiente> listaRecibosPendientes = new ArrayList<ReciboPendiente>();
		Respuesta respuesta = new Respuesta();

		try {		
			int nis = Integer.parseInt(requestParm.get("nis_rad"));
			int pageNumber = Integer.parseInt(requestParm.get("page_num"));
			int pageSize = Integer.parseInt(requestParm.get("page_size"));

			

			Map<String, Object> queryResp = this.dao.listaRecibosPendientes(nis, pageNumber,
					pageSize);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					ReciboPendiente recPendiente = new ReciboPendiente();
					Timestamp tsFactMes = (Timestamp) map.get("MES");
					Date dFactMes = new Date(tsFactMes.getTime());
					recPendiente.setMes(dFactMes);
					recPendiente.setVencimiento((String) map.get("VENCIMIENTO"));
					recPendiente.setTotal_fact(((BigDecimal) map.get("TOTAL_FACT")).doubleValue());
					recPendiente.setDeuda(((BigDecimal) map.get("DEUDA")).doubleValue());
					recPendiente.setVolumen(((BigDecimal) map.get("VOLUMEN")).doubleValue());
					recPendiente.setTipo_recibo((String) map.get("TIPO_RECIBO"));
					recPendiente.setRecibo((String) map.get("SIMBOLO_VAR"));
					recPendiente.setSec_nis(((BigDecimal) map.get("SEC_NIS")).intValue());
					recPendiente.setSec_rec(((BigDecimal) map.get("SEC_REC")).intValue());
					recPendiente.setF_fact((String) map.get("F_FACT"));
					recPendiente.setNro_factura((String) map.get("NRO_FACTURA"));
					recPendiente.setSelect(false);
					listaRecibosPendientes.add(recPendiente);
				}
				respuesta.setTotal(((BigDecimal) queryResp.get("TOTAL")).intValue());
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setbRESP(listaRecibosPendientes);
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVRecibosServiceImpl.ListaRecibosPendientesXNis()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(listaRecibosPendientes);
			respuesta.setTotal(0);
			respuesta.setnRESP_SP(0);
		}
		return respuesta;
	}

	@Override
	public Respuesta ListaRecibosPagadosXNis(Map<String, String> requestParm) throws ParseException, Exception {
		List<ReciboPagado> listaRecibosPagados = new ArrayList<ReciboPagado>();
		Respuesta respuesta = new Respuesta();

		try {
			int nis = Integer.parseInt(requestParm.get("nis_rad"));
			int pageNumber = Integer.parseInt(requestParm.get("page_num"));
			int pageSize = Integer.parseInt(requestParm.get("page_size"));

			Map<String, Object> queryResp = this.dao.listaRecibosPagados(nis, pageNumber,
					pageSize);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				List<Map<String, Object>> lista = (List<Map<String, Object>>) queryResp.get("IO_CURSOR");
				for (Map<String, Object> map : lista) {
					ReciboPagado recPagado = new ReciboPagado();
					Timestamp tsMes = (Timestamp) map.get("MES");
					Date dMes = new Date(tsMes.getTime());
					recPagado.setMes(dMes);
					recPagado.setVencimiento((String) map.get("VENCIMIENTO"));
					double cobrado = ((BigDecimal) map.get("COBRADO")).doubleValue();
					recPagado.setCobrado(cobrado);
					recPagado.setTotal_fact(((BigDecimal) map.get("TOTAL_FACT")).doubleValue());
					String fechaPago = (String) map.get("FECHA_PAGO");
					recPagado.setFecha_pago(fechaPago.equals("2999-12-31") ? "" : fechaPago);
					recPagado.setTipo_recibo((String) map.get("TIP_REC"));
					recPagado.setNom_agencia((String) map.get("NOM_AGENCIA"));
					recPagado.setLugar_pago((String) map.get("LUGAR_PAGO"));
					recPagado.setRecibo((String) map.get("SIMBOLO_VAR"));
					recPagado.setNis_rad(((BigDecimal) map.get("NIS_RAD")).intValue());
					recPagado.setSec_nis(((BigDecimal) map.get("SEC_NIS")).intValue());
					recPagado.setSec_rec(((BigDecimal) map.get("SEC_REC")).intValue());
					if (cobrado == 0)
						recPagado.setForma_pago("");
					else
						recPagado.setForma_pago((String) map.get("FORMA_PAGO"));
					recPagado.setHora_pago((String) map.get("HORA_PAGO"));
					recPagado.setF_fact((String) map.get("F_FACT"));
					recPagado.setNro_factura((String) map.get("NRO_FACTURA"));
					recPagado.setEstado((String) map.get("EST_REC"));
					recPagado.setEst_rec((String) map.get("EST_REC"));
					listaRecibosPagados.add(recPagado);
				}
				respuesta.setTotal(((BigDecimal) queryResp.get("TOTAL")).intValue());
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setbRESP(listaRecibosPagados);
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVRecibosServiceImpl.ListaRecibosPagadosXNis()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(listaRecibosPagados);
			respuesta.setTotal(0);
			respuesta.setnRESP_SP(0);
		}

		return respuesta;
	}

	@Override
	public Respuesta DetalleRecibo(Map<String, String> requestParm) throws ParseException, Exception {
		List<ConceptoPago> listaDetalleRecibo = new ArrayList<ConceptoPago>();
		Respuesta respuesta = new Respuesta();

		try {
			String recibo = requestParm.get("recibo");

			Map<String, Object> queryResp = this.dao.detalleRecibo(recibo);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					ConceptoPago concepto = new ConceptoPago();
					concepto.setCod_concepto((String) map.get("CODCONCEPTO"));
					concepto.setDesc_concepto((String) map.get("DESCCONCEPTO"));
					concepto.setMonto_concepto(((BigDecimal) map.get("MONTOCONCEPTO")).doubleValue());
					listaDetalleRecibo.add(concepto);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setbRESP(listaDetalleRecibo);
			respuesta.setnRESP_SP(iRespuesta);

		} catch (Exception e) {
			System.out.println("OCVRecibosServiceImpl.DetalleRecibo()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(listaDetalleRecibo);
			respuesta.setnRESP_SP(0);

		}
		return respuesta;
	}

	@Override
	public Respuesta DetallePagos(Map<String, String> requestParm) throws ParseException, Exception {
		List<DetallePago> listaDetallePago = new ArrayList<DetallePago>();
		Respuesta respuesta = new Respuesta();

		try {
			String simbolo_var = requestParm.get("recibo");
			String nro_factura = requestParm.get("nro_factura");			
			
			Map<String, Object> queryResp = this.dao.detallePago(simbolo_var, nro_factura);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					DetallePago recPagado = new DetallePago();
					recPagado.setFecha_pago((String) map.get("F_PAGO"));
					recPagado.setHora_pago((String) map.get("HORA_PAGO"));
					recPagado.setMonto_pago(((BigDecimal) map.get("IMP_PAGO")).doubleValue());
					recPagado.setNom_agencia((String) map.get("NOM_AGENCIA"));
					recPagado.setNom_sucursal((String) map.get("NOM_SUCURSAL"));
					recPagado.setForma_pago((String) map.get("FORMA_PAGO"));
					recPagado.setTipo_doc((String) map.get("TIPO_DOC"));
					listaDetallePago.add(recPagado);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setbRESP(listaDetallePago);
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVRecibosServiceImpl.DetallePagos()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(listaDetallePago);
			respuesta.setnRESP_SP(0);
		}

		return respuesta;
	}

	@Override
	public Respuesta ObtenerReciboPDF(Map<String, String> requestParm) throws ParseException, Exception {
		Respuesta respuesta = new Respuesta();
		byte[] bytes = null;
		boolean info = false;

		try {
			int secRec = Integer.parseInt(requestParm.get("sec_rec"));
			int nisRad = Integer.parseInt(requestParm.get("nis_rad"));
			int secNis = Integer.parseInt(requestParm.get("sec_nis"));
			String fFact = requestParm.get("f_fact");

			Map<String, Object> queryResp = this.dao.obtenerDatosRecibo(secRec, nisRad, secNis,
					fFact);
			ArrayList<DataBean> dataBeanList = new ArrayList<DataBean>();
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				ClassLoader loader = OCVRecibosServiceImpl.class.getClassLoader();
				Map<String, Object> parameters = new HashMap<String, Object>();
				List<Map<String, Object>> lista = (List<Map<String, Object>>) queryResp.get("IO_CURSOR");
				for (Map<String, Object> map : lista) {
					parameters.put("parNroFactura", map.get("NRO_FACTURA"));
					parameters.put("parRefCobro", map.get("REF_COBRO"));
					parameters.put("parNombreTitular", map.get("NOMBRE_TITULAR"));
					parameters.put("parDirTitularCalle", map.get("DIR_TITULAR_CALLE"));
					parameters.put("parDirTitularLocMunic", map.get("DIR_TITULAR_LOC_MUNIC"));
					parameters.put("parDistritoLugarPago", map.get("DISTRITO_LUGAR_PAGO"));
					parameters.put("parReferenciaDir", map.get("REFERENCIA_DIR"));
					parameters.put("parRUCTitular", map.get("RUC_TITULAR"));
					parameters.put("parNISDigit", map.get("NIS_DIGIT"));
					parameters.put("parMesLetras", map.get("MES_LETRAS"));
					parameters.put("parFechaEmision", map.get("FECHA_EMISION"));
					parameters.put("parFechaVencimiento", map.get("FECHA_VENCIMIENTO"));
					parameters.put("parTarifa", map.get("TARIFA"));
					parameters.put("parTitularSuministro", map.get("TITULAR_SUMINISTRO"));
					parameters.put("parDirSuministro", map.get("DIR_SUMINISTRO"));
					parameters.put("parDistrito", map.get("DISTRITO"));
					parameters.put("parActividad", map.get("ACTIVIDAD"));
					parameters.put("parUnidadUso", map.get("UNIDAD_USO"));
					parameters.put("parPeriodo", map.get("PERIODO"));
					parameters.put("parTipoFacturacion", map.get("TIPO_FACTURACION"));
					parameters.put("parSignoSoles", map.get("SIGNO_SOLES"));
					parameters.put("parImporteTotal", map.get("IMPORTE_TOTAL"));
					parameters.put("parDireccion", map.get("DIRECCION"));
					parameters.put("parCara", Integer.parseInt((String) map.get("CARA")));
					parameters.put("parCategoria", map.get("CATEGORIA"));
					parameters.put("parFrecFacturacion", map.get("FREC_FACTURACION"));
					parameters.put("parSector", map.get("SECTOR"));
					parameters.put("parTipoDescarga", map.get("TIPO_DESCARGA"));
					parameters.put("parIndice", ((String) map.get("INDICE")).trim());
					parameters.put("parSecuencial", ((String) map.get("SECUENCIAL")).trim());
					parameters.put("parLote", ((String) map.get("LOTE")).trim());
					parameters.put("parLogoSedapal", loader.getResource("images/logo-sedapal.png").getPath());
					parameters.put("parDedo", loader.getResource("images/dedo.png").getPath());
					parameters.put("parCaraTriste", loader.getResource("images/sad_PNG36230.png").getPath());
					parameters.put("parCaraFeliz", loader.getResource("images/smiley_PNG36230.png").getPath());
					parameters.put("parManoEscribiendo", loader.getResource("images/writting_hand.png").getPath());
					parameters.put("parLogoAndroid", loader.getResource("images/googleplay.png").getPath());
					parameters.put("parLogoIOS", loader.getResource("images/app-store.png").getPath());
					parameters.put("parNumSunat", map.get("SER_SUNAT")); //MBS 20.11.2019 Numeración SUNAT

					// Obtiene lista de medidores
					for (int iMedidores = 1; iMedidores <= 3; iMedidores++) {
						parameters.put("parMedidor" + iMedidores, map.get("MEDIDOR" + iMedidores));
						parameters.put("parLectAnterior" + iMedidores, map.get("LECT_ANTERIOR" + iMedidores));
						parameters.put("parLectActual" + iMedidores, map.get("LECT_ACTUAL" + iMedidores));
						parameters.put("parConsumo" + iMedidores, map.get("LECT_CONSUMO" + iMedidores));
					}

					// Obtiene lista cargos
					for (int iCargo = 1; iCargo <= 15; iCargo++) {
						parameters.put("parConceptoCargo" + iCargo, map.get("CONCEPTO_CARGO" + iCargo));
						parameters.put("parImporteCargo" + iCargo, map.get("IMPORTE_CARGO" + iCargo));
					}

					// Obtiene lista de información complementaria
					for (int iInfoComp = 1; iInfoComp <= 18; iInfoComp++) {
						parameters.put("parInfoComplementaria" + iInfoComp, map.get("INFO_COMPLEMENTARIA" + iInfoComp));
					}

					// Obtiene lista de mensajes del cliente
					for (int iMensjCli = 1; iMensjCli <= 11; iMensjCli++) {
						String mensajeCli = (String) map.get("MENSAJE_CLI" + iMensjCli);
						parameters.put("parIndicadorCli" + iMensjCli, mensajeCli.charAt(0));
						mensajeCli = this.validarPrimerCaracter(mensajeCli);
						parameters.put("parMensajeCli" + iMensjCli, mensajeCli);
					}

					// Obtiene lista de mensajes de la cara
					for (int iMensjCara = 1; iMensjCara <= 3; iMensjCara++) {
						parameters.put("parMensajeCara" + iMensjCara, map.get("MENSAJE_CARA" + iMensjCara));
					}

					// Obtiene lista para gráfico de consumo
					for (int iMesConsumo = 2; iMesConsumo <= 13; iMesConsumo++) {
						dataBeanList.add(this.produce((String) map.get("MES" + iMesConsumo),
								Double.parseDouble(map.get("CONSUMO" + iMesConsumo).toString())));
					}

					// Lista los factores para VMA
					boolean indicaVMA = false;
					for (int iFactor = 1; iFactor <= 3; iFactor++) {
						String dbo5 = ((String) map.get("DBO5" + iFactor)).trim();
						String dqo = ((String) map.get("DQO" + iFactor)).trim();
						String sst = ((String) map.get("SST" + iFactor)).trim();
						String ayg = ((String) map.get("AYG" + iFactor)).trim();
						if (!dbo5.equals("0") || !dqo.equals("0") || !sst.equals("0") || !ayg.equals("0")
								|| indicaVMA) {
							indicaVMA = true;
						}
						parameters.put("parDBO5" + iFactor, dbo5);
						parameters.put("parDQO" + iFactor, dqo);
						parameters.put("parSST" + iFactor, sst);
						parameters.put("parAYG" + iFactor, ayg);
					}

					// indica si hay VMA
					String facAjuste = ((String) map.get("FAC_AJUSTE")).trim();
					if (!facAjuste.equals("0") || indicaVMA) {
						indicaVMA = true;
					}

					parameters.put("parFacAjuste", facAjuste);
					parameters.put("parVMA", indicaVMA);
					info = true;
				}

				if (info) {
					Map<String, Object> queryResp2 = this.dao.obtenerParametrosRecibo();
					bdCast = (BigDecimal) queryResp2.get("nRESP_SP");
					iRespuesta = bdCast.intValueExact();
					if (iRespuesta != 0) {
						for (Map<String, Object> map2 : (List<Map<String, Object>>) queryResp2.get("IO_CURSOR")) {
							parameters.put((String) map2.get("DESC_PARAM"), map2.get("VALOR_PARAM"));
						}
					}

					try {
						JasperReport jasperReport = JasperCompileManager
								.compileReport(loader.getResource("reportes/Recibo_SEDAPAL.jrxml").getPath());
						JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataBeanList);
						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
								beanColDataSource);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
						bytes = baos.toByteArray();
						respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
						respuesta.setnRESP_SP(iRespuesta);
					} catch (Exception e) {
						respuesta.setnRESP_SP(0);
						respuesta.setcRESP_SP("No se pudo generar el recibo PDF.");
					}
				} else {
					respuesta.setnRESP_SP(0);
					respuesta.setcRESP_SP("No se encontró información para la generación del recibo.");
				}
			}
		} catch (Exception e) {
			System.out.println("OCVRecibosServiceImpl.ObtenerReciboPDF()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(bytes);
		return respuesta;
	}

	@Override
	public Respuesta ValidarReciboAnterior(Map<String, String> requestParm) throws ParseException, Exception {
		Respuesta respuesta = new Respuesta();
		try {
			int nis = Integer.parseInt(requestParm.get("nis_rad"));
			String recibo = requestParm.get("recibo");

			Map<String, Object> queryResp = this.dao.validarReciboAnterior(nis, recibo);
			respuesta.setbRESP(((BigDecimal) queryResp.get("oRESULT")).intValue() == 1 ? true : false);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setcRESP_SP2((String) queryResp.get("cRESP_SP2"));
			respuesta.setComisionVisa(Double
					.parseDouble(queryResp.get("oCOMISION") == null ? "0" : queryResp.get("oCOMISION").toString()));
			respuesta.setnRESP_SP(bdCast.intValue());
		} catch (Exception e) {
			System.out.println("OCVRecibosServiceImpl.ValidarReciboAnterior()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(false);
			respuesta.setnRESP_SP(0);
		}

		return respuesta;
	}

	private String validarPrimerCaracter(String mensaje) {
		if (mensaje.charAt(0) == '1' || mensaje.charAt(0) == '2') {
			return mensaje.substring(1);
		} else {
			return mensaje;
		}
	}

	private DataBean produce(String mes, Double consumo) {
		DataBean dataBean = new DataBean();
		dataBean.setMeses(mes);
		dataBean.setConsumo(consumo);
		return dataBean;
	}

}