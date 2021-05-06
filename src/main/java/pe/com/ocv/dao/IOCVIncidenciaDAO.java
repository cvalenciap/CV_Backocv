package pe.com.ocv.dao;

import java.util.Map;

public abstract interface IOCVIncidenciaDAO {
	
	public abstract Map<String, Object> listaMunicipiosAfectados();
	
	public abstract Map<String, Object> listaMunicipiosSuministro(int nisRad);

	public abstract Map<String, Object> listaIncidenciasMunicipio(int codMunic);

	public abstract Map<String, Object> listaIncidenciasSuministro(int codMunic, int nisRad);

	public abstract Map<String, Object> listaAreasAfectadas(int codIncidencia);

	public abstract Map<String, Object> obtenerPredioAfectado(int nis);
	
	public abstract Map<String, Object> obtenerCoordenadas(int codMunic);
	
	public abstract Map<String, Object> listarIncidencias();
	
}