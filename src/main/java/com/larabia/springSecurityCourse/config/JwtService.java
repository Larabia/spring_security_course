package com.larabia.springSecurityCourse.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
    // Secret Key utilizada para firmar y verificar los tokens JWT.
    // En un entorno productivo, la clave no debería estar hardcodeada aquí.
    // En su lugar, se recomienda almacenarla en un gestor de secretos (por ejemplo, AWS Secrets Manager, HashiCorp Vault, etc.).
	private static final String SECRET_KEY = "01234527aa08e384029f7dcd740e80cc7555f904e322b9edbd3c9d3ca78f97c5";
	
	
	
	//METODOS PARA GENERARLE UN TOKEN AL CLIENTE
    /**
     * Genera un token JWT utilizando la información del usuario proporcionada.
     * Este método llama internamente al método {@link #generateToken(Map, UserDetails)} 
     * con un mapa vacío de claims adicionales.
     *
     * @param userDetails   Información del usuario para establecer el subject del token.
     * @return              Un token JWT firmado y listo para ser utilizado.
     */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> extraClaims = new HashMap<>();
	    extraClaims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
	    return generateToken(extraClaims, userDetails);
		
	}
	
	/**
	 * Genera un token JWT con los claims adicionales proporcionados y la información del usuario.
	 *
	 * @param extraClaims   Un mapa de claims adicionales que se incluirán en el token.
	 * @param userDetails   Información del usuario para establecer el subject del token.
	 * @return              Un token JWT firmado y listo para ser utilizado.
	 */
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {//Map<String, Claims> no es válido. De hecho, Claims es lo que devolvés cuando decodificás un JWT, no lo que le pasás al construirlo.
	
		return Jwts.builder().setClaims(extraClaims)// Agrega los claims adicionales proporcionados en el mapa extraClaims.
				.setSubject(userDetails.getUsername())// Establece el "subject" del token como el nombre de usuario del usuario autenticado.
				.setIssuedAt(new Date(System.currentTimeMillis()))// Define la fecha de emisión del token como la hora actual del sistema.
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Define la fecha de expiración del token (24 horas después de la emisión).
				.signWith(getSingInKey(), SignatureAlgorithm.HS256) // Firma el token con la clave secreta usando el algoritmo HS256.
				.compact();
				
	}

	
	
    /**
     * Obtiene todos los claims presentes en el token JWT.
     * Este método decodifica el token utilizando la clave secreta configurada.
     * 
     * @param token El token JWT del cual extraer los claims.
     * @return Los claims presentes en el token.
     */
	private Claims getAllClaims(String token) {//La clase Claims en jjwt (Java JWT) representa el payload de un JSON Web Token (JWT). Es una subinterfaz de Map<String, Object>, lo que significa que almacena los datos del token en forma de clave-valor.
		
		//La clase Jwts pertenece a la biblioteca Java JWT (jjwt) y se utiliza para crear, analizar y validar JSON Web Tokens (JWTs)
		return Jwts
				.parserBuilder()
				.setSigningKey(getSingInKey())// Configura la clave para verificar la firma del token.
				.build()
				.parseClaimsJws(token)// Decodifica y verifica el token.
				.getBody();// Obtiene el payload del token.
	}
	
	// METODO QUE CREA UNA KEY HMAC-SHA PARA VERIFICAR LA FIRMA DEL TOKEN

    /**
     * Convierte la clave secreta en un objeto `Key` adecuado para firmar y verificar JWTs.
     * La clave se decodifica desde Base64 y se utiliza para generar una clave HMAC-SHA.
     * 
     * @return La clave utilizada para firmar/verificar tokens.
     */
	private Key getSingInKey() { //La clase Key en Java pertenece al paquete java.security y representa una clave criptográfica utilizada para firmar y verificar tokens JWT, cifrar y descifrar datos, o realizar otras operaciones criptográficas.
		
		// Decodifica la clave secreta desde Base64.
		byte[] keyBites = Decoders.BASE64.decode(SECRET_KEY);
		
        // Genera la clave HMAC-SHA utilizando la clave decodificada.
		return Keys.hmacShaKeyFor(keyBites);
	}
	
	// METODOS QUE LA VALIDAN QUE EL NOMBRE DE USUARIO SEA EL CORRECTO Y QUE EL TOKEN NO HAYA EXPIRADO

    /**
     * Valida un token JWT verificando que el nombre de usuario coincida y que el token no haya expirado.
     * 
     * @param token El token JWT a validar.
     * @param userDetails Los detalles del usuario autenticado.
     * @return `true` si el token es válido, `false` en caso contrario.
     */
	public boolean validateToken(String token, UserDetails userDetails) {
		
		//Toma el nombre del usuario(mail) del token
		final String username = getUserName(token);
		
		//Verifica que el username del token sea igual al username del UserDetails (verifica que el token corresponda al usuario que lo envia)
		return (username.equals(userDetails.getUsername()) && !isTkenExpired(token));
	}

    /**
     * Verifica si un token JWT ha expirado.
     * 
     * @param token El token JWT a verificar.
     * @return `true` si el token ha expirado, `false` en caso contrario.
     */
	private boolean isTkenExpired(String token) {
		
		return getExpiration(token).before(new Date());
	}

	// METODOS QUE OBTIENEN EL NOMBRE DE USUARIO Y LA FECHA DE EXPIRACION DEL TOKEN
	
    /**
     * Método para obtener el nombre de usuario (subject) del token JWT.
     * 
     * @param token El token JWT del cual extraer el nombre de usuario.
     * @return El nombre de usuario presente en el token.
     */
	public String getUserName(String token) {
		
		//La interfaz Claims me permite tomar el nombre de usuario(mail) del token, requiere dependencias jjwt-api, jjwt-impl y jjwt-jackson
		//jjwt-api (contiene interfaces para trabajar con jwt)
		//jjwt-impl (implementaciones de las interfaces)
		//jjwt-jackson (para la serializacion y serializacion de jwt a json)
		return getClaim(token, Claims::getSubject);
	}
	
    /**
     * Obtiene la fecha de expiración de un token JWT.
     * 
     * @param token El token JWT del cual extraer la fecha de expiración.
     * @return La fecha de expiración del token.
     */
	private Date getExpiration(String token) {
		
		return getClaim(token, Claims::getExpiration);
	}
	
    /**
     * Método genérico para extraer un "claim" específico del token JWT.
     * Los "claims" son los datos almacenados en el payload del token.
     * 
     * @param <T> El tipo de dato del "claim".
     * @param token El token JWT del cual extraer el claim.
     * @param claimsResolver Una función que especifica qué claim extraer.
     * @return El valor del claim extraído.
     */
	public <T> T getClaim(String token, Function <Claims, T> claimsResolver) {

		final Claims claims = getAllClaims(token);// Extrae todos los claims del token.
		return claimsResolver.apply(claims); // Aplica la función especificada (Claims::getSubject y Claims::getExpiration) para obtener el claim deseado.
	}
}
