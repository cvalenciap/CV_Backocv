package pe.com.ocv.util;

import java.util.Random;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.TreeMap;
import java.util.Map;

public class Encriptacion {
	public Encriptacion() {
		super();
	}

	public Map<String, Object> convertirSHA256(String password) {
		Map<String, Object> out = new TreeMap<String, Object>();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		byte[] hash = md.digest(password.getBytes());
		StringBuffer sb = new StringBuffer();
		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}
		out.put("vINGRESO", password);
		out.put("vSALIDA", sb.toString());
		return out;
	}

	public Map<String, Object> generarClave() {
		Map<String, Object> out = new TreeMap<String, Object>();
		String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
		String minusculas = "abcdefghijklmnopqrstuvxyz";
		String numeros = "0123456789";
		String clave = null;
		clave = "";
		Random rMay = new Random();
		Random rMin = new Random(134L);
		Random rNum = new Random(155L);
		int longClave = (int) (Math.random() * 10.0 + 1.0);
		longClave += 8;
		int cantidadMay = (int) (Math.random() * 6.0 + 1.0);
		int cantidadMin;
		int cantidadNum;
		if (cantidadMay == longClave - 2) {
			cantidadMin = 1;
			cantidadNum = 1;
		} else {
			int pivot = longClave - 2 - cantidadMay;
			cantidadMin = (int) (Math.random() * pivot + 1.0);
			cantidadNum = longClave - cantidadMin - cantidadMay;
		}
		for (int i = 0; i < cantidadMay; ++i) {
			clave += mayusculas.charAt(rMay.nextInt(25));
		}
		for (int i = 0; i < cantidadMin; ++i) {
			clave += minusculas.charAt(rMin.nextInt(25));
		}
		for (int i = 0; i < cantidadNum; ++i) {
			clave += numeros.charAt(rNum.nextInt(10));
		}
		out.put("CLAVE", clave);
		return out;
	}
}