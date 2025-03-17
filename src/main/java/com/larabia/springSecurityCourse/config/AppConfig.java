package com.larabia.springSecurityCourse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.larabia.springSecurityCourse.repository.UserRepository;

import lombok.RequiredArgsConstructor;


/**
 * Configuración de Spring Security para la autenticación de usuarios.
 * 
 * Esta clase define los beans necesarios para la autenticación:
 * - UserDetailsService: Carga los detalles de un usuario desde la base de datos.
 * - AuthenticationProvider: Gestiona la autenticación usando UserDetailsService y un codificador de contraseñas.
 * - PasswordEncoder: Define el mecanismo de codificación de contraseñas (BCrypt).
 * 
 * Se usa la anotación @Configuration para que Spring la detecte como una clase de configuración.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {
	
	private final UserRepository userRepository;
	
	
	
	public AppConfig(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}


    /**
     * Método que devuelve una implementación de UserDetailsService, 
     * que es utilizada por Spring Security para cargar los detalles de un usuario basado en su correo electrónico.
     *
     * @return Un objeto UserDetailsService que utiliza el repositorio de usuarios
     *         para encontrar un usuario por su correo electrónico.
     */
	@Bean //La anotación @Bean le indica a Spring que el método userDetailsService() proporciona un objeto que debe ser registrado en el contenedor de Spring como un bean. Esto permite que Spring gestione la creación y ciclo de vida del objeto y lo inyecte en cualquier lugar donde se requiera el tipo UserDetailsService.
	public UserDetailsService userDetailsService() {
		
        // Retorna una implementación de UserDetailsService que busca al usuario 
        // en el repositorio utilizando su correo electrónico. Si no se encuentra el usuario, 
        // se lanza una excepción UsernameNotFoundException.
		return username -> userRepository.findUserByEmail(username)
				.orElseThrow(()-> new UsernameNotFoundException("User not found"));
			
		}
	
	
    /**
     * Define un bean de tipo AuthenticationProvider.
     * 
     * El AuthenticationProvider se encarga de autenticar a los usuarios utilizando:
     * - Un UserDetailsService para obtener los detalles del usuario.
     * - Un PasswordEncoder para verificar la contraseña.
     *
     * @return Un AuthenticationProvider configurado con UserDetailsService y PasswordEncoder.
     */
	@Bean 
	public AuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		
        // Configura el proveedor de autenticación con el servicio de usuarios y el codificador de contraseñas.
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;
		
	}

    /**
     * Define un bean de tipo PasswordEncoder.
     * 
     * Se usa BCrypt para codificar las contraseñas antes de almacenarlas en la base de datos.
     * Este mismo codificador se usa luego para verificar la contraseña introducida por el usuario en el login.
     *
     * @return Un PasswordEncoder basado en BCrypt.
     */
	@Bean 
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}
				
		

}
