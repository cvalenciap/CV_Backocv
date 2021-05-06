package pe.com.ocv.service.impl;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import pe.com.ocv.dao.IOCVUtilDAO;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IEnchufateService;
import pe.com.ocv.service.IOCVUtilService;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.JsonUtil;
import pe.com.ocv.util.ParametrosUtil;
import pe.com.ocv.util.Util;

import pe.com.gmd.util.exception.GmdException;

@Service
@SuppressWarnings("unchecked")
public class EnchufateServiceImpl implements IEnchufateService {
			
	@Autowired
	private IOCVUtilService utilService;
	
	@Autowired
	private IOCVUtilDAO utilDao;
	
	@SuppressWarnings("unused")
	@Override
	public String getToken(Map<String, String> requestParm) {
		String token = "";
		InputStream _is;
		try {
			// Creamos URL Connection
			String url = requestParm.get("base_uri") + requestParm.get("token_uri");
			URL myURL = new URL(url);
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();

			// Añadimos parámetros del servicio
			String urlParameters = "grant_type=password&username=" + requestParm.get("username") + "&password="
					+ requestParm.get("password") + "&applicantType=" + requestParm.get("applicant_type");

			// Seteamos propiedas en el URL Connection
			myURLConnection = Util.setUrlProperties(myURLConnection, requestParm.get("basic_auth_username"),
					requestParm.get("basic_auth_password"), Constantes.CONTENT_FORM_URLENCODED, Constantes.METHOD_POST,
					urlParameters);

			// Verificamos si la repsuesta es correcta o incorrecta
			if (myURLConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = myURLConnection.getInputStream();

				// Obtenemos la repsuesta
				StringBuffer response = Util.getResponse(myURLConnection.getInputStream());

				// Parseamos la repsuesta en un objeto JSON
				JsonObject jsonObj = new Gson().fromJson(response.toString(), JsonObject.class);

				// Seteamos el valor del token generado
				token = jsonObj.get("access_token").getAsString();
			} else {
				/* error from server */
				_is = myURLConnection.getErrorStream();
				token = "";
			}

		} catch (Exception e) {
			token = "";
		}
		return token;
	}
	
