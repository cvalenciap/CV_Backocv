package pe.com.ocv.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.dao.IOCVUtilDAO;
import pe.com.ocv.model.ParametroGC;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVUtilService;

@Service
@SuppressWarnings("unchecked")
public class OCVUtilServiceImpl implements IOCVUtilService {

	@Autowired
	private IOCVUtilDAO dao;

	@Override
	public Respuesta ObtenerParametrosCategoria(Map<String, String> requestParm) {
		Respuesta respuesta = new Respuesta();
		Map<String, String> dato = new TreeMap<String, String>();
		try {
			String categoria = requestParm.get("categoria");
			Map<String, Object> queryResp = this.dao.obtenerParamCategoria(categoria);
						
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					dato.put((String) map.get("CLASE"), (String) map.get("VALOR"));
				}
			}
			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVUtilServiceImpl.ObtenerParametrosCategoria()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP(dato);
		return respuesta;
	}
	
	/*actualizacion obtener parametros por categoria*/
	@Override
	public Respuesta obtenerParametrosCategoriaV2(Map<String, String> requestParm) throws GmdException{
		Respuesta respuesta = new Respuesta();
		Map<String, String> dato = new TreeMap<String, String>();
		try {
			String categoria = requestParm.get("categoria");
			Map<String, Object> queryResp = dao.obtenerParamCategoriaV2(categoria);
			int iRespuesta = ((Integer) queryResp.get("nRESP_SP")).intValue();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					dato.put((String) map.get("CLASE"), (String) map.get("VALOR"));
				}
			}
			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVUtilServiceImpl.ObtenerParametrosCategoria()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP(dato);
		return respuesta;
	}

	@Override
	public Respuesta ObtenerParametrosGC(Map<String, String> requestParm) {
		List<ParametroGC> listaParametros = new ArrayList<ParametroGC>();
		Respuesta respuesta = new Respuesta();

		try {
			int pageNumber = Integer.parseInt(requestParm.get("page_num"));
			int pageSize = Integer.parseInt(requestParm.get("page_size"));
			//int adminEtic = Integer.parseInt(requestParm.get("admin_etic"));
			String correo = requestParm.get("correo");
			
			Map<String, Object> queryResp = this.dao.obtenerParametrosGC(pageNumber, pageSize, correo);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta > 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					ParametroGC parametro = new ParametroGC();
					parametro.setCategoria((String) map.get("categoria"));
					parametro.setClase((String) map.get("clase"));
					parametro.setF_alta((String) map.get("f_alta"));
					parametro.setFlag(((String) map.get("flag")).equals("1") ? true : false);
					parametro.setValor((String) map.get("valor"));
					parametro.setModulo((String) map.get("modulo"));
					listaParametros.add(parametro);
				}
				respuesta.setTotal(((BigDecimal) queryResp.get("TOTAL")).intValue());
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVUtilServiceImpl.ObtenerParametrosGC()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(listaParametros);
		return respuesta;
	}

	@Override
	public Respuesta InsertarParametrosGC(Map<String, String> requestParm) {
		ParametroGC parameter = new ParametroGC();
		Respuesta respuesta = new Respuesta();
		try {
			parameter.setClase(requestParm.get("clase"));
			parameter.setCategoria(requestParm.get("categoria"));
			parameter.setValor(requestParm.get("valor"));
			parameter.setModulo(requestParm.get("modulo"));

			Map<String, Object> queryResp = this.dao.insertarParametrosGC(parameter);
			respuesta.setnRESP_SP(((BigDecimal) queryResp.get("nRESP_SP")).intValueExact());
			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
		} catch (Exception e) {
			System.out.println("OCVUtilServiceImpl.InsertarParametrosGC()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		return respuesta;
	}

	@Override
	public Respuesta EditarParametrosGC(Map<String, String> requestParm) {
		ParametroGC parameter = new ParametroGC();
		Respuesta respuesta = new Respuesta();

		try {
			parameter.setCategoria(requestParm.get("categoria"));
			parameter.setClase(requestParm.get("clase"));
			parameter.setValor(requestParm.get("valor"));
			parameter.setFlag(Boolean.parseBoolean(requestParm.get("flag")));
			parameter.setModulo(requestParm.get("modulo"));

			Map<String, Object> queryResp = this.dao.editarParametrosGC(parameter);
			respuesta.setnRESP_SP(((BigDecimal) queryResp.get("nRESP_SP")).intValueExact());
			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
		} catch (Exception e) {
			System.out.println("OCVUtilServiceImpl.EditarParametrosGC()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		parameter.setClase(requestParm.get("clase"));

		return respuesta;
	}

}
