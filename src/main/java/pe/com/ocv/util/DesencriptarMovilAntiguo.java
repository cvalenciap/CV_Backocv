package pe.com.ocv.util;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;

public class DesencriptarMovilAntiguo {

	static Cipher dcipher;
	static byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3,
			(byte) 0x03 };

	static {
		try {
			int iterationCount = 2;
			KeySpec keySpec = new PBEKeySpec("aldjgbcne34kso2945ndh2j4j211".toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			dcipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (Exception ex) {
			System.out.println("error: " + ex.getMessage());
		}
	}

	public static String decrypt(String str) {

		try {
			return new String(dcipher.doFinal(new BASE64Decoder().decodeBuffer(str)));
		} catch (Exception ex) {
			System.out.println("error: " + ex.getMessage());
		}
		return null;
	}
}
