package pe.com.ocv.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVPagoEjecutadoService;

@RestController
@RequestMapping(value = { "/api/pago-ejecutado" }, method = { RequestMethod.POST }, produces = {
		"application/JSON" }, consumes = { "application/JSON" })
public class OCVPagoEjecutadoApi {

	@Autowired
	private IOCVPagoEjecutadoService service;

	public OCVPagoEjecutadoApi() {
	}

	@PostMapping(path = { "/obtener-resultado" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ObtenerResultado(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.ObtenerResultadoPago(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	@PostMapping(path = { "/enviar-correo-pago" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> EnviarCorreoPago(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.EnviarCorreoPago(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*init intermediate screen*/
	@PostMapping(path = { "/obtener-datos-pago" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> obtenerDatosPago(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.obtenerDatosPago(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	/*end intermediate screen*/
}
