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

import pe.com.ocv.dao.IOCVMaestrasDAO;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.DatasourceUtil;

@Repository
public class OCVMaestrasDAOImpl implements IOCVMaestrasDAO {
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public Map<String, Object> devuelveMaestras(int i) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_MAESTRA).withProcedureName("PRC_CARGA_MAESTRA")
					.declareParameters(new SqlParameter("V_CMAESTRO", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("V_CMAESTRO", i);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR EJECUTANDO PRC_CARGA_MAESTRA");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerUsuariosAntiguos() {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_MAESTRA).withProcedureName("PRC_OBTENER_USUARIOS_ANT")
					.declareParameters(new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			out = jdbcCall.execute();
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO USUARIOS ANTIGUOS");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerDominios() {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_MAESTRA).withProcedureName("PRC_OBTENER_DOMINIOS")
					.declareParameters(new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			out = jdbcCall.execute();
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO DOMINIOS");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerTitulos() {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_MAESTRA).withProcedureName("PRC_OBTENER_TITULO")
					.declareParameters(new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			out = jdbcCall.execute();
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO TITULOS");
		}
		return out;
	}

}