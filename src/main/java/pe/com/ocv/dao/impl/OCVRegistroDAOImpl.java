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
import pe.com.ocv.dao.IOCVRegistroDAO;
import pe.com.ocv.model.OCVRegistro;
import pe.com.ocv.model.Usuario;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.ConstantesDAO;
import pe.com.ocv.util.DatasourceUtil;
import pe.com.ocv.util.ExecuteProcedure;

@Repository
public class OCVRegistroDAOImpl implements IOCVRegistroDAO {
	
	/*the environment*/
	@Autowired
	private Environment environment;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	/*the executor*/
	private ExecuteProcedure execSp;

	@Override
	public Map<String, Object> insertaRegistro(OCVRegistro OCVRegistro, boolean migracion) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_INS_USUA_CLTE")
					.declareParameters(new SqlParameter("vNIS", OracleTypes.NUMBER),
							new SqlParameter("vREF_COBRO", OracleTypes.VARCHAR),
							new SqlParameter("vTIP_DOC", OracleTypes.NUMBER),
							new SqlParameter("vNUM_DOC", OracleTypes.VARCHAR),
							new SqlParameter("vAPELLIDO1", OracleTypes.VARCHAR),
							new SqlParameter("vAPELLIDO2", OracleTypes.VARCHAR),
							new SqlParameter("vNOMBRES", OracleTypes.VARCHAR),
							new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlParameter("vTELEFONO1", OracleTypes.VARCHAR),
							new SqlParameter("vCLAVE", OracleTypes.VARCHAR),
							new SqlParameter("vACEPTATERMINOS", OracleTypes.CHAR),
							new SqlParameter("vACEPTANOTIFICACION", OracleTypes.CHAR),
							new SqlParameter("vACEPTACORREO", OracleTypes.CHAR),
							/*add cod verify*/
							new SqlParameter(ConstantesDAO.PAR_N_COD_VERIFY, OracleTypes.DECIMAL),
							/**/
							new SqlParameter("vFLAG_MIGRAR", OracleTypes.NUMBER),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vNIS", OCVRegistro.getNis_rad())
					.addValue("vREF_COBRO", OCVRegistro.getRef_cobro()).addValue("vTIP_DOC", OCVRegistro.getTipo_docu())
					.addValue("vNUM_DOC", OCVRegistro.getNro_doc()).addValue("vAPELLIDO1", OCVRegistro.getApellido1())
					.addValue("vAPELLIDO2", OCVRegistro.getApellido2()).addValue("vNOMBRES", OCVRegistro.getNombres())
					.addValue("vCORREO", OCVRegistro.getCorreo()).addValue("vTELEFONO1", OCVRegistro.getTelefono1())
					.addValue("vCLAVE", OCVRegistro.getClave())
					.addValue("vACEPTATERMINOS", OCVRegistro.getAcepta_terminos())
					.addValue("vACEPTANOTIFICACION", OCVRegistro.getAcepta_noti())
					.addValue("vACEPTACORREO", OCVRegistro.getAcepta_correo())
					/*add add verify*/
					.addValue(ConstantesDAO.PAR_N_COD_VERIFY, OCVRegistro.getCodVerify())
					/**/
					.addValue("vFLAG_MIGRAR", migracion ? 1 : 0);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO REGISTRO DE USUARIO");
		}
		return out;
	}

	@Override
	public Map<String, Object> actualizaRegistro(OCVRegistro OCVRegistro) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_ACT_USUA_CLTE")
					.declareParameters(new SqlParameter("vID_CLIENTE", OracleTypes.NUMBER),
							new SqlParameter("vTIPO_SOL", OracleTypes.NUMBER),
							new SqlParameter("vTIP_DOC", OracleTypes.NUMBER),
							new SqlParameter("vNUM_DOC", OracleTypes.VARCHAR),
							new SqlParameter("vAPELLIDO1", OracleTypes.VARCHAR),
							new SqlParameter("vAPELLIDO2", OracleTypes.VARCHAR),
							new SqlParameter("vNOMBRES", OracleTypes.VARCHAR),
							new SqlParameter("vSEXO", OracleTypes.VARCHAR),
							new SqlParameter("vDISTRITO", OracleTypes.VARCHAR),
							new SqlParameter("vDIRECCION", OracleTypes.VARCHAR),
							new SqlParameter("vFECHA_NAC", OracleTypes.VARCHAR),
							new SqlParameter("vTELEFONO1", OracleTypes.VARCHAR),
							new SqlParameter("vTELEFONO2", OracleTypes.VARCHAR),
							new SqlParameter("vACEPTANOTIFICACION", OracleTypes.CHAR),
							new SqlParameter("vACEPTACORREO", OracleTypes.CHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource()
					.addValue("vID_CLIENTE", OCVRegistro.getId_cliente())
					.addValue("vTIPO_SOL", OCVRegistro.getTipo_soli()).addValue("vTIP_DOC", OCVRegistro.getTipo_docu())
					.addValue("vNUM_DOC", OCVRegistro.getNro_doc()).addValue("vAPELLIDO1", OCVRegistro.getApellido1())
					.addValue("vAPELLIDO2", OCVRegistro.getApellido2()).addValue("vNOMBRES", OCVRegistro.getNombres())
					.addValue("vSEXO", OCVRegistro.getSexo()).addValue("vDISTRITO", OCVRegistro.getDistrito())
					.addValue("vDIRECCION", OCVRegistro.getDireccion())
					.addValue("vFECHA_NAC", OCVRegistro.getFecha_nac())
					.addValue("vTELEFONO1", OCVRegistro.getTelefono1())
					.addValue("vTELEFONO2", OCVRegistro.getTelefono2())
					.addValue("vACEPTANOTIFICACION", OCVRegistro.getAcepta_noti())
					.addValue("vACEPTACORREO", OCVRegistro.getAcepta_correo());
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO REGISTRO DE USUARIO");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerDatosCliente(int id_cliente) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_OBT_DATOS_CLTE")
					.declareParameters(new SqlParameter("vID_CLIENTE", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vID_CLIENTE", id_cliente);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO LA VALIDACION DEL NIS");
		}
		return out;
	}

	@Override
	public Map<String, Object> validaNIS(int nis) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_VALIDA_NIS")
					.declareParameters(new SqlParameter("vNIS", OracleTypes.NUMBER),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vNIS", nis);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO LA VALIDACION DEL NIS");
		}
		return out;
	}

	@Override
	public Map<String, Object> validaCorreo(String correo) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_VALIDA_CORREO")
					.declareParameters(new SqlParameter("vCORREO", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCORREO", correo);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO LA VALIDACION DEL CORREO");
		}
		return out;
	}

	@Override
	public Map<String, Object> validaRefCobro(int nis, String refCobro) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_VALIDA_REF_COBRO")
					.declareParameters(new SqlParameter("vNIS", OracleTypes.NUMBER),
							new SqlParameter("vREF_COBRO", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vNIS", nis).addValue("vREF_COBRO",
					refCobro);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO LA VALIDACION DE LAS REFERENCIAS DE COBRO");
		}
		return out;
	}

	@Override
	public Map<String, Object> validaTipoNroDoc(int tipoDoc, String numDoc, int nisRad) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_VALIDA_TIPO_NRO_DOC")
					.declareParameters(new SqlParameter("vTIPO_DOC", OracleTypes.NUMBER),
							new SqlParameter("vNUM_DOC", OracleTypes.VARCHAR),
							new SqlParameter("vNIS_RAD", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vTIPO_DOC", tipoDoc)
					.addValue("vNUM_DOC", numDoc)
					.addValue("vNIS_RAD", nisRad);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO EL ENVIO DE CORREO.");
		}
		return out;
	}

	@Override
	public Map<String, Object> validarToken(String token, char flag_act) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_VALIDA_TOKEN")
					.declareParameters(new SqlParameter("vTOKEN", OracleTypes.VARCHAR),
							new SqlParameter("vFLAG_ACT", OracleTypes.VARCHAR),
							new SqlOutParameter("oID_CLIENTE", OracleTypes.NUMBER),
							new SqlOutParameter("oCORREO", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vTOKEN", token).addValue("vFLAG_ACT",
					flag_act);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO LA VALIDACION DEL TOKEN");
		}
		return out;
	}

	@Override
	public Map<String, Object> actualizarEstadoUsuario(int idCliente, int idEstado) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_ACTUALIZA_ESTADO")
					.declareParameters(new SqlParameter("vID_CLIENTE", OracleTypes.NUMBER),
							new SqlParameter("vID_ESTADO", OracleTypes.NUMBER),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vID_CLIENTE", idCliente)
					.addValue("vID_ESTADO", idEstado);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO ACTUALIZACION DE ESTADO DE USUARIO");
		}
		return out;
	}
	
	/*obtener datos cliente por correo*/
	@SuppressWarnings("unchecked")
	@Override
	public Usuario obtenerDatosUsuario(Integer idCliente, String correo) throws Exception {
		Usuario usuario = new Usuario();
		try {
			SimpleJdbcCall caller = new SimpleJdbcCall(jdbc.getDataSource());
			caller.withCatalogName(ConstantesDAO.PKG_REGISTRO).withProcedureName(ConstantesDAO.PRC_GET_CLIENTE)
					.declareParameters(	
							new SqlParameter(ConstantesDAO.PAR_V_ID_CLIENTE, Types.VARCHAR),
							new SqlParameter(ConstantesDAO.PAR_V_CORREO, Types.VARCHAR),
							new SqlOutParameter(
									ConstantesDAO.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<Usuario>() {
									@Override
									public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
										Usuario record = new Usuario();
										record.setIdCliente(rs.getInt(1));
										record.setNumSuministro(rs.getLong(2));
										record.setRefCobro(rs.getString(3));
										record.setTipoDocumento(rs.getInt(4));
										record.setNumDocumento(rs.getString(5));
										record.setApePaterno(rs.getString(6));
										record.setApeMaterno(rs.getString(7));
										record.setNombre(rs.getString(8));
										record.setCorreo(rs.getString(9));
										record.setToken(rs.getString(10));
										record.setEstado(rs.getInt(11));
										record.setCodVerify(rs.getInt(12));
										return record;										
									}
								}))
					.withSchemaName(environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA));
		
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ConstantesDAO.PAR_V_ID_CLIENTE, idCliente);
			params.addValue(ConstantesDAO.PAR_V_CORREO, correo);
			Map<String, Object> results = caller.execute(params);		
			usuario = ((List<Usuario>) results.get(ConstantesDAO.PAR_OUT_CURSOR)).get(0);
		}catch(Exception exception) {
			throw new Exception("Error obteniendo los datos del usuario a nivel de base de datos");
		}
		return usuario;
	}
	/**/
	
	/*actualizar codigo de verificacion de usuario*/
	@Override
	public void actualizarCodigoVerificacion(String correo, Integer codeVerify) throws Exception {
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		try {
			paramsInput = new ArrayList<SqlParameter>();
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_V_CORREO, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantesDAO.PAR_N_COD_VERIFY, OracleTypes.NUMBER));	
			
			paramsOutput = new ArrayList<SqlOutParameter>();
			
			this.execSp = new ExecuteProcedure(jdbc.getDataSource(), ConstantesDAO.PKG_REGISTRO+ConstantesDAO.P_SEPARADOR+ConstantesDAO.PRC_UPDATE_CODE_VERIFY, 
					environment.getRequiredProperty(ConstantesDAO.ORACLE_PROCEDURES_SCHEMA), paramsInput, paramsOutput);
			inputs = new HashMap<String, Object>();			
			inputs.put(ConstantesDAO.PAR_V_CORREO, correo);
			inputs.put(ConstantesDAO.PAR_N_COD_VERIFY, codeVerify);
			// Ejecutamos el store procedure
			this.execSp.executeSp(inputs);
		} catch (Exception excepcion) {
			excepcion.printStackTrace();
			throw new Exception("Error actualizando el codigo de verificacion del usuario.");
		}
	}
	
	@Override
	public Map<String, Object> obtenerDatosClienteAntiFraude(String correo_cliente) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_REGISTRO).withProcedureName("PRC_DATOS_CLTE_ANTI_FRAUDE")
					.declareParameters(new SqlParameter("vCORREO_CLT", OracleTypes.VARCHAR),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCORREO_CLT", correo_cliente);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR AL OBTENER LOS DATOS DEL CLIENTE ANTI FRAUDE");
		}
		return out;
	}
}