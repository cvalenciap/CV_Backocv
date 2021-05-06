package pe.com.ocv.util;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;

public class DesencriptarAquanet {
	static Cipher ecipher;
	static Cipher dcipher;
	static byte[] salt;

	public DesencriptarAquanet() {
		super();
	}

	public static String decrypt(String str) {
		try {
			return new String(DesencriptarAquanet.dcipher.doFinal(new Base64().decode(str)));
		} catch (Exception ex) {
			System.out.println("JwtUtil.decrypt: " + ex.getMessage());
		}
		return null;
	}

	static {
		DesencriptarAquanet.salt = new byte[] { -87, -101, -56, 50, 86, 53, -29, 3 };
		try {
			int iterationCount = 2;
			KeySpec keySpec = new PBEKeySpec("aldjgbcne34kso2945ndh2j4j211".toCharArray(), DesencriptarAquanet.salt,
					iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			DesencriptarAquanet.ecipher = Cipher.getInstance(key.getAlgorithm());
			DesencriptarAquanet.dcipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(DesencriptarAquanet.salt, iterationCount);
			DesencriptarAquanet.ecipher.init(1, key, paramSpec);
			DesencriptarAquanet.dcipher.init(2, key, paramSpec);
		} catch (Exception ex) {
			System.out.println("JwtUtil.decrypt: " + ex.getMessage());
		}
	}
}