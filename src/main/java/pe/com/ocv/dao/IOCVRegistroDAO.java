package pe.com.ocv.dao;

import java.util.Map;
import pe.com.ocv.model.OCVRegistro;
import pe.com.ocv.model.Usuario;

public abstract interface IOCVRegistroDAO {
	
	public abstract Map<String, Object> insertaRegistro(OCVRegistro paramOCVRegistro, boolean migracion);
	
	public abstract Map<String, Object> actualizaRegistro(OCVRegistro paramOCVRegistro);
	
	public abstract Map<String, Object> obtenerDatosCliente(int id_cliente);

	public abstract Map<String, Object> validaNIS(int nis);

	public abstract Map<String, Object> validaCorreo(String correo);

	public abstract Map<String, Object> validaRefCobro(int nis, String refCobro);

	public abstract Map<String, Object> validaTipoNroDoc(int tipoDoc, String nroDoc, int nis_rad);

	public abstract Map<String, Object> validarToken(String token, char flag_act);

	public abstract Map<String, Object> actualizarEstadoUsuario(int idCliente, int idEstado);
	
	Usuario obtenerDatosUsuario(Integer idCliente, String correo) throws Exception;
	
	/*update code verify*/
	void actualizarCodigoVerificacion(String correo, Integer codeVerify) throws Exception;
	
	public abstract Map<String, Object> obtenerDatosClienteAntiFraude(String correo_cliente);
	
}