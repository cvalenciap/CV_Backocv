package pe.com.ocv.util;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import org.springframework.stereotype.Component;
import javax.servlet.Filter;

@Component
public class CORSFilter implements Filter {
	public CORSFilter() {
		super();
	}

	@Override
	public void destroy() {
	}
	//
	public static String VALID_METHODS = "DELETE, HEAD, GET, OPTIONS, POST, PUT";

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
//		final HttpServletResponse response = (HttpServletResponse) res;
		/*
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, PATCH, POST, PUT, HEAD");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Auth-Token, Access-Control-Request-Headers, Access-Control-Request-Method, Accept, x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		chain.doFilter(req, res);*/
		
		// No Origin header present means this is not a cross-domain request
		HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) res;
		String origin = httpReq.getHeader("Origin");
         if (origin == null) {
            // Return standard response if OPTIONS request w/o Origin header
           if ("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
                httpResp.setHeader("Allow", VALID_METHODS);
                httpResp.setStatus(200);
                return;
            }
        } else {
            // This is a cross-domain request, add headers allowing access
            httpResp.setHeader("Access-Control-Allow-Origin", origin);
            httpResp.setHeader("Access-Control-Allow-Methods", VALID_METHODS);

            String headers = httpReq.getHeader("Access-Control-Request-Headers");
            if (headers != null)
                httpResp.setHeader("Access-Control-Allow-Headers", headers);

            // Allow caching cross-domain permission
            httpResp.setHeader("Access-Control-Max-Age", "3600");
        }
        // Pass request down the chain, except for OPTIONS
        if (!"OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            chain.doFilter(req, res);
        }
	}

	@Override
	public void init(final FilterConfig arg0) throws ServletException {
	}
}