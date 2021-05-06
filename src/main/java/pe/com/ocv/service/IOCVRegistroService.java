package pe.com.ocv.service;

import java.text.ParseException;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

import pe.com.ocv.model.Respuesta;

@Transactional
public interface IOCVRegistroService {

	Respuesta insertaOCVRegistro(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta actualizaOCVRegistro(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta obtenerDatosCliente(Map<String, String> requestParm);

	Respuesta validaNis(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta validaCorreo(String correo);

	Respuesta validaRefCobro(Map<String, String> requestParm);

	Respuesta validaTipoNroDoc(Map<String, String> requestParm);

	Respuesta confirmaRegistro(Map<String, String> requestParm) throws ParseException, Exception;

	Respuesta actualizarEstadoUsuario(Map<String, String> requestParm) throws ParseException, Exception;
	
	Respuesta enviarConfirmacionRegistro(Map<String, String> requestParm) throws ParseException, Exception;
	
	/*add code verification*/
	Respuesta validarCodigoConfirmacion(Map<String, String> requestParm) throws Exception; 
	
	/*add send code verification*/
	Respuesta enviarCodigoConfirmacion(Map<String, String> requestParm) throws Exception;
	
	Respuesta obtenerDatosClienteAntiFraude(Map<String, String> requestParm);

}