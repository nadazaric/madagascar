package ftn.rbs.madagascar_hub.security.auth;

import java.io.IOException;

import ftn.rbs.madagascar_hub.security.jwt.IJWTTokenService;
import ftn.rbs.madagascar_hub.security.jwt.TokenUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private TokenUtils tokenUtils;

	private UserDetailsService userDetailsService;
	
	private IJWTTokenService tokenService;
	
	protected final Log LOGGER = LogFactory.getLog(getClass());

	public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService, IJWTTokenService tokenService) {
		this.tokenUtils = tokenHelper;
		this.userDetailsService = userDetailsService;
		this.tokenService = tokenService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {


		String email;
		System.out.println(request.getRequestURI());
		String authToken = tokenUtils.getToken(request);
		String username;
		try {
	
			if (authToken != null) {
				
				
				if (authToken.charAt(0) == '\"')
					authToken = authToken.substring(1, authToken.length() - 1);


				username = tokenUtils.getUsernameFromToken(authToken);
				if (username != null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					
					if (tokenUtils.validateToken(authToken, userDetails)) {
						TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
						authentication.setToken(authToken);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			}
			
		} catch (ExpiredJwtException ex) {
			LOGGER.debug("Token expired!");
			throw ex;
		} 
		
		chain.doFilter(request, response);
	}
}