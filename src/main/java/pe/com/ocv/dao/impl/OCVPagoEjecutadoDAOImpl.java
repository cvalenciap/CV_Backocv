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
import pe.com.ocv.dao.IOCVPagoEjecutadoDAO;
import pe.com.ocv.model.PagoEjecutado;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.ConstantesDAO;
import pe.com.ocv.util.DatasourceUtil;
import pe.com.ocv.util.ExecuteProcedure;

@Repository
public class OCVPagoEjecutadoDAOImpl implements IOCVPagoEjecutadoDAO {

	/* the environment */
	@Autowired
	private Environment environment;

	/* the template */
	@Autowired
	private JdbcTemplate jdbc;

	/* the executor */
	private ExecuteProcedure execSp;

	@Override
	public Map<String, Object> insertarPagoEjecutado(PagoEjecutado pago) throws Exception {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_PAGO_EJECUTADO).withProcedureName("PRC_INSERTAR_PAGO_EJECUTADO")
					.declareParameters(new SqlParameter("vTRX_ID", OracleTypes.VARCHAR),
							new SqlParameter("vERROR_CODE", OracleTypes.VARCHAR),
							new SqlParameter("vERROR_MSG", OracleTypes.VARCHAR),
							new SqlParameter("vFECHA", OracleTypes.VARCHAR),
							new SqlParameter("vHORA", OracleTypes.VARCHAR),
							new SqlParameter("vDESCRIPCION", OracleTypes.VARCHAR),
							new SqlParameter("vNUM_OPERACION", OracleTypes.VARCHAR),
							new SqlParameter("vNUM_TARJETA", OracleTypes.VARCHAR),
							new SqlParameter("vNUM_LIQUIDACION", OracleTypes.NUMBER),
							new SqlParameter("vMONTO", OracleTypes.NUMBER),
							new SqlParameter("vNIS_RAD", OracleTypes.NUMBER),
							new SqlParameter("vESTADO", OracleTypes.VARCHAR),
							new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vTRX_ID", pago.getTrxId())
					.addValue("vERROR_CODE", pago.getErrorCode()).addValue("vERROR_MSG", pago.getErrorMsg())
					.addValue("vFECHA", pago.getFecha()).addValue("vHORA", pago.getHora())
					.addValue("vDESCRIPCION", pago.getDescripcion()).addValue("vNUM_OPERACION", pago.getNumOperacion())
					.addValue("vNUM_TARJETA", pago.getNumTarjeta())
					.addValue("vNUM_LIQUIDACION", pago.getNumLiquidacion()).addValue("vMONTO", pago.getMonto())
					.addValue("vNIS_RAD", pago.getNisRad()).addValue("vESTADO", pago.getEstado())
					.addValue("vCORREO", pago.getCorreo());
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR INSERTANDO PAGO EJECUTADO");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerResultadoPago(String trxID) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_PAGO_EJECUTADO).withProcedureName("PRC_OBTENER_RESULTADO_PAGO")
					.declareParameters(new SqlParameter("vTRX_ID", OracleTypes.VARCHAR),
							new SqlOutParameter("oCOD_VISA", OracleTypes.NUMBER),
							new SqlOutParameter("oCOD_ENCHUFATE", OracleTypes.NUMBER),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vTRX_ID", trxID);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO EL RESULTADO DEL PAGO");
		}
		return out;
	}

	@Override
	public Map<String, Object> enviarCorreoPago(String fecha, String hora, String numOperacion, String numTarjeta,
			double monto, int nisRad, String correo) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_PAGO_EJECUTADO).withProcedureName("PRC_ENVIAR_CORREO_PAGO")
					.declareParameters(new SqlParameter("vFECHA", OracleTypes.VARCHAR),
							new SqlParameter("vHORA", OracleTypes.VARCHAR),
							new SqlParameter("vNUM_OPERACION", OracleTypes.VARCHAR),
							new SqlParameter("vNUM_TARJETA", OracleTypes.VARCHAR),
							new SqlParameter("vMONTO", OracleTypes.NUMBER),
							new SqlParameter("vNIS_RAD", OracleTypes.NUMBER),
							new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vFECHA", fecha)
					.addValue("vHORA", hora).addValue("vNUM_OPERACION", numOperacion)
					.addValue("vNUM_TARJETA", numTarjeta).addValue("vMONTO", monto).addValue("vNIS_RAD", nisRad)
					.addValue("vCORREO", correo);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR ENVIANDO CORREO CONFIRMACIÓN PAGO");
		}
		return out;
	}

