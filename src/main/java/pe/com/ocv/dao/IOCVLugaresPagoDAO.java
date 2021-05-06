package pe.com.ocv.dao;

import java.util.Map;

public abstract interface IOCVLugaresPagoDAO {
	
	public abstract Map<String, Object> listaAgencias();

	public abstract Map<String, Object> listaSucursales(int codAgencia);
	
}