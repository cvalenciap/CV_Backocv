package pe.com.ocv.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.dao.IOCVAuditoriaDAO;
import pe.com.ocv.model.Auditoria;
import pe.com.ocv.model.ParametroGC;
import pe.com.ocv.util.ConstantesDAO;
import pe.com.ocv.util.ExecuteProcedure;

@Repository
public class OCVAuditoriaDAOImpl implements IOCVAuditoriaDAO{
	
	/*the environment*/
	@Autowired
	private Environment environment;
	
	/*the template*/
	@Autowired
	private JdbcTemplate jdbc;
	
	/*the executor*/
	private ExecuteProcedure execSp;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Auditoria> obtenerRelacionCanalAuditoria(Integer idCanalAuditoria, Integer idCanal, Integer idAuditoria) throws Exception {
		List<Auditoria> lstRetorno = null;
		SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
		caller.withCatalogName(ConstantesDAO.PKG_AUDITORIA).withProcedureName(ConstantesDAO.PRC_OBTENER_CANAL_AUDITORIA)
				.declareParameters(
						new SqlParameter(ConstantesDAO.PAR_N_ID_CANAL_AUDITORIA, Types.DECIMAL),
						new SqlParameter(ConstantesDAO.PAR_N_ID_CANAL, Types.DECIMAL),
						new SqlParameter(ConstantesDAO.PAR_N_ID_TIPO_AUDITORIA, Types.DECIMAL),
						new SqlOutParameter(ConstantesDAO.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<Auditoria>() {
							@Override
							public Auditoria mapRow(ResultSet rs, int rowNum) throws SQLException {
								Auditoria record = new Auditoria();
								record.setIdCanalAuditoria(rs.getInt(1));
								record.setIdCanal(rs.getInt(2));
								record.setDesCanal(rs.getString(3));
								record.setDesCanalCorta(rs.getString(4));
								record.setIdAuditoria(rs.getInt(5));
								record.setDesAuditoria(rs.getString(6));
								record.setDesAuditoriaCorta(rs.getString(7));
								return record;										
							}
						}),
						new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, Types.VARCHAR),
						new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, Types.VARCHAR))
				.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ConstantesDAO.PAR_N_ID_CANAL_AUDITORIA, idCanalAuditoria);
		params.addValue(ConstantesDAO.PAR_N_ID_CANAL, idCanal);
		params.addValue(ConstantesDAO.PAR_N_ID_TIPO_AUDITORIA, idAuditoria);
		Map<String, Object> results = caller.execute(params);		
		lstRetorno = (List<Auditoria>) results.get(ConstantesDAO.PAR_OUT_CURSOR);
		return lstRetorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> registrarLog(String correo, Integer idCanalAuditoria , Integer suministro) throws Exception {
		Map<String, Object> out = new HashMap<String, Object>();
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_CORREO, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_ID_CANAL_AUDITORIA, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_NIS_RAD, OracleTypes.DECIMAL));			
			
			paramsOutput = new ArrayList<SqlOutParameter>();
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, OracleTypes.VARCHAR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, OracleTypes.NUMBER));
			
			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_AUDITORIA+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_INSERTAR_LOG, 
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_V_CORREO, correo);
			inputs.put(ConstantesDAO.PAR_N_ID_CANAL_AUDITORIA, idCanalAuditoria);
			inputs.put(ConstantesDAO.PAR_N_NIS_RAD, suministro);
					
			// Ejecutamos el store procedure
			out = (Map<String, Object>) this.execSp.executeSp(inputs);
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR INSERTANDO AUDITORIA");
		}
		return out;	
	}
	
	/*cvalenciap-16/04/2020: inicio verificacion version*/
	@SuppressWarnings("unchecked")
	@Override
	public List<ParametroGC> compararVersionMovil(String version) throws Exception {
		List<ParametroGC> lstRetorno = null;
		SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
		caller.withCatalogName(ConstantesDAO.PKG_AUTENTICACION).withProcedureName(ConstantesDAO.PRC_COMPARE_VERSION_APK)
				.declareParameters(
						new SqlParameter(ConstantesDAO.PAR_V_VALOR, Types.VARCHAR),
						new SqlOutParameter(ConstantesDAO.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<ParametroGC>() {
							@Override
							public ParametroGC mapRow(ResultSet rs, int rowNum) throws SQLException {
								ParametroGC record = new ParametroGC();
								record.setClase(rs.getString(5));
								record.setCategoria(rs.getString(6));
								record.setValor(rs.getString(7));
								record.setF_alta(rs.getString(9));
								record.setModulo(rs.getString(11));
								return record;										
							}
						}),
						new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, Types.VARCHAR),
						new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, Types.VARCHAR))
				.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ConstantesDAO.PAR_V_VALOR, version);
		Map<String, Object> results = caller.execute(params);		
		lstRetorno = (List<ParametroGC>) results.get(ConstantesDAO.PAR_OUT_CURSOR);
		return lstRetorno;
	}
	/*cvalenciap-16/04/2020: fin verificacion version*/
	
	/*gjaramillo-14/08/2020: inicio verificacion version*/
