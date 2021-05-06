package pe.com.ocv.service.impl;

import java.text.ParseException;
import java.math.BigDecimal;

import pe.com.ocv.model.Consumo;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.model.Suministro;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.ocv.dao.IOCVSuministrosDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.ocv.service.IOCVAuditoriaService;
import pe.com.ocv.service.IOCVSuministrosService;
import pe.com.ocv.util.Constantes;

@Service
@SuppressWarnings("unchecked")
public class OCVSuministrosServiceImpl implements IOCVSuministrosService {

	@Autowired
	private IOCVSuministrosDAO dao;
	
	@Autowired
	private IOCVAuditoriaService auditoriaService;

	public OCVSuministrosServiceImpl() {
		super();
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Respuesta ListaNisXCliente(Map<String, String> RequestParm) {
		List<Suministro> listaNIS = new ArrayList<Suministro>();
		Respuesta respuesta = new Respuesta(0, "");

		try {
			int nis_rad = Integer.parseInt(RequestParm.get("nis_rad"));
			String email = RequestParm.get("auth_correo");

			Map<String, Object> queryResp = dao.obtenerListNisXCliente(nis_rad);
//			inicio cvalenciap
//			if(RequestParm.get("flagChannel") != null && RequestParm.get("flagChannel").equals(Constantes.FLAG_CHANNEL_APP)) {
//				daoAudit.registrarLog(email, Constantes.AUDITORIA_CONSULTA_APP, nis_rad);
//			}else {
//				daoAudit.registrarLog(email, Constantes.AUDITORIA_CONSULTA, nis_rad);
//			}
//			daoAudit.registrarLog(email, Util.getValueConstantAuditoria(RequestParm.get("flagChannel"), Constantes.AUDITORIA_CONSULTA), nis_rad);
			auditoriaService.registrarLog(email, RequestParm.get("flagChannel"), Constantes.AUDITORIA_CONSULTA, nis_rad);
//			fin cvalenciap
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					Suministro item = new Suministro();
					bdCast = (BigDecimal) map.get("NIS_RAD");
					item.setNis_rad(bdCast.intValueExact());
					listaNIS.add(item);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
			respuesta.setbRESP(listaNIS);
		} catch (Exception e) {
			System.out.println("OCVSuministrosServiceImpl.ListaNisXCliente()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(listaNIS);
			respuesta.setnRESP_SP(0);
		}

		return respuesta;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Respuesta CabeceraXNis(Map<String, String> RequestParm) {
		Respuesta respuesta = new Respuesta(0, "");
		Suministro suministro = new Suministro();

		try {
			int nis_rad = Integer.parseInt(RequestParm.get("nis_rad"));

			Map<String, Object> queryResp = dao.CabeceraXNis(nis_rad);
			//inicio cvalenciap
			String flag_multiple = RequestParm.get("flag_multiple");
			if(flag_multiple != null && Integer.parseInt(flag_multiple) == Constantes.AUDITORIA_SUMINISTRO_MULTIPLE) {
//				if(RequestParm.get("flagChannel") != null && RequestParm.get("flagChannel").equals(Constantes.FLAG_CHANNEL_APP)) {
//					daoAudit.registrarLog(RequestParm.get("auth_correo"), Constantes.AUDITORIA_CONSULTA_APP, nis_rad);
//				} else {
//					daoAudit.registrarLog(RequestParm.get("auth_correo"), Constantes.AUDITORIA_CONSULTA, nis_rad);
//				}
//				daoAudit.registrarLog(RequestParm.get("auth_correo"), Util.getValueConstantAuditoria(RequestParm.get("flagChannel"), Constantes.AUDITORIA_CONSULTA), nis_rad);
				auditoriaService.registrarLog(RequestParm.get("auth_correo"), RequestParm.get("flagChannel"), Constantes.AUDITORIA_CONSULTA, nis_rad);
			}
			//fin inicio cvalenciap
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					bdCast = (BigDecimal) map.get("NIS_RAD");
					suministro.setNis_rad(bdCast.intValueExact());
					suministro.setEst_sum((String) map.get("EST_SUM"));
					suministro.setDireccion((String) map.get("DIRECCION"));
					bdCast = (BigDecimal) map.get("TOTAL_DEUDA");
					String nombres = map.get("NOMBRES") == null ? "" : (String) map.get("nombres");
					suministro.setNom_cliente(nombres.trim());
					suministro.setTotal_deuda(bdCast.doubleValue());
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
			respuesta.setbRESP(suministro);
		} catch (Exception e) {
			System.out.println("OCVSuministrosServiceImpl.CabeceraXNis()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(suministro);
			respuesta.setnRESP_SP(0);
		}

		return respuesta;
	}

	@Override
	public Respuesta HistoricoConsumo(Map<String, String> requestParm) throws ParseException, Exception {
		List<Consumo> listaConsumo = new ArrayList<Consumo>();
		Respuesta respuesta = new Respuesta(0, "");

		try {
			int nis = Integer.parseInt(requestParm.get("nis_rad"));

			Map<String, Object> queryResp = dao.historicoConsumo(nis);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					Consumo consumo = new Consumo();
					consumo.setMes_fact((String) map.get("MES_FACT"));
					consumo.setVolumen(((BigDecimal) map.get("VOLUMEN")).doubleValue());
					consumo.setMonto(((BigDecimal) map.get("MONTO")).doubleValue());
					listaConsumo.add(consumo);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
			respuesta.setbRESP(listaConsumo);
		} catch (Exception e) {
			System.out.println("OCVSuministrosServiceImpl.HistoricoConsumo()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(listaConsumo);
			respuesta.setnRESP_SP(0);
		}

		return respuesta;
	}

}