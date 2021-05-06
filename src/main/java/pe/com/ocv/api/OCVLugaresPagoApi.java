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
import pe.com.ocv.service.IOCVLugaresPagoService;

@RestController
@RequestMapping(value = { "/api/lugares-pago" }, method = { RequestMethod.POST }, produces = { "application/JSON" })
public class OCVLugaresPagoApi {

	@Autowired
	private IOCVLugaresPagoService service;

	public OCVLugaresPagoApi() {
	}

	@GetMapping(path = { "/lista-agencias" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveAgencias() throws Exception {
		Respuesta restuladoCons = service.ListaAgencias();
		return new ResponseEntity<Respuesta>(restuladoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/lista-sucursales" }, produces = { "application/json" }, consumes = { "application/JSON" })
	public ResponseEntity<Respuesta> devuelveSucursales(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta restuladoCons = service.ListaSucursales(requestParm);
		return new ResponseEntity<Respuesta>(restuladoCons, HttpStatus.OK);
	}

}