//	@Override
//	public Map<String, Object> insertarPagoEjecutadoSinCorreo(PagoEjecutado pago) throws Exception {
//		Map<String, Object> out = null;
//		try {
//			SimpleJdbcCall jdbcCall;
//			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
//					.withCatalogName(Constantes.PKG_PAGO_EJECUTADO).withProcedureName("PRC_INSERTAR_PAGO_EJECUTADO_V2")
//					.declareParameters(new SqlParameter("vTRX_ID", OracleTypes.VARCHAR),
//							new SqlParameter("vERROR_CODE", OracleTypes.VARCHAR),
//							new SqlParameter("vERROR_MSG", OracleTypes.VARCHAR),
//							new SqlParameter("vFECHA", OracleTypes.VARCHAR),
//							new SqlParameter("vHORA", OracleTypes.VARCHAR),
//							new SqlParameter("vDESCRIPCION", OracleTypes.VARCHAR),
//							new SqlParameter("vNUM_OPERACION", OracleTypes.VARCHAR),
//							new SqlParameter("vNUM_TARJETA", OracleTypes.VARCHAR),
//							new SqlParameter("vNUM_LIQUIDACION", OracleTypes.NUMBER),
//							new SqlParameter("vMONTO", OracleTypes.NUMBER),
//							new SqlParameter("vNIS_RAD", OracleTypes.NUMBER),
//							new SqlParameter("vESTADO", OracleTypes.VARCHAR),
//							new SqlParameter("vSIMBOLO_VAR", OracleTypes.NUMBER),
//							new SqlParameter("vFECHA_EMISION", OracleTypes.DATE),
//							new SqlParameter("vFECHA_VENCIMIENTO", OracleTypes.DATE),
//							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
//							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
//			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vTRX_ID", pago.getTrxId())
//					.addValue("vERROR_CODE", pago.getErrorCode()).addValue("vERROR_MSG", pago.getErrorMsg())
//					.addValue("vFECHA", pago.getFecha()).addValue("vHORA", pago.getHora())
//					.addValue("vDESCRIPCION", pago.getDescripcion()).addValue("vNUM_OPERACION", pago.getNumOperacion())
//					.addValue("vNUM_TARJETA", pago.getNumTarjeta())
//					.addValue("vNUM_LIQUIDACION", pago.getNumLiquidacion()).addValue("vMONTO", pago.getMonto())
//					.addValue("vNIS_RAD", pago.getNisRad()).addValue("vESTADO", pago.getEstado())
//					.addValue("vSIMBOLO_VAR", pago.getSimboloVar()).addValue("vFECHA_EMISION", pago.getFechaEmision())
//					.addValue("vFECHA_VENCIMIENTO", pago.getFechaVencimiento());
//			out = jdbcCall.execute(paraMap);
////			close connection
//			DatasourceUtil.closeConnection(this.jdbc);
//		} catch (Exception e) {
//			System.out.println(e + "ERROR INSERTANDO PAGO EJECUTADO");
//		}
//		return out;
//	}
	/* adecuacion pago ejecutado */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertarPagoEjecutadoSinCorreo(PagoEjecutado pago) throws GmdException {
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_TRX_ID, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_ERROR_CODE, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_ERROR_MSG, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_FECHA, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_HORA, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_DESCRIPCION, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_NUM_OPERACION, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_NUM_TARJETA, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_NUM_LIQUIDACION, OracleTypes.NUMBER));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_MONTO, OracleTypes.NUMBER));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_NIS_RAD, OracleTypes.NUMBER));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_ESTADO, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_SIMBOLO_VAR, OracleTypes.NUMBER));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_D_FECHA_EMISION, OracleTypes.DATE));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_D_FECHA_VENCIMIENTO, OracleTypes.DATE));

			paramsOutput = new ArrayList<SqlOutParameter>();

			this.execSp = new ExecuteProcedure(jdbc.getDataSource(),
					ConstantesDAO.PKG_PAGO_EJECUTADO + ConstantesDAO.P_SEPARADOR
							+ ConstantesDAO.PRC_INSERTAR_PAGO_EJECUTADO_V2,
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_V_TRX_ID, pago.getTrxId());
			inputs.put(ConstantesDAO.PAR_V_ERROR_CODE, pago.getErrorCode());
			inputs.put(ConstantesDAO.PAR_V_ERROR_MSG, pago.getErrorMsg());
			inputs.put(ConstantesDAO.PAR_V_FECHA, pago.getFecha());
			inputs.put(ConstantesDAO.PAR_V_HORA, pago.getHora());
			inputs.put(ConstantesDAO.PAR_V_DESCRIPCION, pago.getDescripcion());
			inputs.put(ConstantesDAO.PAR_V_NUM_OPERACION, pago.getNumOperacion());
			inputs.put(ConstantesDAO.PAR_V_NUM_TARJETA, pago.getNumTarjeta());
			inputs.put(ConstantesDAO.PAR_V_NUM_LIQUIDACION, pago.getNumLiquidacion());
			inputs.put(ConstantesDAO.PAR_N_MONTO, pago.getMonto());
			inputs.put(ConstantesDAO.PAR_N_NIS_RAD, pago.getNisRad());
			inputs.put(ConstantesDAO.PAR_V_ESTADO, pago.getEstado());
			inputs.put(ConstantesDAO.PAR_N_SIMBOLO_VAR, pago.getSimboloVar());
			inputs.put(ConstantesDAO.PAR_D_FECHA_EMISION, pago.getFechaEmision());
			inputs.put(ConstantesDAO.PAR_D_FECHA_VENCIMIENTO, pago.getFechaVencimiento());

			// Ejecutamos el store procedure
			this.execSp.executeSp(inputs);
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR INSERTANDO PAGO EJECUTADO");
			throw new GmdException(excepcion);
		}
	}

