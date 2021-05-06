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
import pe.com.ocv.dao.IOCVLugaresPagoDAO;
import pe.com.ocv.util.Constantes;
import pe.com.ocv.util.DatasourceUtil;

@Repository
public class OCVLugaresPagoDAOImpl implements IOCVLugaresPagoDAO {
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public Map<String, Object> listaAgencias() {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_LUGAR_PAGO).withProcedureName("PRC_LISTAR_AGENCIAS")
					.declareParameters(new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			out = jdbcCall.execute();
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO LISTA DE AGENCIAS.");
		}
		return out;
	}

	@Override
	public Map<String, Object> listaSucursales(int codAgencia) {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall jdbcCall;
			jdbcCall = new SimpleJdbcCall(this.jdbc).withSchemaName(Constantes.BD_SCHEMA)
					.withCatalogName(Constantes.PKG_LUGAR_PAGO).withProcedureName("PRC_LISTAR_SUCURSALES_AGENCIA")
					.declareParameters(new SqlParameter("vCOD_AGENCIA", OracleTypes.NUMBER),
							new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR),
							new SqlOutParameter("cRESP_SP", OracleTypes.VARCHAR),
							new SqlOutParameter("nRESP_SP", OracleTypes.NUMBER));
			MapSqlParameterSource paraMap = new MapSqlParameterSource().addValue("vCOD_AGENCIA", codAgencia);
			out = jdbcCall.execute(paraMap);
//			close connection
			DatasourceUtil.closeConnection(this.jdbc);
		} catch (Exception e) {
			System.out.println(e + "ERROR OBTENIENDO LISTA DE AGENCIAS.");
		}
		return out;
	}

}