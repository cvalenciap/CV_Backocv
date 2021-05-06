package pe.com.ocv.api;

import java.text.ParseException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVSuministrosService;

@RestController
@RequestMapping(value = { "/api/suministros" }, method = { RequestMethod.POST }, produces = {
		"application/JSON" }, consumes = { "application/JSON" })
public class OCVSuministrosApi {

	@Autowired
	private IOCVSuministrosService service;

	public OCVSuministrosApi() {
	}

	@PostMapping(path = { "/lista-nis" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveListNisXCli(@RequestBody Map<String, String> requestParm)
			throws ParseException {
		Respuesta resultadoCons = service.ListaNisXCliente(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/detalle-nis" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveCabecera(@RequestBody Map<String, String> requestParm)
			throws ParseException {
		Respuesta restuladoCons = service.CabeceraXNis(requestParm);
		return new ResponseEntity<Respuesta>(restuladoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/historico-consumo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveHistoricoConsumo(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.HistoricoConsumo(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

}