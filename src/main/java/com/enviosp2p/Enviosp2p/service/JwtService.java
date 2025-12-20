package com.enviosp2p.Enviosp2p.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // 🔐 LA LLAVE MAESTRA
    // Importante: Esta clave debe ser secreta y larga (mínimo 256 bits).
    // En producción, esto NUNCA se pone aquí hardcodeado, se lee de application.properties.
    // Puedes generar una online buscando "HMAC SHA256 Key Generator".
    private static final String SECRET_KEY = "MI_CLAVE_SUPER_SECRETA_QUE_DEBE_SER_MUY_LARGA_PARA_QUE_SEA_SEGURA_123456";

    // 1. GENERAR TOKEN (Versión simple)
    // Este es el metodo que llamarás desde tu AuthService.
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // 2. GENERAR TOKEN (Versión con datos extra)
    // Por si quieres meter el ID del usuario o su rol dentro del token.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // Aquí guardamos el correo
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación: AHORA
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira en 24 horas
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Firmamos digitalmente
                .compact();
    }

    // 3. VALIDAR TOKEN
    // Verifica dos cosas:
    // a) Que el correo del token coincida con el usuario.
    // b) Que el token no haya expirado.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 4. EXTRAER EL USUARIO (CORREO)
    // Sirve para saber de quién es este token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // --- MÉTODOS AUXILIARES (Los engranajes internos) ---

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        // Decodificamos la clave secreta que está en Base64
        byte[] keyBytes = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}