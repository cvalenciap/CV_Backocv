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
import pe.com.ocv.dao.IOCVIncidenciaDAO;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.DatasourceUtil;

@Repository
public class OCVIncidenciasDAOImpl implements IOCVIncidenciaDAO {
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public Map<String, Object> listaMunicipiosAfectados() {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_INCIDENCIA).withProcedureName("PRC_LIST_MUNICIPIOS_AFECTADOS")
					.declareParameters(new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			out = jdbcCall.execute();
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO MUNICIPIOS AFECTADOS.");
		}
		return out;
	}

	@Override
	public Map<String, Object> listaMunicipiosSuministro(int nisRad) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_INCIDENCIA).withProcedureName("PRC_LIST_MUNICIPIOS_SUMINISTRO")
					.declareParameters(new SqlParameter("vNIS_RAD", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vNIS_RAD", nisRad);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO MUNICIPIOS AFECTADOS.");
		}
		return out;
	}

	@Override
	public Map<String, Object> listaIncidenciasSuministro(int codMunic, int nisRad) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_INCIDENCIA).withProcedureName("PRC_LIST_INCIDENCIAS_NISRAD")
					.declareParameters(new SqlParameter("vNIS_RAD", OracleTypes.NUMBER),
							new SqlParameter("vCOD_MUNIC", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vNIS_RAD", nisRad).addValue("vCOD_MUNIC", codMunic);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO INCIDENCIAS POR SUMINISTRO.");
		}
		return out;
	}

	@Override
	public Map<String, Object> listaIncidenciasMunicipio(int codMunic) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_INCIDENCIA).withProcedureName("PRC_LIST_INCIDENCIAS_MUNICIPIO")
					.declareParameters(new SqlParameter("vCOD_MUNIC", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCOD_MUNIC", codMunic);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO INCIDENCIAS POR MUNICIPIO.");
		}
		return out;
	}

	@Override
	public Map<String, Object> listaAreasAfectadas(int codIncidencia) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_INCIDENCIA).withProcedureName("PRC_LIST_AREAS_AFECTADAS")
					.declareParameters(new SqlParameter("vCOD_INCIDENCIA", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCOD_INCIDENCIA", codIncidencia);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO AREAS AFECTADAS.");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerPredioAfectado(int nis) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_INCIDENCIA).withProcedureName("PRC_PREDIO_AFECTADO")
					.declareParameters(new SqlParameter("vNIS_RAD", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vNIS_RAD", nis);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO PREDIO AFECTADO.");
		}
		return out;
	}
	
	@Override
	public Map<String, Object> obtenerCoordenadas(int codMunic) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_INCIDENCIA).withProcedureName("PRC_OBTENER_COORDENADAS")
					.declareParameters(new SqlParameter("vCOD_INCIDENCIA", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCOD_MUNIC", codMunic);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO LISTA DE COORDENADAS.");
		}
		return out;
	}
	
	@Override
	public Map<String, Object> listarIncidencias() {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_INCIDENCIA).withProcedureName("PRC_INCIDENCIAS_ALL")
					.declareParameters(new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			out = jdbcCall.execute();
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO TODAS LAS INCIDENCIAS");
		}
		return out;
	}
}