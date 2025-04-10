# 🛡️ Spring Security Course - Proyecto Final

Este proyecto es una aplicación RESTful desarrollada como parte de un curso de Spring Security. Implementa autenticación y autorización con JWT, integración con PostgreSQL y manejo de usuarios básicos.

---

## 🧠 Descripción

La aplicación permite registrar y autenticar usuarios, y acceder a endpoints protegidos mediante tokens JWT. Es ideal como base para cualquier proyecto que requiera seguridad básica con Spring Boot.

---

## 🧰 Tecnologías utilizadas

- Java 17  
- Spring Boot 3.4.3  
- Spring Security  
- JWT (jjwt 0.11.5)  
- PostgreSQL  
- Maven  
- Eclipse (IDE)  
- DBeaver (cliente de base de datos)  
- Postman (para testeo de endpoints)

---

## 📁 Estructura del proyecto
```plaintext
src/ 
└── main/ 
├── java/ 
│     └── com.larabia.springSecurityCourse 
│           ├── config → Configuración de seguridad, filtros, JWT 
│           ├── controller → Endpoints públicos y protegidos 
│           ├── entity → Entidades JPA 
│           ├── repository → Repositorio de usuarios 
│           └── service → Lógica de autenticación 
└── resources/ 
      └── application.properties → Configuración general
```

---

## ⚙️ Configuración del entorno

### 📄 Archivo `application.properties`

```properties
spring.application.name=springSecurityCourse

spring.datasource.url=jdbc:postgresql://localhost:5432/SpringSecurityDB
spring.datasource.username=sa
spring.datasource.password=123456

spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.platform=postgres

server.port=8085
```

## 🛢️ Configuración de base de datos

La base debe crearse previamente en PostgreSQL.
1. Conectate como usuario postgres en DBeaver.
2. Ejecutá este script:
```plaintext
CREATE DATABASE "SpringSecurityDB";

CREATE USER sa WITH PASSWORD '123456';
GRANT ALL PRIVILEGES ON DATABASE "SpringSecurityDB" TO sa;
```


## 🚀 Cómo ejecutar el proyecto

1. Cloná o descargá el proyecto.
2. Importalo en **Eclipse** como proyecto **Maven**.
3. Asegurate de tener creada la base de datos `SpringSecurityDB` en PostgreSQL.
4. Clic derecho sobre la clase `SpringSecurityCourseApplication.java`  
   → **Run As > Java Application**
5. La aplicación estará corriendo en:  
   [http://localhost:8085](http://localhost:8085)

---

## 📦 Dependencias principales (`pom.xml`)

- `spring-boot-starter-web`: API REST
- `spring-boot-starter-data-jpa`: integración con base de datos
- `spring-boot-starter-security`: autenticación y autorización
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: manejo de tokens JWT
- `postgresql`: base de datos relacional
- `lombok`: generación automática de getters/setters
- `devtools`: recarga automática en desarrollo

## 📂 Controladores (`/controller`)

### 📍 `AuthController.java`

Responsable de manejar los endpoints de autenticación:

| Endpoint      | Método | Descripción                                          |
|---------------|--------|------------------------------------------------------|
| `/register`   | POST   | Registra un nuevo usuario y devuelve un token JWT    |
| `/autenticate`| POST   | Autentica credenciales y genera un token JWT         |

---

### 📍 `GreetingController.java`

Endpoints de prueba para verificar acceso público y protegido:

| Endpoint              | Método | Acceso     | Descripción                           |
|-----------------------|--------|------------|---------------------------------------|
| `/sayHelloPublic`     | GET    | Público    | Devuelve un saludo sin autenticación  |
| `/sayHelloProtected`  | GET    | Protegido  | Requiere JWT en el header             |

---

## 🧠 Servicios (`/service`)

### 📍 `AuthServiceImpl.java`

| Método         | Descripción                                               |
|----------------|-----------------------------------------------------------|
| `register()`   | Guarda un nuevo usuario y devuelve un token JWT.          |
| `autenticate()`| Verifica las credenciales y devuelve un token JWT.        |

**Usa los siguientes componentes:**

- `PasswordEncoder` (BCrypt)
- `AuthenticationManager`
- `JwtService`
- `UserRepository`

---

## 🔐 Configuración de seguridad (`/config`)

### 📍 `SecurityConfig.java`

- Define qué rutas son públicas y cuáles requieren autenticación.
- Desactiva CSRF.
- Usa `JwtFilter` para proteger rutas.
- Aplica política de sesión **stateless** (sin sesiones).

### 📍 `JwtFilter.java`

Filtro que intercepta cada request:

- Extrae y valida el token JWT.
- Carga el usuario desde la base.
- Inyecta la autenticación en el contexto de seguridad.

### 📍 `JwtService.java`

| Método                      | Descripción                      |
|-----------------------------|----------------------------------|
| `generateToken(user)`       | Genera el token JWT              |
| `getUserName(token)`        | Extrae el email del token        |
| `validateToken(token, user)`| Verifica firma y validez del JWT |

**Características:**

- Firma: HS256 con clave secreta  
- Expiración: 24 horas

### 📍 `AppConfig.java`

Define los beans necesarios para autenticación:

- `UserDetailsService`
- `PasswordEncoder`
- `AuthenticationProvider`
- `AuthenticationManager`

---

## 🧩 Entidades y Repositorio

### 📍 `User.java`

Entidad `tbl_user` que implementa `UserDetails`.

Campos:
- `id`
- `firstName`
- `lastName`
- `email`
- `password`
- `role`

---

### 📍 `Role.java`

```java
public enum Role {
    USER, ADMIN
}
```
## 📌 Endpoints principales

| Método | Endpoint                         | Autenticación | Descripción                     |
|--------|----------------------------------|----------------|---------------------------------|
| POST   | `/api/auth/register`            | ❌ No          | Registra un nuevo usuario       |
| POST   | `/api/auth/autenticate`         | ❌ No          | Login, devuelve token JWT       |
| GET    | `/api/greeting/sayHelloPublic`  | ❌ No          | Saludo público                  |
| GET    | `/api/greeting/sayHelloProtected` | ✅ Sí         | Requiere JWT válido             |

---

## 📝 Notas útiles

- El token debe enviarse en el header como:  
  `Authorization: Bearer <tu-token>`

- Los tokens son válidos por **24 horas**.

- Si cambiás la clave JWT (`SECRET_KEY`), los tokens anteriores dejan de funcionar.

- Las tablas se generan automáticamente si tenés configurado:  
  `spring.jpa.hibernate.ddl-auto=update`

---

## 👀 Comentarios en el código

El proyecto contiene **comentarios explicativos** en los puntos clave del flujo:

- Métodos de seguridad y lógica de autenticación
- Configuración de filtros, servicios y entidades

👉 Ideal para repasar cómo funciona la autenticación JWT con Spring Security.

---

## 🧠 Créditos

Basado en el curso de Spring Security de JAAX https://www.youtube.com/watch?v=KBvBY5qyfEM.

Desarrollado como práctica personal por Larabia.
