package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.service.UsuarioService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private AuthEntryPoint authEntryPoint;
	
	@Autowired
	private RequestFilter requestFilter;
	
    @Bean
    UsuarioService userDetailsService() {
		return new UsuarioService();
	}
    
	// Método que nos suministrará la codificación
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Método que autentifica
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	 @Bean
	 AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	    return authConfig.getAuthenticationManager();
	  }
	
	
	// Aquí es donde podemos especificar qué es lo que hace y lo que no
	// según el rol del usuario 
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(authEntryPoint))
	        .authorizeHttpRequests((requests) -> {
			requests

				.requestMatchers("/api/usuario/signin", "/api/usuario/registro" ).permitAll()
				.requestMatchers("media/**").authenticated()
				.requestMatchers(HttpMethod.POST,"/api/reservas/reservar/**").authenticated()
				.requestMatchers(HttpMethod.POST,"/api/reservas/devolver/**").authenticated()
				.requestMatchers(HttpMethod.GET,"/api/reservas").authenticated()
				.requestMatchers("/api/reservas/**").authenticated()
				.requestMatchers(HttpMethod.GET, "api/libros").authenticated()
				.requestMatchers(HttpMethod.GET, "/api/usuario").authenticated() 
				.requestMatchers(HttpMethod.GET, "api/libros/titulo/**").authenticated()
				.requestMatchers(HttpMethod.DELETE, "/api/usuario/**").hasAuthority("ROLE_ADMIN")
				.requestMatchers(HttpMethod.POST, "api/libros/**").hasAuthority("ROLE_ADMIN")
				.anyRequest().denyAll();
	        })
	        .formLogin((form) -> form.permitAll())
	        .logout((logout) -> logout.permitAll().logoutSuccessUrl("/"));
		
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
