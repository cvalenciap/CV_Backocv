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
import pe.com.ocv.dao.IOCVNotificacionDAO;
import pe.com.ocv.model.Notificacion;
import pe.com.ocv.model.PagoEjecutado;
import pe.com.ocv.util.ConstantesDAO;
import pe.com.ocv.util.ExecuteProcedure;

@Repository
public class OCVNotificacionDAOImpl implements IOCVNotificacionDAO {
	
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
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> enviarCorreoxNotificacion(Integer idNotificacion, PagoEjecutado pago) throws Exception {
		Map<String, Object> out = new HashMap<String, Object>();
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_ID_NOTIFICACION, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_CORREO, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_NUM_OPERACION, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_NUM_TARJETA, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_NIS_RAD, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_FECHA, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_HORA, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_MONTO, OracleTypes.DECIMAL));			
			
			paramsOutput = new ArrayList<SqlOutParameter>();
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, OracleTypes.VARCHAR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, OracleTypes.NUMBER));
			
			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_NOTIFICACIONES+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_ENVIAR_CORREO_NOTIFICACION, 
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_N_ID_NOTIFICACION, idNotificacion);
			inputs.put(ConstantesDAO.PAR_V_CORREO, pago.getCorreo());
			inputs.put(ConstantesDAO.PAR_V_NUM_OPERACION, pago.getNumOperacion());
			inputs.put(ConstantesDAO.PAR_V_NUM_TARJETA, pago.getNumTarjeta());
			inputs.put(ConstantesDAO.PAR_N_NIS_RAD, pago.getNisRad());
			inputs.put(ConstantesDAO.PAR_V_FECHA, pago.getFecha());
			inputs.put(ConstantesDAO.PAR_V_HORA, pago.getHora());
			inputs.put(ConstantesDAO.PAR_N_MONTO, pago.getMonto());
					
			// Ejecutamos el store procedure
			out = (Map<String, Object>) this.execSp.executeSp(inputs);
//			out.put("nRESP_SP", outputs.get(ConstantesDAO.COD_RESPUESTA));
//			out.put("cRESP_SP", outputs.get(ConstantesDAO.MENSAJE_RESPUESTA));
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR ENVIANDO NOTIFICACION");
		}
		return out;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Notificacion obtenerNotificacion(Integer idNotificacion) throws Exception {
		List<Notificacion> lstRetorno = null;
		SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
		caller.withCatalogName(ConstantesDAO.PKG_NOTIFICACIONES).withProcedureName("PRC_OBTENER_NOTIFICACION")
				.declareParameters(
						new SqlParameter("PAR_N_ID_NOTIFICACION", Types.DECIMAL),
						new SqlOutParameter(ConstantesDAO.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<Notificacion>() {
							@Override
							public Notificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
								Notificacion record = new Notificacion();
								record.setIdNotificacion(rs.getInt(1));
								record.setDesNotificacion(rs.getString(2));
								record.setStRegi(rs.getString(3));
								record.setFeUsuaCrea(rs.getDate(4));
								record.setFeUsuaCrea(rs.getDate(5));
								record.setHtmlMensaje(rs.getString(6));
								return record;										
							}
						}))
				.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("PAR_N_ID_NOTIFICACION", idNotificacion);
		Map<String, Object> results = caller.execute(params);		
		lstRetorno = (List<Notificacion>) results.get(ConstantesDAO.PAR_OUT_CURSOR);
		Notificacion notificacionRetorno = lstRetorno.get(0);
		return notificacionRetorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> enviarCorreoxNotificacionGeneral(Integer idNotificacion, String correo, String var1, String var2, String var3, String var4, String var5, String var6, String var7) throws Exception {
		Map<String, Object> out = new HashMap<String, Object>();
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_ID_NOTIFICACION, OracleTypes.DECIMAL));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_CORREO, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.VAR1, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.VAR2, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.VAR3, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.VAR4, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.VAR5, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.VAR6, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.VAR7, OracleTypes.VARCHAR));
			
			paramsOutput = new ArrayList<SqlOutParameter>();
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.MENSAJE_RESPUESTA, OracleTypes.VARCHAR));
			paramsOutput.add(new SqlOutParameter(ConstantesDAO.COD_RESPUESTA, OracleTypes.NUMBER));
			
			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_NOTIFICACIONES+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_ENVIAR_CORREO_GENER, 
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantesDAO.PAR_N_ID_NOTIFICACION, idNotificacion);
			inputs.put(ConstantesDAO.PAR_V_CORREO, correo);
			inputs.put(ConstantesDAO.VAR1, var1);
			inputs.put(ConstantesDAO.VAR2, var2);
			inputs.put(ConstantesDAO.VAR3, var3);
			inputs.put(ConstantesDAO.VAR4, var4);
			inputs.put(ConstantesDAO.VAR5, var5);
			inputs.put(ConstantesDAO.VAR6, var6);
			inputs.put(ConstantesDAO.VAR7, var7);
			
			// Ejecutamos el store procedure
			out = (Map<String, Object>) this.execSp.executeSp(inputs);
//			out.put("nRESP_SP", outputs.get(ConstantesDAO.COD_RESPUESTA));
//			out.put("cRESP_SP", outputs.get(ConstantesDAO.MENSAJE_RESPUESTA));
		} catch (Exception excepcion) {
			System.out.println(excepcion + "ERROR ENVIANDO NOTIFICACION");
		}
		return out;		
	}
}
