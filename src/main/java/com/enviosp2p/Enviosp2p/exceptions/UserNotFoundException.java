package com.enviosp2p.Enviosp2p.exceptions;

// UserNotFoundException.java
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String mensaje) {
        super(mensaje);
    }
}