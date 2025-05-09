package com.larabia.springSecurityCourse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.RequiredArgsConstructor;

/**
 * Configuración de seguridad de Spring Security.
 * 
 * - `@Configuration`: Indica que esta clase es una clase de configuración de Spring.
 * - `@EnableWebSecurity`: Habilita la seguridad web y permite personalizar la configuración.
 * - `@EnableMethodSecurity`: Habilita la seguridad a nivel de métodos, permitiendo el uso de anotaciones como `@PreAuthorize`.
 * - `@RequiredArgsConstructor`: Genera un constructor con los atributos marcados como `final`, facilitando la inyección de dependencias.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	// Filtro para manejar la autenticación basada en JWT.
	@Autowired
	private JwtFilter jwtFilter;
	
	// Proveedor de autenticación que maneja la validación de credenciales.
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Autowired
	private CustomAuthenticationEntryPoint authenticationEntryPoint;



    /**
     * Configura la cadena de filtros de seguridad (SecurityFilterChain).
     * 
     * @param httpSecurity Objeto `HttpSecurity` para definir las reglas de seguridad.
     * @return Una instancia de `SecurityFilterChain` con la configuración definida.
     * @throws Exception Si hay errores en la configuración de seguridad.
     */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.csrf(csrf -> csrf.disable())// Desactiva la protección CSRF, útil en APIs REST donde no se usa sesión.
				.authorizeHttpRequests(auth -> auth  // Define reglas de autorización para las solicitudes HTTP.
					    .requestMatchers("/api/greeting/sayHelloAdmin/**").hasRole("ADMIN") // solo visible para usuarios admin
						.requestMatchers(publicEndpoints()).permitAll()// Solicitudes que NO requieren autenticación.
						.anyRequest().authenticated())// Todas las demás solicitudes requieren autenticación.
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// Configura la gestión de sesiones como STATELESS (sin estado).
	            																					   // Esto es clave en autenticaciones con JWT, ya que no se almacenan sesiones en el servidor.
				.authenticationProvider(authenticationProvider)// Establece el proveedor de autenticación definido en la configuración.
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)// Agrega el filtro JWT antes del filtro `UsernamePasswordAuthenticationFilter`.
        .exceptionHandling(exception -> exception //Maneja excepciones del filter chain generadas en la validacion de tokens
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint));																			
		// Construye y retorna la cadena de seguridad configurada.
		return httpSecurity.build();
		
	}
	
	private RequestMatcher publicEndpoints() {
		return new OrRequestMatcher(
				new AntPathRequestMatcher("/api/greeting/sayHelloPublic"), 
				new AntPathRequestMatcher("/api/auth/**"));
	}
	
	

	
	
}
