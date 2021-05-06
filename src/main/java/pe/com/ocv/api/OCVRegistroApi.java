package pe.com.ocv.api;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVRegistroService;
import pe.com.ocv.service.IOCVUtilService;

@RestController
@RequestMapping(value = { "/api/registro-usuario" }, method = { RequestMethod.POST }, produces = {
		"application/JSON" }, consumes = { "application/JSON" })
public class OCVRegistroApi {

	@Autowired
	private IOCVRegistroService service;

	@Autowired
	private IOCVUtilService utilServ;

	public OCVRegistroApi() {
	}

	@SuppressWarnings("unchecked")
	@PostMapping(path = { "/nuevo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> InsertarOCVRegistro(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Map<String, String> dato = new TreeMap<String, String>();
		dato.put("categoria", "DATOS_CAPTCHA");
		Map<String, String> datosCaptcha = (Map<String, String>) utilServ.ObtenerParametrosCategoria(dato).getbRESP();
		requestParm.put("url", datosCaptcha.get("url"));
		requestParm.put("private_key", datosCaptcha.get("private_key"));
		Respuesta resultadoCons = service.insertaOCVRegistro(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/actualizar-datos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ActualizarOCVRegistro(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.actualizaOCVRegistro(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/obtener-datos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ObtenerDatosCliente(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.obtenerDatosCliente(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/valida-suministro" }, produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<Respuesta> ValidaNis(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.validaNis(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/valida-correo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ValidaCorreo(@RequestBody Map<String, String> requestParm) throws Exception {
		String correo = requestParm.get("correo");
		Respuesta resultadoCons = service.validaCorreo(correo);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/valida-ref-cobro" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ValidaRefCobro(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.validaRefCobro(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/vali-tipo-nro-doc" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ValidarTipoNroDoc(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.validaTipoNroDoc(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
//	[HttpGet]
//	[Route("GetAllAuthor")]  
//	[EnableCors("AllowOrigin")]
//	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(path = { "/confirmar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ConfirmarRegistro(@RequestBody Map<String, String> requestParm,
			HttpServletRequest request) throws Exception {
		Respuesta resultadoCons = service.confirmaRegistro(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*confirmacion de correo*/
	@PostMapping(path = { "/enviar-confirmacion" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> enviarConfirmacionRegistro(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.enviarConfirmacionRegistro(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*verificar codigo de confirmacion*/
	@PostMapping(path = { "/valida-codigo-confirmacion" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> validarCodigoConfirmacion(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.validarCodigoConfirmacion(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*enviar codigo de verificacion*/
	@PostMapping(path = { "/enviar-codigo-confirmacion" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> enviarCodigoConfirmacion(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.enviarCodigoConfirmacion(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	@PostMapping(path = { "/obtener-datos-cli-anti-fraude" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ObtenerDatosClienteAntiFraude(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.obtenerDatosClienteAntiFraude(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

}