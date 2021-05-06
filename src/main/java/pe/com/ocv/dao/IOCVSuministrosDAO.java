package pe.com.ocv.dao;

import java.util.Map;

public abstract interface IOCVSuministrosDAO {

	public abstract Map<String, Object> obtenerListNisXCliente(int nis_rad);

	public abstract Map<String, Object> CabeceraXNis(int nis);

	public abstract Map<String, Object> historicoConsumo(int nis);

}