package pe.com.ocv.util;

import org.springframework.jdbc.core.JdbcTemplate;

public class DatasourceUtil {
	
	public static void closeConnection(JdbcTemplate jdbcTemplate) throws Exception{
		try {
			jdbcTemplate.getDataSource().getConnection().close();
		}catch(Exception e) {
			System.out.println(e + "Error al cerrar conexion de base de datos.");
		}
		
	}
	
}
