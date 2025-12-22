package com.enviosp2p.Enviosp2p.utils;

import java.security.SecureRandom;

public class GeneradorCodigo {

    // Definimos los caracteres permitidos (Mayúsculas y Números para mejor legibilidad)
    // Puedes agregar "abcdefghijklmnopqrstuvwxyz" si quieres minúsculas también.
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // SecureRandom es más seguro que Random para tokens y claves
    private static final SecureRandom RANDOM = new SecureRandom();

    // Constructor privado para evitar que alguien haga "new GeneradorCodigo()"
    // Es una clase de utilidad estática.
    private GeneradorCodigo() {}

    /**
     * Genera un string alfanumérico aleatorio.
     * @param longitud La cantidad de caracteres que necesitas.
     * @return El código generado (ej: "A1B2C3")
     */
    public static String generar(int longitud) {
        StringBuilder sb = new StringBuilder(longitud);

        for (int i = 0; i < longitud; i++) {
            // Elegimos un índice aleatorio
            int index = RANDOM.nextInt(CARACTERES.length());
            // Agregamos el caracter correspondiente
            sb.append(CARACTERES.charAt(index));
        }

        return sb.toString();
    }
}