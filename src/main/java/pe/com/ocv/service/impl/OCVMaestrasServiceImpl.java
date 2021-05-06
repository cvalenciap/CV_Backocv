package pe.com.ocv.service.impl;

import pe.com.ocv.util.DesencriptarAquanet;
import pe.com.ocv.util.DesencriptarMovilAntiguo;
import pe.com.ocv.util.Encriptacion;
import pe.com.ocv.model.PreguntaSecreta;
import pe.com.ocv.model.Respuesta;
import pe.com.ocv.model.Distrito;
import pe.com.ocv.model.OCVRegistro;
import pe.com.ocv.model.TipoSolicitante;
import pe.com.ocv.model.TipoTarjeta;
import pe.com.ocv.model.TipoDocumento;
import pe.com.ocv.dao.IOCVMaestrasDAO;
import pe.com.ocv.dao.IOCVRegistroDAO;
import pe.com.ocv.service.IOCVMaestrasService;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unchecked")
public class OCVMaestrasServiceImpl implements IOCVMaestrasService {

	@Autowired
	private IOCVMaestrasDAO dao;

	@Autowired
	private IOCVRegistroDAO daoReg;

	private Encriptacion encriptacion;

	public OCVMaestrasServiceImpl() {
		super();
		this.encriptacion = new Encriptacion();
	}

