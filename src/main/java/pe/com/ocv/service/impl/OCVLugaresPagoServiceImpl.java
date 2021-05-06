package pe.com.ocv.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import pe.com.ocv.dao.IOCVLugaresPagoDAO;
import pe.com.ocv.model.Agencia;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.model.Sucursal;

import org.springframework.stereotype.Service;
import pe.com.ocv.service.IOCVLugaresPagoService;

@Service
@SuppressWarnings("unchecked")
public class OCVLugaresPagoServiceImpl implements IOCVLugaresPagoService {

	@Autowired
	private IOCVLugaresPagoDAO dao;

	public OCVLugaresPagoServiceImpl() {
		super();
	}

	@Override
	public Respuesta ListaAgencias() {
		List<Agencia> listaAgencias = new ArrayList<Agencia>();
		Respuesta respuesta = new Respuesta();
		try {

			Map<String, Object> queryResp = this.dao.listaAgencias();
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					Agencia agencia = new Agencia();
					agencia.setCod_agencia(((BigDecimal) map.get("CODAGENCIA")).intValue());
					agencia.setNom_entidad((String) map.get("NOMBREENTIDAD"));
					listaAgencias.add(agencia);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVLugaresPagoServiceImpl.ListaAgencias()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP(listaAgencias);
		return respuesta;
	}

	@Override
	public Respuesta ListaSucursales(Map<String, String> requestParm) {
		List<Sucursal> listaSucursales = new ArrayList<Sucursal>();
		Respuesta respuesta = new Respuesta();
		try {
			int iCodAgencia = Integer.parseInt(requestParm.get("cod_agencia"));

			Map<String, Object> queryResp = this.dao.listaSucursales(iCodAgencia);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					Sucursal sucursal = new Sucursal();
					sucursal.setNom_recaudador((String) map.get("NOMBRERECAUDADOR"));
					sucursal.setDir_recaudador((String) map.get("DIRECCIONRECAUDADOR"));
					sucursal.setDistrito((String) map.get("DISTRITO"));
					sucursal.setTip_recaudador((String) map.get("TIPORECAUDADOR"));
					sucursal.setLatitud(((BigDecimal) map.get("LATITUD")).doubleValue());
					sucursal.setLongitud(((BigDecimal) map.get("LONGITUD")).doubleValue());
					listaSucursales.add(sucursal);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVLugaresPagoServiceImpl.ListaSucursales()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(listaSucursales);
		return respuesta;
	}

}