	@SuppressWarnings("null")
	/*adecuaciones proceso de pago*/
	@Override
	public Respuesta generarLiquidacion(Map<String, Object> requestParm) throws GmdException {
		Respuesta respuesta = new Respuesta(0, "");
		Map<String, Object> dato = new TreeMap<String, Object>();
		InputStream _is;
		List<Map<String, String>> listaInput = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> listaRegistroPrevio = new ArrayList<Map<String, Object>>();
		boolean flagMessage = false;
		try {
			/*validacion de input documentos*/
			listaInput = (List<Map<String, String>>) requestParm.get("documentos");
			if(listaInput == null || listaInput.isEmpty() || listaInput.size() == 0) {
				throw new GmdException("Lista de documentos vacia.");
			}
			/**/
			/*evaluación de flag modo*/
			if(requestParm.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
				// Creamos URL Connection
				String url = requestParm.get("url").toString().replace("nis_rad", requestParm.get("nis_rad").toString());
				URL myURL = new URL(url);
				HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();

				// Añadimos parámetros del servicio
				JsonObject body = new JsonObject();
				JsonArray documentos = new JsonArray();
				
				// Generacion de lista documentos
				for (Map<String, String> doc : listaInput) {
					JsonObject documento = new JsonObject();
					documento.addProperty("codigoServicio", requestParm.get("cod_servicio").toString());
					documento.addProperty("tipoDoc", requestParm.get("tipo_doc").toString());
					documento.addProperty("numeroDoc", doc.get("numeroDoc"));
					documentos.add(documento);
				}
				body.add("documentos", documentos);
				// Seteamos propiedades en el URL Connection
				myURLConnection = Util.setUrlProperties(myURLConnection, requestParm.get("access_token").toString(),
						Constantes.CONTENT_JSON, Constantes.METHOD_PUT, body.toString());

				// Verificamos si la repsuesta es correcta o incorrecta
				if (myURLConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
					_is = myURLConnection.getInputStream();

					// Obtenemos la respuesta
					StringBuffer response = Util.getResponse(_is);

					// Parseamos la repsuesta en un objeto JSON
					JsonObject jsonObj = new Gson().fromJson(response.toString(), JsonObject.class);
					/*forzar excepcion con numero liquidacion vacio y documentos vacios*/
					String numLiq = jsonObj.get("numeroLiquidacion").getAsString();
					if(numLiq == null || numLiq == "null" || StringUtils.isEmpty(numLiq.trim())) {
						throw new GmdException("Error en el número de liquidación generado.");
					}
					Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
					listaRegistroPrevio = JsonUtil.convertirCadenaJsonALista((JsonUtil.convertirObjetoACadenaJson(jsonObj.get("documentos"))), listType);
					if(listaRegistroPrevio.size() == 0 || documentos.size() != listaRegistroPrevio.size()) {
						flagMessage = true;
						/*get support parameters*/
						Map<String, String> parametrosSoporte = new HashMap<String, String>();
						Map<String, String> dataSupport = new TreeMap<String, String>();
						dataSupport.put(Constantes.CAMP_CATEGORIA, Constantes.CATEGORIA_SOPORTE);
						parametrosSoporte = (Map<String, String>) utilService.ObtenerParametrosCategoria(dataSupport).getbRESP();
						/**/
						String messageError = Constantes.MENSAJE_ERROR_GENERATE_LIQUI.replace(Constantes.REPLACE_MAIL_CONSTANT, parametrosSoporte.get("correo_soporte")).replace(Constantes.REPLACE_PHONE_CONSTANT,  parametrosSoporte.get("numero_soporte"));
						throw new GmdException(messageError);
					}
					dato.put("numeroLiquidacion", numLiq);
					dato.put("importeTotal", jsonObj.get("importeTotal").getAsString());
					dato.put("listaRegistros", JsonUtil.convertirObjetoACadenaJson(listaRegistroPrevio));
				} else {
					/* error from server */
					_is = myURLConnection.getErrorStream();
					StringBuffer response = Util.getResponse(_is);
					System.out.println("respuesta generar liquidacion: " + response.toString());
					flagMessage = true;
					throw new GmdException(Constantes.MENSAJE_ERROR_ENCHUFATE);
				}				
			}else if(requestParm.get("FLAG_MODO").equals(Constantes.FLAG_MODO_JOB)) {
				//generar numero de liquidacion por sequence
				String numLiq = String.valueOf(utilDao.obtenerNumLiquidacionSeq());
				Double importeTotal = 0.0;
				for (Map<String, String> doc : listaInput) {
					Map<String, Object> documento = new HashMap<String, Object>();
					documento.put("numeroDoc", doc.get("numeroDoc"));
					documento.put("fechaEmision", doc.get("fechaEmision"));
					documento.put("fechaVencimiento", doc.get("fechaVencimiento"));
					documento.put("deuda", doc.get("deuda"));
					importeTotal += ParametrosUtil.roundDouble(Double.valueOf(String.valueOf(doc.get("deuda"))),2);
					listaRegistroPrevio.add(documento);
				}
				dato.put("numeroLiquidacion", numLiq);
				dato.put("importeTotal", ParametrosUtil.roundDouble(importeTotal,2));
				dato.put("listaRegistros", JsonUtil.convertirObjetoACadenaJson(listaRegistroPrevio));
			} else {
				throw new GmdException("Error al obtener el parámetro FLAG_MODO del proceso.");
			}
			// Ponemos los resultados en el objeto Respuesta
			respuesta.setcRESP_SP("Ejecución Correcta");
			respuesta.setnRESP_SP(1);
			respuesta.setbRESP(dato);
		} catch (Exception e) {
			if(flagMessage) {
				respuesta.setcRESP_SP(e.getMessage());
			} else {
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_ENCHUFATE);
			}
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
			respuesta.setbRESP(dato);
		}
		return respuesta;
	}
	
	/*actualizacion de generacion de liquidacion*/
	@SuppressWarnings("null")
	/*adecuaciones proceso de pago*/
	@Override
	public Respuesta generarLiquidacion2(Map<String, Object> requestParm) throws GmdException {
		Respuesta respuesta = new Respuesta(0, "");
		Map<String, Object> dato = new TreeMap<String, Object>();
		InputStream _is;
		List<Map<String, String>> listaInput = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> listaRegistroPrevio = new ArrayList<Map<String, Object>>();
		boolean flagMessage = false;
		try {
			/*validacion de input documentos*/
			listaInput = (List<Map<String, String>>) requestParm.get("documentos");
			if(listaInput == null || listaInput.isEmpty() || listaInput.size() == 0) {
				throw new GmdException("Lista de documentos vacia.");
			}
			/**/
			/*evaluación de flag modo*/
			if(requestParm.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
				// Creamos URL Connection
				String url = requestParm.get("url").toString().replace("nis_rad", requestParm.get("nis_rad").toString());
				URL myURL = new URL(url);
				HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();

				// Añadimos parámetros del servicio
				JsonObject body = new JsonObject();
				JsonArray documentos = new JsonArray();
				
				// Generacion de lista documentos
				for (Map<String, String> doc : listaInput) {
					JsonObject documento = new JsonObject();
					documento.addProperty("codigoServicio", requestParm.get("cod_servicio").toString());
					documento.addProperty("tipoDoc", requestParm.get("tipo_doc").toString());
					documento.addProperty("numeroDoc", doc.get("numeroDoc"));
					documentos.add(documento);
				}
				body.add("documentos", documentos);
				// Seteamos propiedades en el URL Connection
				myURLConnection = Util.setUrlProperties(myURLConnection, requestParm.get("access_token").toString(),
						Constantes.CONTENT_JSON, Constantes.METHOD_PUT, body.toString());

				// Verificamos si la repsuesta es correcta o incorrecta
				if (myURLConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
					_is = myURLConnection.getInputStream();

					// Obtenemos la respuesta
					StringBuffer response = Util.getResponse(_is);

					// Parseamos la repsuesta en un objeto JSON
					JsonObject jsonObj = new Gson().fromJson(response.toString(), JsonObject.class);
					/*forzar excepcion con numero liquidacion vacio y documentos vacios*/
					String numLiq = jsonObj.get("numeroLiquidacion").getAsString();
					if(numLiq == null || numLiq == "null" || StringUtils.isEmpty(numLiq.trim())) {
						throw new GmdException("Error en el número de liquidación generado.");
					}
					Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
					listaRegistroPrevio = JsonUtil.convertirCadenaJsonALista((JsonUtil.convertirObjetoACadenaJson(jsonObj.get("documentos"))), listType);
					if(listaRegistroPrevio.size() == 0 || documentos.size() != listaRegistroPrevio.size()) {
						flagMessage = true;
						/*get support parameters*/
						Map<String, String> parametrosSoporte = new HashMap<String, String>();
						Map<String, String> dataSupport = new TreeMap<String, String>();
						dataSupport.put(Constantes.CAMP_CATEGORIA, Constantes.CATEGORIA_SOPORTE);
						parametrosSoporte = (Map<String, String>) utilService.ObtenerParametrosCategoria(dataSupport).getbRESP();
						/**/
						String messageError = Constantes.MENSAJE_ERROR_GENERATE_LIQUI.replace(Constantes.REPLACE_MAIL_CONSTANT, parametrosSoporte.get("correo_soporte")).replace(Constantes.REPLACE_PHONE_CONSTANT,  parametrosSoporte.get("numero_soporte"));
						throw new GmdException(messageError);
					}
					//generar numero de liquidacion por sequence
					String numLiqJob = String.valueOf(utilDao.obtenerNumLiquidacionSeq());
					dato.put("numeroLiquidacionJob", numLiqJob);
					dato.put("numeroLiquidacionEnchufate", numLiq);
					dato.put("importeTotal", jsonObj.get("importeTotal").getAsString());
					dato.put("listaRegistros", JsonUtil.convertirObjetoACadenaJson(listaRegistroPrevio));
				} else {
					/* error from server */
					_is = myURLConnection.getErrorStream();
					StringBuffer response = Util.getResponse(_is);
					System.out.println("respuesta generar liquidacion: " + response.toString());
					flagMessage = true;
					throw new GmdException(Constantes.MENSAJE_ERROR_ENCHUFATE);
				}				
			}else if(requestParm.get("FLAG_MODO").equals(Constantes.FLAG_MODO_JOB)) {
				//generar numero de liquidacion por sequence
				String numLiq = String.valueOf(utilDao.obtenerNumLiquidacionSeq());
				Double importeTotal = 0.0;
				for (Map<String, String> doc : listaInput) {
					Map<String, Object> documento = new HashMap<String, Object>();
					documento.put("numeroDoc", doc.get("numeroDoc"));
					documento.put("fechaEmision", doc.get("fechaEmision"));
					documento.put("fechaVencimiento", doc.get("fechaVencimiento"));
					documento.put("deuda", doc.get("deuda"));
					importeTotal += ParametrosUtil.roundDouble(Double.valueOf(String.valueOf(doc.get("deuda"))),2);
					listaRegistroPrevio.add(documento);
				}
				dato.put("numeroLiquidacionEnchufate", Constantes.NUM_LIQ_DEFAULT);
				dato.put("numeroLiquidacionJob", numLiq);
				dato.put("importeTotal", ParametrosUtil.roundDouble(importeTotal,2));
				dato.put("listaRegistros", JsonUtil.convertirObjetoACadenaJson(listaRegistroPrevio));
			} else {
				throw new GmdException("Error al obtener el parámetro FLAG_MODO del proceso.");
			}
			// Ponemos los resultados en el objeto Respuesta
			respuesta.setcRESP_SP("Ejecución Correcta");
			respuesta.setnRESP_SP(1);
			respuesta.setbRESP(dato);
		} catch (Exception e) {
			if(flagMessage) {
				respuesta.setcRESP_SP(e.getMessage());
			} else {
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_ENCHUFATE);
			}
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
			respuesta.setbRESP(dato);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta pagarLiquidacion(Map<String, String> requestParm) throws GmdException{
		InputStream _is;
		Respuesta respuesta = new Respuesta(0, "");
		Map<String, Object> dato = new TreeMap<String, Object>();
		try {
			// Creamos URL Connection
			String url = requestParm.get("url").replace("nis_rad", requestParm.get("nis_rad"));
			URL myURL = new URL(url);
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();

			// Añadimos parámetros del servicio
			JsonObject body = new JsonObject();
			body.addProperty("numeroLiquidacion", requestParm.get("numeroLiquidacion"));
			body.addProperty("numeroTarjeta", requestParm.get("numeroTarjeta"));
			body.addProperty("fechaPago", requestParm.get("fechaPago"));
			body.addProperty("horaPago", requestParm.get("horaPago"));
			body.addProperty("numOperacionTar", requestParm.get("numOperacionTar"));
			body.addProperty("tipoTarjeta", "Visa");

			// Seteamos propiedades en el URL Connection
			myURLConnection = Util.setUrlProperties(myURLConnection, requestParm.get("access_token").toString(),
					Constantes.CONTENT_JSON, Constantes.METHOD_PUT, body.toString());

			// Verificamos si la repsuesta es correcta o incorrecta
			if (myURLConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = myURLConnection.getInputStream();

				// Obtenemos la respuesta
				StringBuffer response = Util.getResponse(_is);

				// Parseamos la respuesta en un objeto JSON
				JsonObject jsonObj = new Gson().fromJson(response.toString(), JsonObject.class);
				dato.put("numeroLiquidacion", jsonObj.get("numeroLiquidacion").getAsString());
				dato.put("importeTotal", jsonObj.get("importeTotal").getAsString());
				dato.put("estado", jsonObj.get("estado").getAsString());
				dato.put("descripcionEstado", jsonObj.get("descripcionEstado").getAsString());

				// Ponemos los resultados en el objeto Repuesta
				respuesta.setcRESP_SP("Ejecución Correcta");
				respuesta.setnRESP_SP(1);
			} else {
				/* error from server */
				_is = myURLConnection.getErrorStream();
				// Obtenemos la respuesta
				respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_ENCHUFATE);
				respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
				throw new GmdException(respuesta.getcRESP_SP());
			}
			respuesta.setbRESP(dato);
		} catch (Exception e) {
			respuesta.setcRESP_SP(Constantes.MENSAJE_ERROR_ENCHUFATE);
			respuesta.setcRESP_SP2(e.getMessage());
			respuesta.setnRESP_SP(Constantes.ESTADO_FAILURE);
			respuesta.setbRESP(dato);
			throw new GmdException(respuesta.getcRESP_SP());
		}
		return respuesta;
	}

}
