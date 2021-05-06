package pe.com.ocv.security;

import java.util.Base64;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;

public class JwtUtil {
	private long EXPIRATIONTIME;
	private String headerString;
	private static Key secret;
	private static byte[] secretBytes;
	private static String base64SecretBytes;

	public JwtUtil() {
		super();
		this.EXPIRATIONTIME = 1000 * 60 * 60;
		this.headerString = "Authorization";
	}

	public void addAuthentication(HttpServletResponse res, String username) {
		String token = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + this.EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS256, JwtUtil.base64SecretBytes).compact();
		res.setContentType("application/json;charset=UTF-8");
		String cadena = "{\"cRESP_SP\" : \"Ejecución Correcta\",";
		cadena += "\"nRESP_SP\": 1,";
		cadena = cadena + "\"bRESP\" : {\"token\": \"" + token + "\"}}";
		try {
			res.getWriter().write(cadena);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(this.headerString);
		if (token != null) {
			String username = Jwts.parser().setSigningKey(JwtUtil.secret).parseClaimsJws(token).getBody()
					.getSubject();
			if (username != null) {
				return new AuthenticatedUser(username);
			}
		}
		return null;
	}

	static {
		secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
		secretBytes = JwtUtil.secret.getEncoded();
		base64SecretBytes = Base64.getEncoder().encodeToString(JwtUtil.secretBytes);
	}
}