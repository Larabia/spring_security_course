package com.larabia.springSecurityCourse.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.larabia.springSecurityCourse.config.JwtService;
import com.larabia.springSecurityCourse.controller.models.AuthResponse;
import com.larabia.springSecurityCourse.controller.models.AuthenticationRequest;
import com.larabia.springSecurityCourse.controller.models.RegisterRequest;
import com.larabia.springSecurityCourse.entity.Role;
import com.larabia.springSecurityCourse.entity.User;
import com.larabia.springSecurityCourse.exeption.EmailAlreadyExistsException;
import com.larabia.springSecurityCourse.exeption.UserNotFoundException;
import com.larabia.springSecurityCourse.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio que implementa la lógica de autenticación y registro de usuarios.
 * 
 * Esta clase utiliza varios componentes de Spring Security y JWT para manejar:
 * - El registro de nuevos usuarios (incluye guardar en base de datos y generar token).
 * - La autenticación de usuarios existentes (verifica credenciales y genera token).
 * 
 * Se anotó con @Service para que Spring la detecte como un componente de servicio,
 * y con @RequiredArgsConstructor para generar automáticamente el constructor con dependencias inyectadas.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	// Repositorio para acceder a los datos de usuarios en la base de datos.
	private final UserRepository userRepository;
	// Codificador de contraseñas, se usa para encriptar antes de guardar y para validar al autenticar (Bcrypt) (que lo demuestre).
	private final PasswordEncoder passwordEncoder;
	// Servicio encargado de generar tokens JWT.
	private final JwtService jwtService;
	// Componente de Spring Security que maneja la autenticación de usuarios.
	private final AuthenticationManager authenticationManager;

	
	/**
	 * Registra un nuevo usuario en el sistema.
	 * 
	 * 1. Construye un objeto User a partir de los datos recibidos.
	 * 2. Encripta la contraseña antes de guardarla.
	 * 3. Asigna por defecto el rol USER.
	 * 4. Guarda el nuevo usuario en la base de datos.
	 * 5. Genera un token JWT asociado al usuario.
	 *
	 * @param request Datos del nuevo usuario (nombre, apellido, email, contraseña).
	 * @return Una respuesta con el token JWT generado para el nuevo usuario.
	 */
	@Override
	public AuthResponse register(RegisterRequest request) {
		
		//Valida que el mail no haya sido registrado previamente
	    if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
	        throw new EmailAlreadyExistsException("Ya existe un usuario con el email: " + request.getEmail());
	    }
	    
		var user = User.builder()
			    .firstName(request.getFirstName())
			    .lastName(request.getLastName())
			    .email(request.getEmail())
			    .password(passwordEncoder.encode(request.getPassword()))// encripta la contraseña
			    .roles(Set.of(Role.USER)) // asigna rol USER por defecto
			    .build();
		
		userRepository.save(user);// guarda el usuario en la base de datos
		
		// genera el token JWT usando los datos del usuario
		var jwtToken = jwtService.generateToken(user); //Aca siempre implementamos el UserDetails
		
		// devuelve el token en la respuesta
		return AuthResponse.builder()
				.token(jwtToken)
				.build();
	}

	
	/**
	 * Autentica un usuario existente en el sistema.
	 * 
	 * 1. Usa el AuthenticationManager para verificar que el email y la contraseña sean válidos.
	 * 2. Busca el usuario por email en la base de datos.
	 * 3. Genera un token JWT si la autenticación fue exitosa.
	 *
	 * @param request Contiene el email y la contraseña del usuario que quiere iniciar sesión.
	 * @return Una respuesta con el token JWT generado si la autenticación es exitosa.
	 */
	@Override
	public AuthResponse autenticate(AuthenticationRequest request) {
		
		// Verifica las credenciales del usuario. Si son incorrectas, lanza excepción automáticamente.
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		// Busca al usuario en la base de datos. Se usa el email desde el request.
		var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("No se encontró un usuario con el email: " + request.getEmail()));
		
		// Genera el token JWT usando los datos del usuario autenticado y que se devolvio de la base de datos (nunca del request no es seguro) 
		var jwtToken = jwtService.generateToken(user);
		
		// Devuelve el token en la respuesta

		return AuthResponse.builder()
				.token(jwtToken)
				.build();
	}

}
