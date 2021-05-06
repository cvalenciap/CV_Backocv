package pe.com.ocv.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import pe.com.ocv.util.Constantes;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {
	private JwtUtil tokenAuthenticationService;

	public LoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		this.tokenAuthenticationService = new JwtUtil();
		this.setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, PATCH, POST, PUT, HEAD");
		res.setHeader("Access-Control-Max-Age", "600");
		res.setHeader("Access-Control-Allow-Headers",
				"x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		User user = new User(req.getParameter("username"), req.getParameter("password"), req.getParameter("role"));
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
				user.getPassword());
		if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
			User user2 = new User(Constantes.USER_TOKEN, Constantes.PASSWORD_TOKEN, Constantes.ROLE_TOKEN);
			UsernamePasswordAuthenticationToken token2 = new UsernamePasswordAuthenticationToken(user2.getUsername(),
					user2.getPassword());
			return this.getAuthenticationManager().authenticate(token2);
		}
		return this.getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		String name = auth.getName();
		this.tokenAuthenticationService.addAuthentication(res, name);
	}
}