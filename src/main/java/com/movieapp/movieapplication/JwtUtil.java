package com.movieapp.movieapplication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "SmqxmZOGlUFllgpEsKMdUBQuUGgGYOAZgfpghIqtObsqvnpIXGJDzTYlOYzdvMXLyiFdauCRSwxtJXaRgjipyZRRvaDajVPqIKdmPPVpXcLbNtjZugabHaJVJnltoxFmvjIabDtOxZCkPeQnwLkBAVoVfKlCFiMNZWGtRKYqkcLFypCFYECjsIDAdpEcHhnBNbwfXavkmUjcBSzMeLiTuNsLLeBxETJMLHPDmHTZSimABXbUyZaQFjTwmqMmwdZF";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 godziny w milisekundach
//    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Bezpieczny klucz generowany automatycznie

    public String generateToken(String userId, String username, String role, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("username: ", username);
        System.out.println("Username" + username);
        claims.put("email", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()
                        + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256,
                        SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);

            Claims body = claims.getBody();
            Date expirationDate = body.getExpiration();
            if (expirationDate.before(new Date())) {
                throw new RuntimeException("Token wygasł");
            }
            System.out.println("Token jest poprawny. Nazwa użytkownika: " + body.getSubject());
            String role = extractRole(token); // Pobranie roli z tokena
            System.out.println("Rola użytkownika to: " + role);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException("Niepoprawny token", e);
        }
    }

}
