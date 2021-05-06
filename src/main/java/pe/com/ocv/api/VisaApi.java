package pe.com.ocv.api;

import java.text.ParseException;
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

import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.service.IOCVUtilService;
import pe.com.ocv.service.IVisaService;

@RestController
@SuppressWarnings("unchecked")
@RequestMapping(value = { "/api/visa/" }, method = { RequestMethod.POST }, produces = {
		"application/JSON" }, consumes = { "application/JSON" })
public class VisaApi {

	@Autowired
	private IVisaService service;

	@Autowired
	private IOCVUtilService utilServ;

	@PostMapping(path = { "/session-visa" }, produces = { "application/json" }, consumes = { "application/json " })
	public ResponseEntity<Respuesta> ObtenerSessionVisa(@RequestBody Map<String, String> requestParm)
			throws ParseException {
		Map<String, String> dato = new TreeMap<String, String>();
		dato.put("categoria", "DATOS_VISA");
		Map<String, String> datosVISA = (Map<String, String>) utilServ.ObtenerParametrosCategoria(dato).getbRESP();
		datosVISA.put("amount", requestParm.get("amount"));
		Respuesta resultadoCons = service.getSession(datosVISA);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}
	
	/*adecuacion proceso pago*/
	@PostMapping(path = { "/payment-visa" }, produces = { "application/json" }, consumes = { "application/json " })
	public ResponseEntity<Respuesta> ObtenerPaymentResponse(@RequestBody Map<String, String> requestParm)
			throws ParseException, GmdException {
		Map<String, String> dato = new TreeMap<String, String>();
		dato.put("categoria", "DATOS_VISA");
		Map<String, String> datosVISA = (Map<String, String>) utilServ.ObtenerParametrosCategoria(dato).getbRESP();
		datosVISA.put("transactionToken", requestParm.get("transactionToken"));
		datosVISA.put("sessionToken", requestParm.get("sessionToken"));
		Respuesta resultadoCons = service.getPaymentResponse(datosVISA);
		return new ResponseEntity<Respuesta>(resultadoCons, HttpStatus.OK);
	}

}
