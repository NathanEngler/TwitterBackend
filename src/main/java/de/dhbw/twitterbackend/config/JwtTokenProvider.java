package de.dhbw.twitterbackend.config;

import de.dhbw.twitterbackend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
// Generiert und stellt Token zur Verfügung
@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "meinGeheimerJWTKeyMeinGeheimerJWTKey";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 Stunden (hier ist einfach eine beliebige Zeit gewählt ohne Hintergedanken)

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String createToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId())) // ID ist subject (
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

