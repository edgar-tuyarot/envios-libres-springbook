package com.enviosp2p.Enviosp2p.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException; // OJO: Importar la de Spring Security
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejar el error de permisos (El famoso 403 que sufriste recién)
    // Esto captura cuando @PreAuthorize bloquea a alguien.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> manejarAccessDenied(AccessDeniedException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .codigo("AUTH-403")
                .mensaje("No tienes permisos para acceder a este recurso. Contacta al admin.")
                .fecha(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 2. Manejar rutas inexistentes
    // En Spring Boot 3, esto captura cuando escribes mal la URL.
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRutaNoEncontrada(NoResourceFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .codigo("RUTA-404")
                .mensaje("La URL que intentas consultar no existe: /" + ex.getResourcePath())
                .fecha(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 3. Manejar cualquier otro error no previsto (El "Catch-All")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorGeneral(Exception ex) {
        // Aquí podrías agregar un log para ti
        ex.printStackTrace();

        ErrorResponse error = ErrorResponse.builder()
                .codigo("INTERNAL-500")
                .mensaje("Ocurrió un error inesperado en el servidor. Intenta más tarde.")
                .detalles(ex.getMessage()) // Cuidado: en producción, evita mostrar esto si es sensible
                .fecha(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarUserNotFound(UsernameNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .codigo("AUTH-401")
                .detalles("Credenciales inválidas")
                .mensaje(ex.getMessage())
                .fecha(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> manejarBadCredentials(BadCredentialsException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .codigo("AUTH-401")
                .detalles("Credenciales inválidas")
                .mensaje(ex.getMessage())
                .fecha(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> manejoDeToken(InvalidTokenException ex){
        ErrorResponse error = ErrorResponse.builder()
                .codigo("AUTH-400")
                .detalles("Token Invalido")
                .mensaje("Token Invalido")
                .fecha(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

}