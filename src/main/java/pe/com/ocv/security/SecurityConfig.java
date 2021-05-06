package pe.com.ocv.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pe.com.ocv.util.Constantes;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private String user;
	private String pwd;
	private String role;

	public SecurityConfig() {
		super();
		this.user = Constantes.USER_TOKEN;
		this.pwd = Constantes.PASSWORD_TOKEN;
		this.role = Constantes.ROLE_TOKEN;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.headers().cacheControl();

		http.csrf().disable().authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.antMatchers(HttpMethod.POST, "/api/login").permitAll()
			.antMatchers(HttpMethod.POST, "/api/general/pago-ejecutado").permitAll()
			/*without filter confirmar*/
			.antMatchers(HttpMethod.POST, "/api/registro-usuario/confirmar/**").permitAll()
			/**/
			.antMatchers(HttpMethod.GET, "/api/general/**").permitAll()
			.antMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
			.antMatchers(HttpMethod.GET, "/images/**").permitAll()
			
			.anyRequest().authenticated()
			.and()			
			
			.addFilterBefore(new LoginFilter("/api/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(this.user).password(this.pwd).roles(new String[] { this.role });
	}
}