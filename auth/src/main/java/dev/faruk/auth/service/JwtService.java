package dev.faruk.auth.service;

import dev.faruk.commoncodebase.error.AppHttpError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtService is the service that is used to generate and validate JWT tokens.
 */
@Component
public class JwtService {
    private static final long EXPIRATION_TIME_IN_MILLIS = 1000 * 60 * 30;

    private final JwtSecretService jwtSecretService;

    @Autowired
    public JwtService(JwtSecretService jwtSecretService) {
        this.jwtSecretService = jwtSecretService;
    }

    /**
     * Get the username from the given token
     * @param token the token to get the username from
     * @return the username from the given token
     */
    public String getUsernameByToken(final String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(jwtSecretService.getSignKey()).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw new AppHttpError.Unauthorized("Token expired");
        } catch (JwtException e) {
            throw new AppHttpError.Unauthorized("Invalid token");
        }
    }

    /**
     * Generate a token for the given username. The token will be valid for 30 minutes.
     * @param userName the username to be included in the token
     * @return the generated token
     */
    public String generateToken(final String userName) {
        Map<String, Object> claims = new HashMap<>();
        return _createToken(claims, userName);
    }

    /**
     * Generate a token for the given username and roles. The token will be valid for 30 minutes.
     * @param claims the claims to be included in the token
     * @param userName the username to be included in the token
     * @return the generated token
     */
    private String _createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLIS))
                .signWith(jwtSecretService.getSignKey(), SignatureAlgorithm.HS256).compact();
    }

}