//	@Override
//	public Respuesta compararVersionAPK(String version) throws Exception {
//		Respuesta respuesta = new Respuesta();
//		Map<String, Object> datos = new TreeMap<String, Object>();
//		SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
//		caller.withCatalogName(ConstantesDAO.PKG_AUTENTICACION).withProcedureName(ConstantesDAO.PRC_VALIDA_APK)
//				.declareParameters(
//						new SqlParameter(ConstantesDAO.PAR_V_VALOR, Types.VARCHAR),
//						new SqlOutParameter(ConstantesDAO.CRESP_SP, Types.VARCHAR),
//						new SqlOutParameter(ConstantesDAO.NRESP_SP, Types.INTEGER),
//						new SqlOutParameter(ConstantesDAO.CCODIGOERROR, Types.VARCHAR),
//						new SqlOutParameter(ConstantesDAO.NESOBLIGATORIO, Types.INTEGER))
//				.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue(ConstantesDAO.PAR_V_VALOR, version);
//		Map<String, Object> results = caller.execute(params);
//		respuesta.setcRESP_SP((String) results.get(ConstantesDAO.CRESP_SP));
//		respuesta.setnRESP_SP((Integer) results.get(ConstantesDAO.NRESP_SP));
//		datos.put("codigoError", (String) results.get(ConstantesDAO.CCODIGOERROR));
//		datos.put("esObligatorio", (Integer) results.get(ConstantesDAO.NESOBLIGATORIO));
//		respuesta.setbRESP(datos);
//		System.out.println("Respuesta - DAO");
//		System.out.println("cResp_SP: " + respuesta.getcRESP_SP());
//		System.out.println("nResp_SP: " + respuesta.getnRESP_SP());
//		System.out.println("bResp_SP: " + respuesta.getbRESP());
//		return respuesta;
//	}
	/*gjaramillo-14/08/2020: fin verificacion version*/
	
	/*verificacion de version APK*/
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> compararVersionAPK(String version) throws GmdException {
		Map<String, Object> out = new HashMap<String, Object>();
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_VALOR, OracleTypes.VARCHAR));		
			
			paramsOutput = new ArrayList<SqlOutParameter>();
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, OracleTypes.VARCHAR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, OracleTypes.INTEGER));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.CCODIGOERROR, OracleTypes.VARCHAR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.NESOBLIGATORIO, OracleTypes.INTEGER));
			
			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_AUTENTICACION+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_VALIDA_APK, 
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_V_VALOR, version);
					
			// Ejecutamos el store procedure
			out = (Map<String, Object>) this.execSp.executeSp(inputs);
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR PROCEDIMIENTO VALIDA VERSION APK");
			throw new GmdException(excepcion);
		}
		return out;
	 }
	
	/*verificacion de version IOs*/
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> compararVersionIOS(String version) throws GmdException {
		Map<String, Object> out = new HashMap<String, Object>();
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_VALOR, OracleTypes.VARCHAR));		
			
			paramsOutput = new ArrayList<SqlOutParameter>();
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, OracleTypes.VARCHAR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, OracleTypes.INTEGER));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.CCODIGOERROR, OracleTypes.VARCHAR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.NESOBLIGATORIO, OracleTypes.INTEGER));
			
			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_AUTENTICACION+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_VALIDA_IOS, 
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_V_VALOR, version);
					
			// Ejecutamos el store procedure
			out = (Map<String, Object>) this.execSp.executeSp(inputs);
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR PROCEDIMIENTO VALIDA VERSION IOS");
			throw new GmdException(excepcion);
		}
		return out;
	 }

}
