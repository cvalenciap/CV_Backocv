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

import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVRecibosService;

@RestController
@RequestMapping(value = { "/api/recibos" }, method = { RequestMethod.POST }, produces = {
		"application/JSON" }, consumes = { "application/JSON" })
public class OCVRecibosApi {

	@Autowired
	private IOCVRecibosService service;

	public OCVRecibosApi() {
	}

	@PostMapping(path = { "/lista-recibos-deudas-nis" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveRecibosPend(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta restuladoCons = service.ListaRecibosPendientesXNis(requestParm);
		return new ResponseEntity<Respuesta>(restuladoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/lista-recibos-pagados-nis" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveRecibosPagd(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta restuladoCons = service.ListaRecibosPagadosXNis(requestParm);
		return new ResponseEntity<Respuesta>(restuladoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/detalle-recibo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveDetalleReciboDeuda(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.DetalleRecibo(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/detalle-pagos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> devuelveDetallePago(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.DetallePagos(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/recibo-pdf" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> obtenerReciboPDF(@RequestBody Map<String, String> requestParm) throws Exception {
		Respuesta resultadoCons = service.ObtenerReciboPDF(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

	@PostMapping(path = { "/validar-recibo-anterior" }, produces = { "application/json" })
	public ResponseEntity<Respuesta> validarReciboAnterior(@RequestBody Map<String, String> requestParm)
			throws Exception {
		Respuesta resultadoCons = service.ValidarReciboAnterior(requestParm);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

}
