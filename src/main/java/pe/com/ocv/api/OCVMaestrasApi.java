package pe.com.ocv.api;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.PagoEjecutado;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVMaestrasService;
import pe.com.ocv.service.IOCVUtilService;
import pe.com.ocv.service.IVisaService;

@RestController
@SuppressWarnings("unchecked")
@RequestMapping(value = { "/api/general" }, method = { RequestMethod.POST }, produces = { "application/JSON" })
public class OCVMaestrasApi {

	@Autowired
	private IOCVMaestrasService service;

	@Autowired
	private IOCVUtilService utilServ;

	@Autowired
	private IVisaService visaServ;

	public OCVMaestrasApi() {
	}

	@GetMapping(path = { "/carga-pre-definida" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ObtenerMaestros(@RequestParam Map<String, String> requestParm)
			throws ParseException {
		Respuesta resultadoCons = service.devuelveMaestras();
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@GetMapping(path = { "/migracion-usuarios" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> MigrarUsuarios(@RequestParam Map<String, String> requestParm)
			throws ParseException {
		Respuesta resultadoCons = service.migrarUsuariosAntiguos();
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@GetMapping(path = { "/titulos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ObtenerTitulos(@RequestParam Map<String, String> requestParm)
			throws ParseException {
		Respuesta resultadoCons = service.obtenerTitulos();
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/parametros-categoria" }, produces = { "application/json" }, consumes = {
			"application/json " })
	public ResponseEntity<Respuesta> ObtenerDatosVisa(@RequestBody Map<String, String> requestParm)
			throws ParseException {
		Respuesta resultadoCons = utilServ.ObtenerParametrosCategoria(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

//	@PostMapping(path = { "/pago-ejecutado" }, produces = { "application/json" }, consumes = {
//			"application/x-www-form-urlencoded" })
//	public void TestVISA(HttpServletResponse response, HttpServletRequest request) throws ParseException, IOException {
//		// Traemos datos de VISA
//		Map<String, String> dato = new TreeMap<String, String>();
//		dato.put("categoria", "DATOS_VISA");
//		Map<String, String> datosVISA = (Map<String, String>) utilServ.ObtenerParametrosCategoria(dato).getbRESP();
//
//		// Obtenemos sessionToken y transactionToken de VISA
//		datosVISA.put("transactionToken", request.getParameter("transactionToken"));
//		datosVISA.put("channel", request.getParameter("channel"));
//		datosVISA.put("correo", request.getParameter("customerEmail"));
//		datosVISA.put("liquidacion", request.getParameter("liquidacion"));
//		datosVISA.put("nis_rad", request.getParameter("nis_rad"));
//		datosVISA.put("amount", request.getParameter("amount"));
//		//get parameters for audit
//		datosVISA.put("auth_correo", request.getParameter("auth_correo"));
//		datosVISA.put("flagChannel", request.getParameter("flagChannel"));
//
//		// Ejecutamos validación de pago de VISA
//		System.out.println("ingreso getpaymentresponse");
//		Respuesta respuesta = visaServ.getPaymentResponse(datosVISA);
//
//		// Redireccionamos a Front-end con Transaction ID
//		String trxId = ((PagoEjecutado) respuesta.getbRESP()).getTrxId();
////		init intermediate screen
////		response.sendRedirect(respuesta.getcRESP_SP() + "consulta-recibos/" + trxId);
//		response.sendRedirect(respuesta.getcRESP_SP() + "respuesta-operacion/" + trxId + '/' + request.getParameter("customerEmail"));
////		end intermediate screen
//	}
	
	/*adecuacion proceso pago*/
	@PostMapping(path = { "/pago-ejecutado" }, produces = { "application/json" }, consumes = {
		"application/x-www-form-urlencoded" })
	public void TestVISA(HttpServletResponse response, HttpServletRequest request) throws ParseException, IOException, GmdException {
		Map<String, String> dato = new TreeMap<String, String>();
		String trxId = "";
		String urlFinal ="";
		try {
			// Traemos datos de VISA
			dato.put("categoria", "DATOS_VISA");
			Map<String, String> datosVISA = (Map<String, String>) utilServ.ObtenerParametrosCategoria(dato).getbRESP();
			
			// Obtenemos sessionToken y transactionToken de VISA
			datosVISA.put("transactionToken", request.getParameter("transactionToken"));
			datosVISA.put("channel", request.getParameter("channel"));
			datosVISA.put("correo", request.getParameter("customerEmail"));
			datosVISA.put("liquidacion", request.getParameter("liquidacion"));
			datosVISA.put("nis_rad", request.getParameter("nis_rad"));
			datosVISA.put("amount", request.getParameter("amount"));
			//get parameters for audit
			datosVISA.put("auth_correo", request.getParameter("auth_correo"));
			datosVISA.put("flagChannel", request.getParameter("flagChannel"));
			/*adecuacion proceso pago*/
			datosVISA.put("listaRegistros", request.getParameter("listaRegistros"));
			
			// Ejecutamos validación de pago de VISA
			System.out.println("ingreso getpaymentresponse");
			Respuesta respuesta = visaServ.getPaymentResponse(datosVISA);
			
			//obtener url de redireccion
			dato.put("categoria", "URL_WEB");
			Map<String, String> datosURL = (Map<String, String>) utilServ.ObtenerParametrosCategoria(dato).getbRESP();
			urlFinal = datosURL.get("URL_OCV_WEB");
			
			// Redireccionamos a Front-end con Transaction ID
			trxId = ((PagoEjecutado) respuesta.getbRESP()).getTrxId();
		} catch (Exception exception) {
			System.out.println("PagoEjecutadoApi()");
			System.err.println("Ocurrió un error en el proceso : " + exception);
		}
		//init intermediate screen
//		PRUEBA
//		response.sendRedirect("http://localhost:4200/#/" + "respuesta-operacion/" + trxId + '/' + request.getParameter("customerEmail"));
		response.sendRedirect(urlFinal + "respuesta-operacion/" + trxId + '/' + request.getParameter("customerEmail"));
		//end intermediate screen
	}
	
	/*actualizacion mastercard*/
	@PostMapping(path = { "/pago-ejecutado-v2" }, produces = { "application/json" }, consumes = {
		"application/x-www-form-urlencoded" })
	public void ejecucionPagoVisaWeb(HttpServletResponse response, HttpServletRequest request) throws ParseException, IOException, GmdException {
		Map<String, String> dato = new TreeMap<String, String>();
		String trxId = "";
		String urlFinal = "";
		try {
			// Traemos datos de VISA
			dato.put("categoria", "DATOS_VISA");
			Map<String, String> datosVISA = (Map<String, String>) utilServ.obtenerParametrosCategoriaV2(dato).getbRESP();
			
			// Obtenemos sessionToken y transactionToken de VISA
			datosVISA.put("transactionToken", request.getParameter("transactionToken"));
			datosVISA.put("channel", request.getParameter("channel"));
			datosVISA.put("correo", request.getParameter("customerEmail"));
			datosVISA.put("liquidacionJob", request.getParameter("liquidacionJob"));
			datosVISA.put("liquidacionEnchufate", request.getParameter("liquidacionEnchufate"));
			datosVISA.put("nis_rad", request.getParameter("nis_rad"));
			datosVISA.put("amount", request.getParameter("amount"));
			//get parameters for audit
			datosVISA.put("auth_correo", request.getParameter("auth_correo"));
			datosVISA.put("flagChannel", request.getParameter("flagChannel"));
			/*adecuacion proceso pago*/
			datosVISA.put("listaRegistros", request.getParameter("listaRegistros"));
			
			// Ejecutamos validación de pago de VISA
			System.out.println("ingreso getpaymentresponse version 2");
			Respuesta respuesta = visaServ.getPaymentResponseV2(datosVISA);
			
			//obtener url de redireccion
			dato.put("categoria", "URL_WEB");
			Map<String, String> datosURL = (Map<String, String>) utilServ.obtenerParametrosCategoriaV2(dato).getbRESP();
			urlFinal = datosURL.get("URL_OCV_WEB");
			trxId = ((PagoEjecutado) respuesta.getbRESP()).getTrxId();
		} catch (Exception exception) {
			System.out.println("PagoEjecutadoApi()");
			System.err.println("Ocurrió un error en el proceso : " + exception);
		}
		// Redireccionamos a Front-end con Transaction ID obtenida en proceso
//		response.sendRedirect("http://localhost:4200/#/" + "respuesta-operacion/" + trxId + '/' + request.getParameter("customerEmail"));
		response.sendRedirect(urlFinal + "respuesta-operacion/" + trxId + '/' + request.getParameter("customerEmail"));
	}
	
//	inicio cvalenciap
	@PostMapping(path = { "/insertar-pago-movil" }, produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<Respuesta> insertarPagoMovil(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = visaServ.insertarPagoMovil(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
//	fin cvalenciap
	
}