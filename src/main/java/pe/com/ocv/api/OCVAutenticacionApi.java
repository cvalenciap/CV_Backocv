package pe.com.ocv.api;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVAutenticacionService;

@RestController
@RequestMapping(value = { "/api/autenticacion-usuario/" }, method = { RequestMethod.POST }, produces = {
		"application/JSON" }, consumes = { "application/JSON" })
public class OCVAutenticacionApi {

	@Autowired
	private IOCVAutenticacionService service;

	public OCVAutenticacionApi() {
	}

	@PostMapping(path = { "/aut-nuevo-usu" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ValidarClave(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.validarClave(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/recuperar-contrasena" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ValidarCorreoRC(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.validarCorreoRecupClave(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/actualizar-contrasena" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ActualizarContrasena(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.actualizarClave(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*add method valid version*/
//	@PostMapping(path = { "/valida-version" }, produces = { "application/json" })
//	public ResponseEntity<Respuesta> validarVersion(@RequestBody Map<String, String> requestParm)
//			throws GmdException {
//		Respuesta resultadoCons = service.validarVersion(requestParm);
//		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
//	}
	/**/
	
	/*update method valid version*/
	@PostMapping(path = { "/valida-version" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> validarVersion(@RequestBody Map<String, String> requestParm)
			throws GmdException {
		Respuesta resultadoCons = service.validarVersiones(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

}