package pe.com.ocv.service.impl;

import java.text.ParseException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import pe.com.ocv.dao.IOCVIncidenciaDAO;
import pe.com.ocv.model.AreaAfectada;
import pe.com.ocv.model.Incidencia;
import pe.com.ocv.model.Municipio;
import pe.com.ocv.model.Respuesta;

import org.springframework.stereotype.Service;
import pe.com.ocv.service.IOCVIncidenciaService;

@Service
@SuppressWarnings("unchecked")
public class OCVIncidenciaServiceImpl implements IOCVIncidenciaService {

	@Autowired
	private IOCVIncidenciaDAO dao;

	public OCVIncidenciaServiceImpl() {
		super();
	}

	@Override
	public Respuesta ListaMunicipiosAfectados() throws ParseException, Exception {
		List<Municipio> listaMunicAfectados = new ArrayList<Municipio>();
		Respuesta respuesta = new Respuesta();
		try {
			Map<String, Object> queryResp = this.dao.listaMunicipiosAfectados();
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					Municipio municipio = new Municipio();
					boolean existe = false;
					int codMunic = ((BigDecimal) map.get("CODMUNICIPIO")).intValue();
					municipio.setCod_municipio(codMunic);
					municipio.setNom_municipio((String) map.get("NOMBREMUNICIPIO"));
					municipio.setNro_incidencias(((BigDecimal) map.get("NROINCIDENCIAS")).intValue());

					Map<String, Object> queryResp2 = this.dao.listaIncidenciasMunicipio(codMunic);
					bdCast = (BigDecimal) queryResp2.get("nRESP_SP");
					iRespuesta = bdCast.intValueExact();
					if (iRespuesta != 0) {
						for (Map<String, Object> mapIncidencias : (List<Map<String, Object>>) queryResp2
								.get("IO_CURSOR")) {
							Incidencia incidencia = new Incidencia();
							int codIncidencia = ((BigDecimal) mapIncidencias.get("NROINCIDENCIA")).intValueExact();
							incidencia.setCod_incidencia(codIncidencia);
							incidencia.setNom_incidencia((String) mapIncidencias.get("NOMBREINCIDENCIA"));
							incidencia.setTipo_incidencia((String) mapIncidencias.get("TIPOINCIDENCIA"));
							incidencia.setEstado_incidencia((String) mapIncidencias.get("ESTADOINCIDENCIA"));
							incidencia.setFecha_inicio((String) mapIncidencias.get("FECHAINICIO"));
							incidencia.setFecha_estimada_sol((String) mapIncidencias.get("FECHAESTIMADASOLUCION"));
							incidencia.setObservacion((String) mapIncidencias.get("OBSERVACION"));
							incidencia.setNis_rad(((BigDecimal) mapIncidencias.get("NIS_RAD")).intValue());
							if (!Objects.isNull(mapIncidencias.get("LATITUD"))) {
								incidencia.setLatitud(((BigDecimal) mapIncidencias.get("LATITUD")).doubleValue());
								existe = true;
							}
							if (!Objects.isNull(mapIncidencias.get("LONGITUD"))) {
								incidencia.setLongitud(((BigDecimal) mapIncidencias.get("LONGITUD")).doubleValue());
								existe = true;
							}
							Map<String, Object> queryResp3 = this.dao
									.listaAreasAfectadas(codIncidencia);
							bdCast = (BigDecimal) queryResp3.get("nRESP_SP");
							iRespuesta = bdCast.intValueExact();
							if (iRespuesta != 0) {
								for (Map<String, Object> mapAreas : (List<Map<String, Object>>) queryResp3
										.get("IO_CURSOR")) {
									AreaAfectada area = new AreaAfectada();
									area.setTipo_area((String) mapAreas.get("TIPOAREA"));
									area.setDesc_area((String) mapAreas.get("DESCAREA"));
									incidencia.getAreas_afectadas().add(area);
								}
							}
							municipio.getIncidencias().add(incidencia);
						}
					}
					municipio.setMapa(existe);
					listaMunicAfectados.add(municipio);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVIncidenciaServiceImpl.ListaMunicipiosAfectados()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(listaMunicAfectados);
		return respuesta;
	}

	@Override
	public Respuesta ListaIncidenciasSuministro(Map<String, String> requestParm) throws ParseException, Exception {
		List<Municipio> listaMunicAfectados = new ArrayList<Municipio>();
		Respuesta respuesta = new Respuesta();
		try {

			int nis_rad = Integer.parseInt(requestParm.get("nis_rad"));

			Map<String, Object> queryResp = this.dao.listaMunicipiosSuministro(nis_rad);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					Municipio municipio = new Municipio();
					boolean existe = false;
					int codMunic = ((BigDecimal) map.get("CODMUNICIPIO")).intValue();
					municipio.setCod_municipio(codMunic);
					municipio.setNom_municipio((String) map.get("NOMBREMUNICIPIO"));
					municipio.setNro_incidencias(((BigDecimal) map.get("NROINCIDENCIAS")).intValue());

					Map<String, Object> queryResp2 = this.dao.listaIncidenciasSuministro(codMunic,
							nis_rad);
					bdCast = (BigDecimal) queryResp2.get("nRESP_SP");
					iRespuesta = bdCast.intValueExact();
					if (iRespuesta != 0) {
						for (Map<String, Object> mapIncidencias : (List<Map<String, Object>>) queryResp2
								.get("IO_CURSOR")) {
							Incidencia incidencia = new Incidencia();
							int codIncidencia = ((BigDecimal) mapIncidencias.get("NROINCIDENCIA")).intValueExact();
							incidencia.setCod_incidencia(codIncidencia);
							incidencia.setNom_incidencia((String) mapIncidencias.get("NOMBREINCIDENCIA"));
							incidencia.setTipo_incidencia((String) mapIncidencias.get("TIPOINCIDENCIA"));
							incidencia.setEstado_incidencia((String) mapIncidencias.get("ESTADOINCIDENCIA"));
							incidencia.setFecha_inicio((String) mapIncidencias.get("FECHAINICIO"));
							incidencia.setFecha_estimada_sol((String) mapIncidencias.get("FECHAESTIMADASOLUCION"));
							incidencia.setObservacion((String) mapIncidencias.get("OBSERVACION"));
							incidencia.setNis_rad(((BigDecimal) mapIncidencias.get("NIS_RAD")).intValue());
							if (!Objects.isNull(mapIncidencias.get("LATITUD"))) {
								incidencia.setLatitud(((BigDecimal) mapIncidencias.get("LATITUD")).doubleValue());
								existe = true;
							}
							if (!Objects.isNull(mapIncidencias.get("LONGITUD"))) {
								incidencia.setLongitud(((BigDecimal) mapIncidencias.get("LONGITUD")).doubleValue());
								existe = true;
							}
							Map<String, Object> queryResp3 = this.dao
									.listaAreasAfectadas(codIncidencia);
							bdCast = (BigDecimal) queryResp3.get("nRESP_SP");
							iRespuesta = bdCast.intValueExact();
							if (iRespuesta != 0) {
								for (Map<String, Object> mapAreas : (List<Map<String, Object>>) queryResp3
										.get("IO_CURSOR")) {
									AreaAfectada area = new AreaAfectada();
									area.setTipo_area((String) mapAreas.get("TIPOAREA"));
									area.setDesc_area((String) mapAreas.get("DESCAREA"));
									incidencia.getAreas_afectadas().add(area);
								}
							}
							municipio.getIncidencias().add(incidencia);
						}
					}
					municipio.setMapa(existe);
					listaMunicAfectados.add(municipio);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVIncidenciaServiceImpl.ListaIncidenciasSuministro()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP(listaMunicAfectados);
		return respuesta;
	}

	@Override
	public Respuesta ListaIncidenciasMunicipio(Map<String, String> requestParm) throws ParseException, Exception {
		List<Incidencia> listaIncidencias = new ArrayList<Incidencia>();
		Respuesta respuesta = new Respuesta();
		try {
			int codMunic = Integer.parseInt(requestParm.get("cod_municipio"));

			Map<String, Object> queryResp = this.dao.listaIncidenciasMunicipio(codMunic);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					Incidencia incidencia = new Incidencia();
					int codIncidencia = ((BigDecimal) map.get("NROINCIDENCIA")).intValueExact();
					incidencia.setCod_incidencia(codIncidencia);
					incidencia.setNom_incidencia((String) map.get("NOMBREINCIDENCIA"));
					incidencia.setTipo_incidencia((String) map.get("TIPOINCIDENCIA"));
					incidencia.setEstado_incidencia((String) map.get("ESTADOINCIDENCIA"));
					incidencia.setFecha_inicio((String) map.get("FECHAINICIO"));
					incidencia.setFecha_estimada_sol((String) map.get("FECHAESTIMADASOLUCION"));
					incidencia.setObservacion((String) map.get("OBSERVACION"));
					incidencia.setNis_rad(((BigDecimal) map.get("NIS_RAD")).intValue());
					if (!Objects.isNull(map.get("LATITUD"))) {
						incidencia.setLatitud(((BigDecimal) map.get("LATITUD")).doubleValue());
					}
					if (!Objects.isNull(map.get("LONGITUD"))) {
						incidencia.setLongitud(((BigDecimal) map.get("LONGITUD")).doubleValue());
					}
					Map<String, Object> queryResp2 = this.dao.listaAreasAfectadas(codIncidencia);
					bdCast = (BigDecimal) queryResp2.get("nRESP_SP");
					iRespuesta = bdCast.intValueExact();
					if (iRespuesta != 0) {
						for (Map<String, Object> mapAreas : (List<Map<String, Object>>) queryResp2.get("IO_CURSOR")) {
							AreaAfectada area = new AreaAfectada();
							area.setTipo_area((String) mapAreas.get("TIPOAREA"));
							area.setDesc_area((String) mapAreas.get("DESCAREA"));
							incidencia.getAreas_afectadas().add(area);
						}
					}
					listaIncidencias.add(incidencia);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVIncidenciaServiceImpl.ListaIncidenciasMunicipio()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(listaIncidencias);
		return respuesta;
	}

	@Override
	public Respuesta ListaAreasAfectadas(Map<String, String> requestParm) throws ParseException, Exception {
		List<AreaAfectada> listaAreasAfectadas = new ArrayList<AreaAfectada>();
		Respuesta respuesta = new Respuesta();
		try {

			int codIncidencia = Integer.parseInt(requestParm.get("cod_incidencia"));

			Map<String, Object> queryResp = this.dao.listaAreasAfectadas(codIncidencia);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) queryResp.get("IO_CURSOR")) {
					AreaAfectada area = new AreaAfectada();
					area.setTipo_area((String) map.get("TIPOAREA"));
					area.setDesc_area((String) map.get("DESCAREA"));
					listaAreasAfectadas.add(area);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVIncidenciaServiceImpl.ListaAreasAfectadas()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP(listaAreasAfectadas);
		return respuesta;
	}

	@Override
	public Respuesta ObtenerPredioAfectado(Map<String, String> requestParm) throws ParseException, Exception {
		Incidencia predio = new Incidencia();
		Respuesta respuesta = new Respuesta();
		try {
			int nis = Integer.parseInt(requestParm.get("nis_rad"));

			Map<String, Object> queryResp = this.dao.obtenerPredioAfectado(nis);
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			int iRespuesta = bdCast.intValueExact();
			if (iRespuesta != 0) {
				List<Map<String, Object>> incidencias = (List<Map<String, Object>>) queryResp.get("IO_CURSOR");
				if (incidencias.size() > 0) {
					for (Map<String, Object> map : incidencias) {
						predio.setNom_incidencia((String) map.get("NOMBREINCIDENCIA"));
						predio.setFecha_estimada_sol((String) map.get("FECHAESTIMADASOLUCION"));
					}
					respuesta.setbRESP(predio);
				} else {
					respuesta.setbRESP(null);
				}
			}

			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(iRespuesta);
		} catch (Exception e) {
			System.out.println("OCVIncidenciaServiceImpl.ObtenerPredioAfectado()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setbRESP(null);
			respuesta.setnRESP_SP(0);
		}

		return respuesta;
	}

	@Override
	public Respuesta ListaIncidencias() throws ParseException, Exception {
		Respuesta respuesta = new Respuesta();
		try {

			Map<String, Object> queryResp = this.dao.listarIncidencias();
			BigDecimal bdCast = (BigDecimal) queryResp.get("nRESP_SP");
			List<Map<String, Object>> incidencias = (List<Map<String, Object>>) queryResp.get("IO_CURSOR");
			respuesta.setcRESP_SP((String) queryResp.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValueExact());
			respuesta.setbRESP(incidencias);
		} catch (Exception e) {
			System.out.println("OCVIncidenciaServiceImpl.ListaIncidencias()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		return respuesta;
	}

}