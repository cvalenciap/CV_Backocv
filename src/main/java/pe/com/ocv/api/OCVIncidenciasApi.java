package pe.com.ocv.api;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVIncidenciaService;

@RestController
@RequestMapping(value = { "/api/incidencias" }, method = { RequestMethod.POST }, produces = { "application/JSON" })
public class OCVIncidenciasApi {

	@Autowired
	private IOCVIncidenciaService service;

	public OCVIncidenciasApi() {
	}

	@GetMapping(path = { "/municipios-afectados" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveMunicipiosAfectados() throws Exception {
		Respuesta resultadoCons = service.ListaMunicipiosAfectados();
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/incidencias-municipios" }, produces = { "application/json" }, consumes = {
			"application/JSON" })
	public ResponseEntity<Respuesta> devuelveIncidenciasMunicipios(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.ListaIncidenciasMunicipio(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/incidencias-suministro" }, produces = { "application/json" }, consumes = {
			"application/JSON" })
	public ResponseEntity<Respuesta> devuelveIncidenciasSuministro(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.ListaIncidenciasSuministro(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}


	@PostMapping(path = { "/areas-afectadas" }, produces = { "application/json" }, consumes = { "application/JSON" })
	public ResponseEntity<Respuesta> devuelveAreasAfectadas(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.ListaAreasAfectadas(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/predio-afectado" }, produces = { "application/json" }, consumes = { "application/JSON" })
	public ResponseEntity<Respuesta> devuelvePredioAfectado(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.ObtenerPredioAfectado(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

}