package pe.com.ocv.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IEnchufateService;
import pe.com.ocv.service.IOCVPagoEjecutadoService;
import pe.com.ocv.service.IOCVUtilService;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.JsonUtil;

@RestController
@SuppressWarnings("unchecked")
@RequestMapping(value = { "/api/enchufate/" }, method = { RequestMethod.POST }, produces = {
		"application/JSON" }, consumes = { "application/JSON" })
public class EnchufateApi {

	@Autowired
	private IEnchufateService service;

	@Autowired
	private IOCVUtilService utilServ;
	
	@Autowired
	private IOCVPagoEjecutadoService pagoService;

	public EnchufateApi() {
	}

	/*adecuacion de proceso pago*/
	@PostMapping(path = { "/generar-liquidacion" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> GenerarLiquidacion(@RequestBody Map<String, Object> requestParm) throws GmdException {
		Map<String, String> dato = new TreeMap<String, String>();
		Respuesta resultadoCons = new Respuesta();
		try {
			dato.put("categoria", "DATOS_ENCHUFATE");
			Map<String, String> datosEnchufate = (Map<String, String>) utilServ.ObtenerParametrosCategoria(dato).getbRESP();
			requestParm.put("cod_servicio", datosEnchufate.get("cod_servicio"));
			requestParm.put("tipo_doc", datosEnchufate.get("tipo_doc"));		
			if(datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
				requestParm.put("access_token", service.getToken(datosEnchufate));
				requestParm.put("url", datosEnchufate.get("base_uri") + datosEnchufate.get("generar_liqui_uri"));
			}			
			requestParm.put("FLAG_MODO", datosEnchufate.get("FLAG_MODO"));
			resultadoCons = service.generarLiquidacion(requestParm);
		} catch (Exception exception) {
			resultadoCons.setcRESP_SP(Constantes.MENSAJE_ERROR_ENCHUFATE);
			resultadoCons.setnRESP_SP(Constantes.ESTADO_FAILURE);
			resultadoCons.setbRESP("");
		}
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*actualización de generacion de liquidacion*/
	@PostMapping(path = { "/generar-liquidacion-v2" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> generarLiquidacion(@RequestBody Map<String, Object> requestParm) throws GmdException {
		Map<String, String> dato = new TreeMap<String, String>();
		Respuesta resultadoCons = new Respuesta();
		try {
			dato.put("categoria", "DATOS_ENCHUFATE");
			Map<String, String> datosEnchufate = (Map<String, String>) utilServ.obtenerParametrosCategoriaV2(dato).getbRESP();
			requestParm.put("cod_servicio", datosEnchufate.get("cod_servicio"));
			requestParm.put("tipo_doc", datosEnchufate.get("tipo_doc"));		
			if(datosEnchufate.get("FLAG_MODO").equals(Constantes.FLAG_MODO_ENCHUFATE)) {
				requestParm.put("access_token", service.getToken(datosEnchufate));
				requestParm.put("url", datosEnchufate.get("base_uri") + datosEnchufate.get("generar_liqui_uri"));
			}			
			requestParm.put("FLAG_MODO", datosEnchufate.get("FLAG_MODO"));
			resultadoCons = service.generarLiquidacion2(requestParm);
		} catch (Exception exception) {
			resultadoCons.setcRESP_SP(Constantes.MENSAJE_ERROR_ENCHUFATE);
			resultadoCons.setnRESP_SP(Constantes.ESTADO_FAILURE);
			resultadoCons.setbRESP("");
		}
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*adecuacion proceso pago*/
	@PostMapping(path = { "pagar-liquidacion" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> PagarLiquidacion(@RequestBody Map<String, Object> requestParm) throws GmdException {
		Respuesta resultadoCons = new Respuesta();
		Map<String, String> datos = new HashMap<String, String>();
		try {
			/*INSERTAR JSON PARSE EN TABLA*/
			resultadoCons = pagoService.insertarRegistroLiquidacionVisa(JsonUtil.convertirObjetoACadenaJson(requestParm));
			// Obtenemos parametros de request
			datos.put("correo", (String) requestParm.get("customerEmail"));//no llega
			datos.put("liquidacion", (String) requestParm.get("numeroLiquidacion"));
			datos.put("nis_rad", (String) requestParm.get("nisRad"));
			datos.put("amount", (String) requestParm.get("amount"));//no llega
			datos.put("tipoTarjeta", (String) requestParm.get("tipoTarjeta"));//verifCard
			//parametros de transaccion
			datos.put("TRANSACTION_ID", (String) requestParm.get("TRANSACTION_ID"));
			datos.put("ACTION_CODE", (String) requestParm.get("ACTION_CODE"));
			datos.put("STATUS", (String) requestParm.get("STATUS"));
			datos.put("TRANSACTION_DATE", (String) requestParm.get("TRANSACTION_DATE"));
			datos.put("ACTION_DESCRIPTION", (String) requestParm.get("ACTION_DESCRIPTION"));
			datos.put("TRACE_NUMBER", (String) requestParm.get("TRACE_NUMBER"));
			datos.put("CARD", (String) requestParm.get("CARD"));
			datos.put("BRAND", (String) requestParm.get("BRAND"));//verifCard
			//get parameters for audit
			datos.put("auth_correo", (String) requestParm.get("authCorreo"));
			datos.put("flagChannel", (String) requestParm.get("flagChannel"));
			/*adecuacion proceso pago*/
			datos.put("listaRegistros", String.valueOf(requestParm.get("listaRegistros")));
			
			// Ejecutamos proceso de pago movil
			System.out.println("ingreso getpaymentresponse movil");
			resultadoCons = pagoService.getPaymentResponseMovil(datos);
		} catch (Exception exception) {
			System.out.println("PagoEjecutadoMovilApi()");
			System.err.println("Ocurrió un error en el proceso : " + exception);
			resultadoCons.setcRESP_SP("Ocurrió un error en el proceso : " + exception);
			resultadoCons.setnRESP_SP(Constantes.ESTADO_FAILURE);
		}
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*adecuacion mastercar pago movil*/
	@PostMapping(path = { "pagar-liquidacion-v2" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> pagarLiquidacionV2(@RequestBody Map<String, Object> requestParm) throws GmdException {
		Respuesta resultadoCons = new Respuesta();
		Map<String, String> datos = new HashMap<String, String>();
		try {
			// Obtenemos parametros de request
			datos.put("correo", (String) requestParm.get("customerEmail"));//no llega
			datos.put("liquidacionEnchufate", (String) requestParm.get("liquidacionEnchufate"));
			datos.put("liquidacionJob", (String) requestParm.get("liquidacionJob"));
			datos.put("nis_rad", (String) requestParm.get("nisRad"));
			datos.put("amount", (String) requestParm.get("amount"));//no llega
			datos.put("tipoTarjeta", (String) requestParm.get("tipoTarjeta"));//verifCard
			//parametros de transaccion
			datos.put("TRANSACTION_ID", (String) requestParm.get("TRANSACTION_ID"));
			datos.put("ACTION_CODE", (String) requestParm.get("ACTION_CODE"));
			datos.put("STATUS", (String) requestParm.get("STATUS"));
			datos.put("TRANSACTION_DATE", (String) requestParm.get("TRANSACTION_DATE"));
			datos.put("ACTION_DESCRIPTION", (String) requestParm.get("ACTION_DESCRIPTION"));
			datos.put("TRACE_NUMBER", (String) requestParm.get("TRACE_NUMBER"));
			datos.put("CARD", (String) requestParm.get("CARD"));
			datos.put("BRAND", (String) requestParm.get("BRAND"));//verifCard
			//get parameters for audit
			datos.put("auth_correo", (String) requestParm.get("authCorreo"));
			datos.put("flagChannel", (String) requestParm.get("flagChannel"));
			/*adecuacion proceso pago*/
			datos.put("listaRegistros", String.valueOf(requestParm.get("listaRegistros")));
			
			// Ejecutamos proceso de pago movil
			System.out.println("ingreso getpaymentresponse movil");
			resultadoCons = pagoService.getPaymentResponseMovilV2(datos);
		} catch (Exception exception) {
			System.out.println("PagoEjecutadoMovilApi()");
			System.err.println("Ocurrió un error en el proceso : " + exception);
			resultadoCons.setcRESP_SP("Ocurrió un error en el proceso : " + exception);
			resultadoCons.setnRESP_SP(Constantes.ESTADO_FAILURE);
		}
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

}