//	insercion de registro previo
//	@SuppressWarnings({ "unchecked", "null" })
//	@Override
//	@Transactional(rollbackFor=Exception.class)
//	public Map<String, Object> insertarRegistroPrevio(PagoEjecutado pago) throws Exception {
//		Map<String, Object> out = new HashMap<String, Object>();
//		List<SqlParameter> paramsInput = null;
//		List<SqlOutParameter> paramsOutput = null;
//		Map<String, Object> inputs = null;
//		try {
//			paramsInput = new ArrayList<SqlParameter>();
//			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_NUM_LIQUIDACION, OracleTypes.DECIMAL));
//			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_SIMBOLO_VAR, OracleTypes.DECIMAL));
//			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_D_FE_EMISION, OracleTypes.DATE));
//			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_D_FE_VENCIMIENTO, OracleTypes.DATE));
//			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_MONTO, OracleTypes.DECIMAL));			
//			
//			paramsOutput = new ArrayList<SqlOutParameter>();
//			paramsOutput.add(new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, OracleTypes.VARCHAR));
//			paramsOutput.add(new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, OracleTypes.NUMBER));
//			
//			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_PAGO_EJECUTADO+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_INSERTAR_REGISTRO_PREV, 
//					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
//			inputs = new HashMap<String, Object>();
//			inputs.put(ConstantesDAO.PAR_N_NUM_LIQUIDACION, pago.getNumLiquidacion());
//			inputs.put(ConstantesDAO.PAR_N_SIMBOLO_VAR, pago.getSimboloVar());
//			inputs.put(ConstantesDAO.PAR_D_FE_EMISION, pago.getFechaEmision());
//			inputs.put(ConstantesDAO.PAR_D_FE_VENCIMIENTO, pago.getFechaVencimiento());
//			inputs.put(ConstantesDAO.PAR_N_MONTO, pago.getMonto());
//			
//					
//			// Ejecutamos el store procedure
//			out = (Map<String, Object>) this.execSp.executeSp(inputs);
//		} catch (Exception excepcion) {
//			System.out.println(excepcion + "ERROR INSERTANDO PAGO EJECUTADO");
//		}
//		return out;		
//	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertarRegistroPrevio(PagoEjecutado pago) throws GmdException {
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_NUM_LIQUIDACION, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_SIMBOLO_VAR, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_D_FE_EMISION, OracleTypes.DATE));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_D_FE_VENCIMIENTO, OracleTypes.DATE));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_MONTO, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_ESTADO, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_ID_CANAL, OracleTypes.NUMBER));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_COD_AGENCIA, OracleTypes.NUMBER));

			paramsOutput = new ArrayList<SqlOutParameter>();

			this.execSp = new ExecuteProcedure(jdbc.getDataSource(),
					ConstantesDAO.PKG_PAGO_EJECUTADO + ConstantesDAO.P_SEPARADOR
							+ ConstantesDAO.PRC_INSERTAR_REGISTRO_PREV,
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_N_NUM_LIQUIDACION, pago.getNumLiquidacion());
			inputs.put(ConstantesDAO.PAR_N_SIMBOLO_VAR, pago.getSimboloVar());
			inputs.put(ConstantesDAO.PAR_D_FE_EMISION, pago.getFechaEmision());
			inputs.put(ConstantesDAO.PAR_D_FE_VENCIMIENTO, pago.getFechaVencimiento());
			inputs.put(ConstantesDAO.PAR_N_MONTO, pago.getMonto());
			inputs.put(ConstantesDAO.PAR_V_ESTADO, pago.getEstado());
			inputs.put(ConstantesDAO.PAR_N_ID_CANAL, pago.getCanal());
			inputs.put(ConstantesDAO.PAR_N_COD_AGENCIA, pago.getCodAgencia());

			// Ejecutamos el store procedure
			this.execSp.executeSp(inputs);
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR INSERTANDO PAGO EJECUTADO");
			throw new GmdException(excepcion);
		}
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<PagoEjecutado> obtenerRegistrosPrevios(Long numLiquidacion, Long simboloVar) throws Exception {
//		List<PagoEjecutado> lstRetorno = null;
//		SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
//		caller.withCatalogName(ConstantesDAO.PKG_PAGO_EJECUTADO).withProcedureName(ConstantesDAO.PRC_LISTA_REGISTRO_PREV)
//				.declareParameters(
//						new SqlParameter(ConstantesDAO.PAR_N_NUM_LIQUIDACION, Types.DECIMAL),
//						new SqlParameter(ConstantesDAO.PAR_N_SIMBOLO_VAR, Types.DECIMAL),
//						new SqlOutParameter(ConstantesDAO.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<PagoEjecutado>() {
//							@Override
//							public PagoEjecutado mapRow(ResultSet rs, int rowNum) throws SQLException {
//								PagoEjecutado record = new PagoEjecutado();
//								record.setNumLiquidacion(rs.getLong(1));
//								record.setSimboloVar(rs.getLong(2));
//								record.setFechaEmision(rs.getDate(3));
//								record.setFechaVencimiento(rs.getDate(4));
//								record.setMonto(rs.getDouble(5));
//								return record;										
//							}
//						}))
//				.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue(ConstantesDAO.PAR_N_NUM_LIQUIDACION, numLiquidacion);
//		params.addValue(ConstantesDAO.PAR_N_SIMBOLO_VAR, simboloVar);
//		Map<String, Object> results = caller.execute(params);		
//		lstRetorno = (List<PagoEjecutado>) results.get(ConstantesDAO.PAR_OUT_CURSOR);
//		return lstRetorno;
//	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PagoEjecutado> obtenerRegistrosPrevios(Long numLiquidacion, Long simboloVar) throws GmdException {
		List<PagoEjecutado> lstRetorno = null;
		try {
			SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
			caller.withCatalogName(ConstantesDAO.PKG_PAGO_EJECUTADO)
					.withProcedureName(ConstantesDAO.PRC_LISTA_REGISTRO_PREV)
					.declareParameters(new SqlParameter(ConstantesDAO.PAR_N_NUM_LIQUIDACION, Types.DECIMAL),
							new SqlParameter(ConstantesDAO.PAR_N_SIMBOLO_VAR, Types.DECIMAL), new SqlOutParameter(
									ConstantesDAO.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<PagoEjecutado>() {
										@Override
										public PagoEjecutado mapRow(ResultSet rs, int rowNum) throws SQLException {
											PagoEjecutado record = new PagoEjecutado();
											record.setNumLiquidacion(rs.getLong(1));
											record.setSimboloVar(rs.getLong(2));
											record.setFechaEmision(rs.getDate(3));
											record.setFechaVencimiento(rs.getDate(4));
											record.setMonto(rs.getDouble(5));
											record.setEstado(rs.getString(6));
											record.setCanal(rs.getInt(7));
											record.setCodAgencia(rs.getInt(8));
											return record;
										}
									}))
					.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ConstantesDAO.PAR_N_NUM_LIQUIDACION, numLiquidacion);
			params.addValue(ConstantesDAO.PAR_N_SIMBOLO_VAR, simboloVar);
			Map<String, Object> results = caller.execute(params);
			lstRetorno = (List<PagoEjecutado>) results.get(ConstantesDAO.PAR_OUT_CURSOR);
		} catch (Exception exception) {
			throw new GmdException(exception);
		}
		return lstRetorno;
	}

	/* init intermediate screen */
	@SuppressWarnings("unchecked")
	@Override
	public List<PagoEjecutado> obtenerDatosPago(String trxID) throws GmdException {
		List<PagoEjecutado> listaPago = new ArrayList<PagoEjecutado>();
		try {
			SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
			caller.withCatalogName(ConstantesDAO.PKG_PAGO_EJECUTADO)
					.withProcedureName(ConstantesDAO.PRC_LISTAR_DATOS_PAGO)
					.declareParameters(new SqlParameter(ConstantesDAO.PAR_V_TRX_ID, Types.VARCHAR), new SqlOutParameter(
							ConstantesDAO.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<PagoEjecutado>() {
								@Override
								public PagoEjecutado mapRow(ResultSet rs, int rowNum) throws SQLException {
									PagoEjecutado record = new PagoEjecutado();
									record.setTrxId(rs.getString(1));
									record.setNumOperacion(rs.getString(2));
									record.setNumTarjeta(rs.getString(3));
									record.setFechaHora(rs.getString(4));
									record.setMonto(rs.getDouble(5));
									record.setNumLiquidacion(rs.getLong(6));
									record.setErrorCode(rs.getString(7));
									return record;
								}
							}))
					.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ConstantesDAO.PAR_V_TRX_ID, trxID);
			Map<String, Object> results = caller.execute(params);
			listaPago = (List<PagoEjecutado>) results.get(ConstantesDAO.PAR_OUT_CURSOR);
		} catch (Exception exception) {
			throw new GmdException("Error obteniendo los datos del pago a nivel de base de datos");
		}
		return listaPago;
	}
	/* end intermediate screen */

	/* adecuacion procedimiento pago */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void actualizarRegistroPrevio(PagoEjecutado pago) throws GmdException {
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_NUM_LIQUIDACION, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_SIMBOLO_VAR, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_D_FE_EMISION, OracleTypes.DATE));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_D_FE_VENCIMIENTO, OracleTypes.DATE));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_MONTO, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_ESTADO, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_ID_CANAL, OracleTypes.NUMBER));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_COD_AGENCIA, OracleTypes.NUMBER));

			paramsOutput = new ArrayList<SqlOutParameter>();

			this.execSp = new ExecuteProcedure(jdbc.getDataSource(),
					ConstantesDAO.PKG_PAGO_EJECUTADO + ConstantesDAO.P_SEPARADOR
							+ ConstantesDAO.PRC_ACTUALIZAR_REGISTRO_PREV,
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_N_NUM_LIQUIDACION, pago.getNumLiquidacion());
			inputs.put(ConstantesDAO.PAR_N_SIMBOLO_VAR, pago.getSimboloVar());
			inputs.put(ConstantesDAO.PAR_D_FE_EMISION, pago.getFechaEmision());
			inputs.put(ConstantesDAO.PAR_D_FE_VENCIMIENTO, pago.getFechaVencimiento());
			inputs.put(ConstantesDAO.PAR_N_MONTO, pago.getMonto());
			inputs.put(ConstantesDAO.PAR_V_ESTADO, pago.getEstado());
			inputs.put(ConstantesDAO.PAR_N_ID_CANAL, pago.getCanal());
			inputs.put(ConstantesDAO.PAR_N_COD_AGENCIA, pago.getCodAgencia());

			// Ejecutamos el store procedure
			this.execSp.executeSp(inputs);
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR INSERTANDO PAGO EJECUTADO");
			throw new GmdException(excepcion);
		}
	}

	/* Liquidación Visa */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> insertarRegistroLiquidacionVisa(String nroLiqui, String nisRad, String json)
			throws GmdException {

		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(ConstantesDAO.PKG_AUDITORIA)
					.withProcedureName(ConstantesDAO.PRC_INSERTAR_LIQUI_VISA).declareParameters(
							new SqlParameter(new SqlParameter(ConstantesDAO.PAR_V_NRO_LIQUI, OracleTypes.VARCHAR)),
							new SqlParameter(ConstantesDAO.PAR_N_NIS_RAD, OracleTypes.NUMBER),
							new SqlParameter(ConstantesDAO.PAR_V_TRAMA_JSON, OracleTypes.VARCHAR),
							new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, OracleTypes.VARCHAR),
							new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource()
					.addValue(ConstantesDAO.PAR_V_NRO_LIQUI, nroLiqui).addValue(ConstantesDAO.PAR_N_NIS_RAD, nisRad)
					.addValue(ConstantesDAO.PAR_V_TRAMA_JSON, json);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR INSERTANDO LIQUIDACION VISA");
		}
		return out;
	}

}
