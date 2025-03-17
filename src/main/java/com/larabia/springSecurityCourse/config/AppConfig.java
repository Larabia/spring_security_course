package com.larabia.springSecurityCourse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.larabia.springSecurityCourse.repository.UserRepository;

import lombok.RequiredArgsConstructor;


/**
 * Configuración de Spring Security para la autenticación de usuarios.
 * Esta clase proporciona un servicio de detalles de usuario (UserDetailsService) 
 * basado en la consulta al repositorio de usuarios (UserRepository) por el correo electrónico.
 * 
 * Se utiliza la anotación @Configuration para indicar que esta clase es una clase de configuración 
 * que puede ser utilizada por el contenedor de Spring.
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
				
		

}
