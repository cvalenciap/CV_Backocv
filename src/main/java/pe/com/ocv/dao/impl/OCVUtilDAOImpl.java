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

import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.GmdException;
import pe.com.ocv.dao.IOCVUtilDAO;
import pe.com.ocv.model.ParametroGC;
import pe.com.ocv.model.TipoTarjeta;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.ConstantesDAO;
import pe.com.ocv.util.DatasourceUtil;
import pe.com.ocv.util.ExecuteProcedure;

@Repository
public class OCVUtilDAOImpl implements IOCVUtilDAO {
	
	/*the environment*/
	@Autowired
	private Environment environment;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	/*the executor*/
	private ExecuteProcedure execSp;

	@Override
	public void enviarCorreo(String to, String subject, String body) {
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_UTIL).withProcedureName("PRC_ENVIO_CORREO")
					.declareParameters(new SqlParameter("p_to", OracleTypes.VARCHAR),
							new SqlParameter("p_from", OracleTypes.VARCHAR),
							new SqlParameter("p_subject", OracleTypes.VARCHAR),
							new SqlParameter("p_text_msg", OracleTypes.VARCHAR),
							new SqlParameter("p_html_msg", OracleTypes.VARCHAR),
							new SqlParameter("p_smtp_host", OracleTypes.VARCHAR),
							new SqlParameter("p_smtp_port", OracleTypes.VARCHAR));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("p_to", to)
					.addValue("p_from", "ozegarra@canvia.com").addValue("p_subject", subject)
					.addValue("p_text_msg", subject).addValue("p_html_msg", body)
					.addValue("p_smtp_host", "mail.sedapal.com.pe").addValue("p_smtp_port", 25);
			jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO EL ENVIO DE CORREO.");
		}
	}

	@Override
	public Map<String, Object> obtenerParamCategoria(String categoria) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_UTIL).withProcedureName("PRC_OBTENER_PARAM_CATEGORIA")
					.declareParameters(new SqlParameter("vCATEGORIA", OracleTypes.VARCHAR),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCATEGORIA", categoria);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO DATOS DE VISA.");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerParametrosGC(int pageNumber, int pageSize, String correo) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_UTIL).withProcedureName("PRC_OBTENER_PARAMETROS_GC")
					.declareParameters(new SqlParameter("pageNumber", OracleTypes.NUMBER),
							new SqlParameter("pageSize", OracleTypes.NUMBER),
							new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("TOTAL", OracleTypes.NUMBER),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("pageNumber", pageNumber)
					.addValue("pageSize", pageSize).addValue("vCORREO", correo);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO PARAMETROS GC");
		}
		return out;
	}

	@Override
	public Map<String, Object> insertarParametrosGC(ParametroGC parameter) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_UTIL).withProcedureName("PRC_INSERTAR_PARAMETROS_GC")
					.declareParameters(new SqlParameter("vCLASE", OracleTypes.VARCHAR),
							new SqlParameter("vCATEGORIA", OracleTypes.VARCHAR),
							new SqlParameter("vVALOR", OracleTypes.VARCHAR),
							new SqlParameter("vMODULO", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCLASE", parameter.getClase())
					.addValue("vCATEGORIA", parameter.getCategoria()).addValue("vVALOR", parameter.getValor())
					.addValue("vMODULO", parameter.getModulo());
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR INSERTANDO PARAMETROS GC");
		}
		return out;
	}

	@Override
	public Map<String, Object> editarParametrosGC(ParametroGC parameter) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_UTIL).withProcedureName("PRC_EDITAR_PARAMETROS_GC")
					.declareParameters(new SqlParameter("vCLASE", OracleTypes.VARCHAR),
							new SqlParameter("vCATEGORIA", OracleTypes.VARCHAR),
							new SqlParameter("vVALOR", OracleTypes.VARCHAR),
							new SqlParameter("vFLAG", OracleTypes.CHAR),
							new SqlParameter("vMODULO", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCLASE", parameter.getClase())
					.addValue("vCATEGORIA", parameter.getCategoria()).addValue("vVALOR", parameter.getValor())
					.addValue("vFLAG", parameter.isFlag() ? 1 : 0).addValue("vMODULO", parameter.getModulo());
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EDITANDO PARAMETROS GC");
		}
		return out;
	}
	
	/*adecuacion proceso pago*/
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoTarjeta> obtenerDatosTarjeta(Integer idTarjeta, String descripcion, Integer codigoAgencia) throws GmdException {
		List<TipoTarjeta> lstRetorno = null;
		try {
			SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
			caller.withCatalogName(ConstantesDAO.PKG_UTIL).withProcedureName(ConstantesDAO.PRC_OBTENER_DATOS_TARJETA)
					.declareParameters(
							new SqlParameter(ConstantesDAO.PAR_N_ID_TARJETA, Types.DECIMAL),
							new SqlParameter(ConstantesDAO.PAR_V_DESCRIPCION, Types.VARCHAR),
							new SqlParameter(ConstantesDAO.PAR_N_COD_AGENCIA, Types.DECIMAL),
							new SqlOutParameter(ConstantesDAO.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<TipoTarjeta>() {
								@Override
								public TipoTarjeta mapRow(ResultSet rs, int rowNum) throws SQLException {
									TipoTarjeta record = new TipoTarjeta();
									record.setId_tarjeta(rs.getInt(1));
									record.setDescripcion(rs.getString(2));
									record.setCodigoAgencia(rs.getInt(3));
									return record;										
								}
							}))
					.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ConstantesDAO.PAR_N_ID_TARJETA, idTarjeta);
			params.addValue(ConstantesDAO.PAR_V_DESCRIPCION, descripcion);
			params.addValue(ConstantesDAO.PAR_N_COD_AGENCIA, codigoAgencia);
			Map<String, Object> results = caller.execute(params);		
			lstRetorno = (List<TipoTarjeta>) results.get(ConstantesDAO.PAR_OUT_CURSOR);
		} catch (Exception exception) {
			System.out.println(exception + "ERROR OBTENIENDO DATOS DE TARJETA DE BASE DE DATOS");
			throw new GmdException(exception);
		}		
		return lstRetorno;
	}
	
	/*generacion de numero de liquidacion por sequence*/
	@SuppressWarnings("unchecked")
	@Override
	public Integer obtenerNumLiquidacionSeq() throws GmdException {
		Integer numLiquidacion = null;
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();	
			
			paramsOutput = new ArrayList<SqlOutParameter>();
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.PAR_N_NUM_LIQUIDACION, OracleTypes.INTEGER));
			
			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_UTIL+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_GET_N_NUM_LIQUIDACION, 
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();					
			// Ejecutamos el store procedure
			Map<String, Integer> outputs = (Map<String, Integer>) this.execSp.executeSp(inputs);
			numLiquidacion = Integer.parseInt(""+outputs.get(ConstantesDAO.PAR_N_NUM_LIQUIDACION));
		} catch (Exception exception) {
			System.out.println(exception + "ERROR OBTENIENDO EL NUMERO DE LIQUIDACION DE BASE DE DATOS");
			throw new GmdException(exception);
		}		
		return numLiquidacion;
	}
	
	/*actualizacion mastercard*/
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> obtenerParamCategoriaV2(String categoria) throws GmdException {
		Map<String, Object> out = new HashMap<String, Object>();
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_CATEGORIA, OracleTypes.VARCHAR));		
			
			paramsOutput = new ArrayList<SqlOutParameter>();
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.PAR_IO_CURSOR, OracleTypes.CURSOR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, OracleTypes.VARCHAR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, OracleTypes.INTEGER));
			
			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_UTIL+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_OBTENER_PARAM_CATEGORIA, 
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_V_CATEGORIA, categoria);
					
			// Ejecutamos el store procedure
			out = (Map<String, Object>) this.execSp.executeSp(inputs);
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR PROCEDIMIENTO VALIDA VERSION IOS");
			throw new GmdException(excepcion);
		}
		return out;
	 }

}