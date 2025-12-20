package com.enviosp2p.Enviosp2p.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String mensaje) {
        super(mensaje);
    }
}