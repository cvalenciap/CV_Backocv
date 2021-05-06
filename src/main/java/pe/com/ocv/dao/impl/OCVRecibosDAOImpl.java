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
import pe.com.ocv.dao.IOCVRecibosDAO;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.DatasourceUtil;

@Repository
public class OCVRecibosDAOImpl implements IOCVRecibosDAO {
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public Map<String, Object> listaRecibosPendientes(int nis, int pageNumber, int pageSize) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_RECIBO_PAGO).withProcedureName("PRC_LIST_RECI_PEND_X_NIS")
					.declareParameters(new SqlParameter("vNIS", OracleTypes.NUMBER),
							new SqlParameter("pageNumber", OracleTypes.NUMBER),
							new SqlParameter("pageSize", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("TOTAL", OracleTypes.NUMBER),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource();
			paraMap.addValue("vNIS", nis, OracleTypes.NUMBER);
			paraMap.addValue("pageNumber", pageNumber, OracleTypes.NUMBER);
			paraMap.addValue("pageSize", pageSize, OracleTypes.NUMBER);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO RECIBOS PENDIENTES.");
		}
		return out;
	}

	@Override
	public Map<String, Object> listaRecibosPagados(int nis, int pageNumber, int pageSize) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall; 
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_RECIBO_PAGO).withProcedureName("PRC_LIST_RECI_PAGD_X_NIS")
					.declareParameters(new SqlParameter("vNIS", OracleTypes.NUMBER),
							new SqlParameter("pageNumber", OracleTypes.NUMBER),
							new SqlParameter("pageSize", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("TOTAL", OracleTypes.NUMBER),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource();
			paraMap.addValue("vNIS", nis, OracleTypes.NUMBER);
			paraMap.addValue("pageNumber", pageNumber, OracleTypes.NUMBER);
			paraMap.addValue("pageSize", pageSize, OracleTypes.NUMBER);
//			verify count connection
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO RECIBOS PAGADOS.");
		}
		return out;
	}

	@Override
	public Map<String, Object> detalleRecibo(String recibo) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_RECIBO_PAGO).withProcedureName("PRC_DETALLE_RECIBO")
					.declareParameters(new SqlParameter("vRECIBO", OracleTypes.VARCHAR),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource();
			paraMap.addValue("vRECIBO", recibo, OracleTypes.VARCHAR);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO EL DETALLE DEL RECIBO.");
		}
		return out;
	}

	@Override
	public Map<String, Object> detallePago(String simbolo_var, String nro_factura) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_RECIBO_PAGO).withProcedureName("PRC_DETALLE_PAGO")
					.declareParameters(new SqlParameter("vSIMBOLO_VAR", OracleTypes.VARCHAR),
							new SqlParameter("vNRO_FACTURA", OracleTypes.VARCHAR),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource();
			paraMap.addValue("vSIMBOLO_VAR", simbolo_var, OracleTypes.VARCHAR);
			paraMap.addValue("vNRO_FACTURA", nro_factura, OracleTypes.VARCHAR);
			System.out.println(paraMap.getValues());
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO EL DETALLE DE PAGOS.");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerDatosRecibo(int secRec, int nisRad, int secNis, String fFact) {
		Map<String, Object> out = null;
		try {
			System.out.println("DAOobtenERdATOSrECIBO"+nisRad);
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_RECIBO_PAGO).withProcedureName("PRC_OBTENER_DATOS_RECIBO")
					.declareParameters(new SqlParameter("vSEC_REC", OracleTypes.NUMBER),
							new SqlParameter("vNIS_RAD", OracleTypes.NUMBER),
							new SqlParameter("vSEC_NIS", OracleTypes.NUMBER),
							new SqlParameter("vF_FACT", OracleTypes.VARCHAR),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource();
			paraMap.addValue("vSEC_REC", secRec, OracleTypes.NUMBER);
			paraMap.addValue("vNIS_RAD", nisRad, OracleTypes.NUMBER);
			paraMap.addValue("vSEC_NIS", secNis, OracleTypes.NUMBER);
			paraMap.addValue("vF_FACT", fFact, OracleTypes.VARCHAR);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO DATOS PARA EL RECIBO.");
		}
		return out;
	}

	@Override
	public Map<String, Object> obtenerParametrosRecibo() {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_RECIBO_PAGO).withProcedureName("PRC_OBTENER_PARAMETROS_RECIBO")
					.declareParameters(new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
//			verify count connection
			out = jdbcCall.execute();
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO DATOS PARA EL RECIBO.");
		}
		return out;
	}

	@Override
	public Map<String, Object> validarReciboAnterior(int nis, String recibo) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_RECIBO_PAGO).withProcedureName("PRC_VALIDAR_ULTIMO_RECIBO")
					.declareParameters(new SqlParameter("vNIS", OracleTypes.NUMBER),
							new SqlParameter("vSIMBOLO_VAR", OracleTypes.VARCHAR),
							new SqlOutParameter("oCOMISION", OracleTypes.VARCHAR),
							new SqlOutParameter("oRESULT", OracleTypes.NUMBER),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("cRESP_SP2", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paramMap = new MapSqlParameterSource();
			paramMap.addValue("vNIS", nis, OracleTypes.NUMBER);
			paramMap.addValue("vSIMBOLO_VAR", recibo, OracleTypes.VARCHAR);
			out = jdbcCall.execute(paramMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR VALIDANDO RECIBOS ANTERIORES");
		}
		return out;

	}

}