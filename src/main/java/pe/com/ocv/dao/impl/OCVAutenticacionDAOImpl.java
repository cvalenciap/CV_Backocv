package pe.com.ocv.dao.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import pe.com.ocv.dao.IOCVAutenticacionDAO;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.DatasourceUtil;

@Repository
public class OCVAutenticacionDAOImpl implements IOCVAutenticacionDAO {
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public Map<String, Object> validarClave(String correo) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_AUTENTICACION).withProcedureName("PRC_VALIDAR_CLAVE")
					.declareParameters(new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlOutParameter("vCLAVE", OracleTypes.VARCHAR),
							new SqlOutParameter("vID_CLIENTE", OracleTypes.NUMBER),
							new SqlOutParameter("vNIS_RAD", OracleTypes.NUMBER),
							new SqlOutParameter("vADMIN_COM", OracleTypes.NUMBER),
							new SqlOutParameter("vADMIN_ETIC", OracleTypes.NUMBER),
							new SqlOutParameter("vFLAG", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCORREO", correo);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO LA VALIDACION DEL CORREO PARA AUTENTICACI\u00d3N");
		}
		return out;
	}
	
	@Override
	public Map<String, Object> registrarLog(String correo, int operacion, int suministro) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_AUTENTICACION).withProcedureName("PRC_INSERT_LOG")
					.declareParameters(new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlParameter("nTIPOPERACION", OracleTypes.NUMBER),
							new SqlParameter("nNISRAD", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCORREO", correo)
					.addValue("nTIPOPERACION", operacion)
					.addValue("nNISRAD", suministro);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR AL REGISTRAR LOG\u00d3N");
		}
		return out;
	}

	@Override
	public Map<String, Object> validarCorreoRecupClave(String correo) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_AUTENTICACION).withProcedureName("PRC_VALIDA_CORREO_RC")
					.declareParameters(new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlOutParameter("vNOMBRE", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCORREO", correo);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO LA VALIDACION DEL CORREO PARA RECUPERACI\u00d3N DE CONTRASE\u00d1A");
		}
		return out;
	}

	@Override
	public Map<String, Object> actualizaClave(String correo, String clave) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_AUTENTICACION).withProcedureName("PRC_ACTUALIZA_CLAVE")
					.declareParameters(new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlParameter("vCLAVE", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCORREO", correo).addValue("vCLAVE",
					clave);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO LA VALIDACION DEL CORREO PARA RECUPERACI\u00d3N DE CONTRASE\u00d1A");
		}
		return out;
	}

	

}