package pe.com.ocv.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtFilter extends GenericFilterBean {
	private JwtUtil tokenAuthenticationService;

	public JwtFilter() {
		super();
		this.tokenAuthenticationService = new JwtUtil();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
		try {
			Authentication authentication = this.tokenAuthenticationService.getAuthentication((HttpServletRequest) req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(req, res);
		} catch (ExpiredJwtException e) {
			HttpServletResponse response = (HttpServletResponse) res;
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			System.out.println("No autorizado porque expiró: " + e);
		} catch (SignatureException e) {
			HttpServletResponse response = (HttpServletResponse) res;
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			System.out.println("Prohibido porque no se pudo validar el token: " + e);
		} catch (Exception e) {
			HttpServletResponse response = (HttpServletResponse) res;
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			System.out.println("Error: " + e.toString());
		}
	}
}