	@Override
	public Respuesta devuelveMaestras() {
		List<TipoSolicitante> lTipoSolicitante = new ArrayList<TipoSolicitante>();
		List<PreguntaSecreta> lPreguntaSecreta = new ArrayList<PreguntaSecreta>();
		List<TipoDocumento> lTipoDocumento = new ArrayList<TipoDocumento>();
		Map<String, Object> bResp = new TreeMap<String, Object>();
		List<Distrito> lDistrito = new ArrayList<Distrito>();
		List<TipoTarjeta> lTipoTarjeta = new ArrayList<TipoTarjeta>();
		Map<String, Object> dato = new TreeMap<String, Object>();
		/*inicio cvalenciap*/
		Map<String, Object> url = new TreeMap<String, Object>();
		/*fin cvalenciap*/
		Respuesta respuesta = new Respuesta(0, "");
		try {
			Map<String, Object> out_1 = this.dao.devuelveMaestras(1);
			BigDecimal bdCast = (BigDecimal) out_1.get("nRESP_SP");
			respuesta.setcRESP_SP((String) out_1.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValueExact());
			if (respuesta.getnRESP_SP() != 0) {
				for (Map<String, Object> map_1 : (List<Map<String, Object>>) out_1.get("IO_CURSOR")) {
					TipoSolicitante tSolicitante = new TipoSolicitante();
					bdCast = (BigDecimal) map_1.get("ID_TIPOSOLICITANTE");
					tSolicitante.setIdTipoSolicitante(bdCast.intValueExact());
					tSolicitante.setTipoSolicitante((String) map_1.get("TIPOSOLICITANTE"));
					tSolicitante.setDescripcion((String) map_1.get("DESCRIPCION"));
					lTipoSolicitante.add(tSolicitante);
				}
			}

			Map<String, Object> out_2 = this.dao.devuelveMaestras(2);
			bdCast = (BigDecimal) out_2.get("nRESP_SP");
			respuesta.setcRESP_SP((String) out_2.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValueExact());
			if (respuesta.getnRESP_SP() != 0) {
				for (Map<String, Object> map_2 : (List<Map<String, Object>>) out_2.get("IO_CURSOR")) {
					TipoDocumento tDocumento = new TipoDocumento();
					bdCast = (BigDecimal) map_2.get("ID_TIPODOCUMENTO");
					tDocumento.setIdTipoDocumento(bdCast.intValueExact());
					tDocumento.setTipoDocumento((String) map_2.get("TIPODOCUMENTO"));
					tDocumento.setDescripcion((String) map_2.get("DESCRIPCION"));
					lTipoDocumento.add(tDocumento);
				}
			}

			Map<String, Object> out_3 = this.dao.devuelveMaestras(3);
			bdCast = (BigDecimal) out_3.get("nRESP_SP");
			respuesta.setcRESP_SP((String) out_3.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValueExact());
			if (respuesta.getnRESP_SP() != 0) {
				for (Map<String, Object> map_3 : (List<Map<String, Object>>) out_3.get("IO_CURSOR")) {
					Distrito tDistrito = new Distrito();
					bdCast = (BigDecimal) map_3.get("COD_MUNIC");
					tDistrito.setCodDist(bdCast.intValueExact());
					tDistrito.setDesDist((String) map_3.get("NOM_MUNIC"));
					lDistrito.add(tDistrito);
				}
			}
			
//			inicio cvalenciap
//			Map<String, Object> out_4 = (Map<String, Object>) this.dao.devuelveMaestras(4);
//			bdCast = (BigDecimal) out_4.get("nRESP_SP");
//			respuesta.setcRESP_SP((String) out_4.get("cRESP_SP"));
//			respuesta.setnRESP_SP(bdCast.intValueExact());
//			if (respuesta.getnRESP_SP() != 0) {
//				for (Map<String, Object> map_4 : (List<Map<String, Object>>) out_4.get("IO_CURSOR")) {
//					PreguntaSecreta tPreguntaSecreta = new PreguntaSecreta();
//					bdCast = (BigDecimal) map_4.get("ID_PREGUNTASECRETA");
//					tPreguntaSecreta.setIdPreguntaSecreta(bdCast.intValueExact());
//					tPreguntaSecreta.setPreguntaSecreta((String) map_4.get("PREGUNTA"));
//					lPreguntaSecreta.add(tPreguntaSecreta);
//				}
//			}
//			fin cvalenciap

			/*
			 * Map<String, Object> out_5 = (Map<String, Object>)
			 * this.dao.devuelveMaestras(5); bdCast = (BigDecimal) out_5.get("nRESP_SP");
			 * respuesta.setcRESP_SP((String) out_5.get("cRESP_SP"));
			 * respuesta.setnRESP_SP(bdCast.intValueExact()); if (respuesta.getnRESP_SP() !=
			 * 0) { for (Map<String, Object> map_5 : (List<Map<String, Object>>)
			 * out_5.get("IO_CURSOR")) { Mensaje tMensaje = new Mensaje();
			 * tMensaje.setCodMensaje((String) map_5.get("COD_MENSAJE"));
			 * tMensaje.setDescMensaje((String) map_5.get("DESC_MENSAJE"));
			 * lMensaje.add(tMensaje); } }
			 */

			Map<String, Object> out_6 = this.dao.devuelveMaestras(6);
			bdCast = (BigDecimal) out_6.get("nRESP_SP");
			respuesta.setcRESP_SP((String) out_6.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValueExact());
			if (respuesta.getnRESP_SP() != 0) {
				for (Map<String, Object> map_6 : (List<Map<String, Object>>) out_6.get("IO_CURSOR")) {
					TipoTarjeta tTipoTarjeta = new TipoTarjeta();
					tTipoTarjeta.setId_tarjeta(((BigDecimal) map_6.get("ID_TARJETA")).intValueExact());
					tTipoTarjeta.setDescripcion(map_6.get("DESCRIPCION").toString());
					lTipoTarjeta.add(tTipoTarjeta);
				}
			}

			Map<String, Object> out_7 = this.dao.devuelveMaestras(7);
			bdCast = (BigDecimal) out_7.get("nRESP_SP");
			respuesta.setcRESP_SP((String) out_7.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValueExact());
			if (respuesta.getnRESP_SP() != 0) {
				for (Map<String, Object> map_7 : (List<Map<String, Object>>) out_7.get("IO_CURSOR")) {
					dato.put(map_7.get("clase").toString(), map_7.get("valor"));
				}
			}
			
			/*inicio cvalenciap*/
			Map<String, Object> out_8 = this.dao.devuelveMaestras(8);
			bdCast = (BigDecimal) out_8.get("nRESP_SP");
			respuesta.setcRESP_SP((String) out_8.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValueExact());
			if (respuesta.getnRESP_SP() != 0) {
				for (Map<String, Object> map_8 : (List<Map<String, Object>>) out_8.get("IO_CURSOR")) {
					url.put(map_8.get("clase").toString(), map_8.get("valor"));
				}
			}
			bResp.put("urlWebFront", url);
			/*fin cvalenciap*/
			
			bResp.put("listaTipoDocumentos", lTipoDocumento);
			bResp.put("listaTipoSolicitantes", lTipoSolicitante);
			bResp.put("listaDistritos", lDistrito);
			bResp.put("listaPreguntaSecreta", lPreguntaSecreta);
			// bResp.put("listaMensajes", lMensaje);
			bResp.put("listaTipoTarjetas", lTipoTarjeta);
			bResp.put("lImagenes", dato);
		} catch (Exception e) {
			System.out.println("OCVMaestrasServiceImpl.devuelveMaestras()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}
		respuesta.setbRESP(bResp);
		return respuesta;
	}

	@Override
	public Respuesta migrarUsuariosAntiguos() {
		List<Map<String, Object>> listaUsuarios = new ArrayList<Map<String, Object>>();
		Respuesta respuesta = new Respuesta();
		try {
			Map<String, Object> out = this.dao.obtenerUsuariosAntiguos();
			BigDecimal bdCast = (BigDecimal) out.get("nRESP_SP");
			if (bdCast.intValue() != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) out.get("IO_CURSOR")) {
					String claveDesencriptada = "";
					String cRESP_REG = "";
					int nRESP_REG = 0;
					Map<String, Object> detalle = new TreeMap<String, Object>();
					try {
						String dominio = map.get("EMAIL").toString().split("@")[1];
						boolean dominioValido = true;
						int result = doLookup(dominio);
						if (result == 0) {
							dominioValido = false;
						}

						if (dominioValido) {
							OCVRegistro usuario = new OCVRegistro();
							// Encripta la clave
							int tipoMigracion = ((BigDecimal) map.get("TIPO_MIGRACION")).intValueExact();
							if (tipoMigracion == 1) {
								claveDesencriptada = DesencriptarMovilAntiguo.decrypt(map.get("PASSWORD").toString());
								detalle.put("tipo", "App Movil");
							} else {
								claveDesencriptada = DesencriptarAquanet.decrypt(map.get("PASSWORD").toString());
								detalle.put("tipo", "Aquanet");
							}
							Map<String, Object> encriptaClave = this.encriptacion
									.convertirSHA256(claveDesencriptada);
							String sClave = (String) encriptaClave.get("vSALIDA");
							// usuario.setRef_cobro(claveDesencriptada);
							usuario.setNis_rad(Integer.parseInt(map.get("NIS_RAD").toString()));
							usuario.setRef_cobro("" + tipoMigracion);
							usuario.setTipo_docu(6);
							usuario.setNro_doc(map.get("DNI").toString());
							usuario.setApellido1(map.get("NOMBRE").toString());
							usuario.setApellido2("");
							usuario.setNombres("");
							usuario.setCorreo(map.get("EMAIL").toString());
							usuario.setTelefono1(map.get("TELEFONO").toString());
							usuario.setClave(sClave);
							usuario.setAcepta_terminos(1);
							usuario.setAcepta_noti(Integer.parseInt(map.get("ENVIA_EMAIL").toString()));
							usuario.setAcepta_correo(Integer.parseInt(map.get("ENVIA_EMAIL").toString()));

							Map<String, Object> outReg = this.daoReg.insertaRegistro(usuario,
									true);
							BigDecimal bdCastReg = (BigDecimal) outReg.get("nRESP_SP");
							nRESP_REG = bdCastReg.intValueExact();
							cRESP_REG = (String) outReg.get("cRESP_SP");
						} else {
							nRESP_REG = 0;
							cRESP_REG = "Dominio no válido";

						}
					} catch (Exception e) {
						nRESP_REG = 0;
						cRESP_REG = "hubo un error: " + e;
					}
					detalle.put("email", map.get("EMAIL"));
					//detalle.put("clave", claveDesencriptada);
					detalle.put("nRESP_reg", nRESP_REG);
					detalle.put("cRESP_reg", cRESP_REG);
					listaUsuarios.add(detalle);
				}
			}

			respuesta.setcRESP_SP((String) out.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValue());
		} catch (Exception e) {
			System.out.println("OCVMaestrasServiceImpl.migrarUsuariosAntiguos()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(listaUsuarios);
		return respuesta;
	}

	@Override
	public Respuesta obtenerTitulos() {
		List<Map<String, Object>> bResp = new ArrayList<Map<String, Object>>();
		Respuesta respuesta = new Respuesta(0, "");

		try {
			Map<String, Object> out = this.dao.obtenerTitulos();
			BigDecimal bdCast = (BigDecimal) out.get("nRESP_SP");
			respuesta.setcRESP_SP((String) out.get("cRESP_SP"));
			respuesta.setnRESP_SP(bdCast.intValueExact());
			if (respuesta.getnRESP_SP() != 0) {
				for (Map<String, Object> map : (List<Map<String, Object>>) out.get("IO_CURSOR")) {
					Map<String, Object> detalle = new TreeMap<String, Object>();
					detalle.put("nombre", map.get("COD_MENSAJE"));
					detalle.put("descripcion", map.get("DESC_MENSAJE"));
					bResp.add(detalle);
				}
			}
		} catch (Exception e) {
			System.out.println("OCVMaestrasServiceImpl.obtenerTitulos()");
			System.err.println("Ocurrió un error: " + e);
			respuesta.setcRESP_SP("Ocurrió un error: " + e);
			respuesta.setnRESP_SP(0);
		}

		respuesta.setbRESP(bResp);
		return respuesta;
	}

	static int doLookup(String hostName) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		// int result = 1;
		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		DirContext ictx = new InitialDirContext(env);
		Attributes attrs = ictx.getAttributes(hostName, new String[] { "MX" });
		Attribute attr = attrs.get("MX");
		if (attr == null)
			return (0);
		return (attr.size());
	}

}