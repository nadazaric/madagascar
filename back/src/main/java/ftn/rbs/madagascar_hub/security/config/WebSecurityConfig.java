package ftn.rbs.madagascar_hub.security.config;

import ftn.rbs.madagascar_hub.security.auth.RestAuthenticationEntryPoint;
import ftn.rbs.madagascar_hub.security.auth.TokenAuthenticationFilter;
import ftn.rbs.madagascar_hub.security.jwt.IJWTTokenService;
import ftn.rbs.madagascar_hub.security.jwt.TokenUtils;
import ftn.rbs.madagascar_hub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
	
	@Autowired
	private IJWTTokenService tokenService;

	@Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  	
  
 	@Bean
 	public DaoAuthenticationProvider authenticationProvider() {
 	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
 	  
 	    authProvider.setUserDetailsService(userDetailsService());
 	    authProvider.setPasswordEncoder(passwordEncoder());
 	 
 	    return authProvider;
 	}

 	@Autowired
 	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
 
 	@Bean
 	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
 	    return authConfig.getAuthenticationManager();
 	}
 	
	@Autowired
	private TokenUtils tokenUtils;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/api/user/login").permitAll()
								.requestMatchers("/api/user").permitAll()
								.requestMatchers("/api/oauth/callback").permitAll()
								.requestMatchers("/api/user/send/verification/email/{email}").permitAll()
								.requestMatchers("/api/user/activate/{activationId}").permitAll()
								.requestMatchers("/api/user/reset/password/email/{email}").permitAll()
								.requestMatchers("/api/user/resetPassword").permitAll()
								.requestMatchers("api/certificate/validate-upload").permitAll()
								.requestMatchers("/api/user/rotatePassword").permitAll()
								.requestMatchers("/api/**").permitAll()
								.anyRequest().authenticated()
				)
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.exceptionHandling(exceptionHandling ->
						exceptionHandling.authenticationEntryPoint(restAuthenticationEntryPoint)
				)
				.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userDetailsService(), tokenService), BasicAuthenticationFilter.class)
				.headers(headers -> headers
						.frameOptions(frameOptions -> frameOptions.disable())
//						.xssProtection(xssProtection -> xssProtection.block(true))
						.contentSecurityPolicy(contentSecurityPolicy -> contentSecurityPolicy.policyDirectives("script-src 'self'"))
				)
				.authenticationProvider(authenticationProvider());

		return http.build();
	}

	 @Bean
	 public CorsConfigurationSource corsConfigurationSource() {
	     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	     CorsConfiguration config = new CorsConfiguration();
	     config.setAllowedOrigins(Arrays.asList("*"));
	     config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	     config.setAllowedHeaders(Arrays.asList("*"));
	     source.registerCorsConfiguration("/**", config);
	     return source;
	 }

	// metoda u kojoj se definisu putanje za igorisanje autentifikacije
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// Dozvoljena POST metoda na ruti /auth/login, za svaki drugi tip HTTP metode greska je 401 Unauthorized
		return (web) -> web.ignoring().requestMatchers(HttpMethod.POST, "/api/user/login").requestMatchers(HttpMethod.POST, "/api/user");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
