package pe.com.ocv.dao;

import java.util.Map;

public abstract interface IOCVMaestrasDAO {
	
	public abstract Map<String, Object> devuelveMaestras(int i);

	public abstract Map<String, Object> obtenerUsuariosAntiguos();

	public abstract Map<String, Object> obtenerDominios();
	
	public abstract Map<String, Object> obtenerTitulos();
	
}