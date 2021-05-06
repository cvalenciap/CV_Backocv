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
import pe.com.ocv.service.IOCVUtilService;

@RestController
@RequestMapping(value = { "/api/parametros" }, method = { RequestMethod.POST }, produces = {
		"application/JSON" }, consumes = { "application/JSON" })
public class OCVParametrosApi {

	@Autowired
	private IOCVUtilService service;

	public OCVParametrosApi() {
	}

	@PostMapping(path = { "/listar-parametros" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> ObtenerParametrosGC(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.ObtenerParametrosGC(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/insertar-parametro" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> InsertarParametroGC(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.InsertarParametrosGC(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/editar-parametro" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> EditarParametroGC(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.EditarParametrosGC(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

}
