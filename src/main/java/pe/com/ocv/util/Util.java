package pe.com.ocv.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pe.com.ocv.model.Respuesta;

public class Util {
	public Util() {
		super();
	}

	public Respuesta validaFormatoCorreo(String correo) {
		String eMailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,4})$";
		Respuesta respuesta = new Respuesta(1, "");
		Pattern pattern = Pattern.compile(eMailPattern);
		if (correo != null) {
			Matcher matcher = pattern.matcher(correo);
			if (matcher.matches()) {
				respuesta.setnRESP_SP(1);
				respuesta.setcRESP_SP("Correo Valido.");
			} else {
				respuesta.setnRESP_SP(0);
				respuesta.setcRESP_SP("Formato Incorrecto.");
			}
		} else {
			respuesta.setnRESP_SP(0);
			respuesta.setcRESP_SP("Debe ingresar un correo.");
		}
		return respuesta;
	}

	public static HttpURLConnection setUrlProperties(HttpURLConnection urlCon, String user, String password,
			String contentType, String method, String parameters) throws IOException {
		String userCredentials = user + ":" + password;
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
		urlCon.setRequestProperty("Authorization", basicAuth);
		urlCon.setUseCaches(false);
		urlCon.setDoInput(true);
		urlCon.setDoOutput(true);
		urlCon.setRequestProperty("Content-Type", contentType);
		urlCon.setRequestMethod(method);
		DataOutputStream wr = new DataOutputStream(urlCon.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();
		return urlCon;
	}

	public static HttpURLConnection setUrlProperties(HttpURLConnection urlCon, String bearer, String contentType,
			String method, String parameters) throws IOException {
		String basicAuth = "Bearer " + bearer;
		urlCon.setRequestProperty("Authorization", basicAuth);
		urlCon.setUseCaches(false);
		urlCon.setDoInput(true);
		urlCon.setDoOutput(true);
		urlCon.setRequestProperty("Content-Type", contentType);
		urlCon.setRequestMethod(method);
		DataOutputStream wr = new DataOutputStream(urlCon.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();
		return urlCon;
	}

	public static HttpURLConnection setUrlProperties(HttpURLConnection urlCon, String sessionToken, String contentType, String parameters) throws IOException {
		urlCon.setRequestProperty("Authorization", sessionToken);
		urlCon.setUseCaches(false);
		urlCon.setDoInput(true);
		urlCon.setDoOutput(true);
		urlCon.setRequestProperty("Content-Type", contentType);
		urlCon.setRequestMethod("POST");
		DataOutputStream wr = new DataOutputStream(urlCon.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();
		return urlCon;
	}

	public static StringBuffer getResponse(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response;
	}

	public static String toDateFormat(String fecha) {
		try {
			SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
			return myFormat.format(fromUser.parse(fecha));
		} catch (Exception e) {
			return fecha;
		}
	}
	
//	obsoleto : gestion por registros de tabla
//	public static Integer getValueConstantAuditoria(String flagChannel, String tipoLog) {
//		Integer valueRegister = 0;
//		flagChannel = flagChannel != null ? flagChannel : Constantes.FLAG_CHANNEL_APK;
//		try {
//			switch (flagChannel) {
//			  case Constantes.FLAG_CHANNEL_WEB:
//			    valueRegister = Integer.valueOf((Constantes.FLAG_CHANNEL_WEB).concat(tipoLog));
//			    break;
//			  case Constantes.FLAG_CHANNEL_APK:
//				  valueRegister = Integer.valueOf((Constantes.FLAG_CHANNEL_APK).concat(tipoLog));
//			    break;
//			  case Constantes.FLAG_CHANNEL_IOS:
//				  valueRegister = Integer.valueOf((Constantes.FLAG_CHANNEL_IOS).concat(tipoLog));
//			    break;
//			  default:
//				  valueRegister = Integer.valueOf((Constantes.FLAG_CHANNEL_WEB).concat(tipoLog));
//			    break;
//			}
//			return valueRegister;
//		} catch (Exception e){
//			return valueRegister;
//		}
//	}
	
	public static String concatenar(String... strParte) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < strParte.length; i++) {
			sb.append(strParte[i]);
		}
		return sb.toString();
	}
}