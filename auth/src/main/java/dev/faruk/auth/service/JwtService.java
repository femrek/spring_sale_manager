package dev.faruk.auth.service;

import dev.faruk.auth.constant.AuthConstants;
import dev.faruk.commoncodebase.error.AppHttpError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtService is the service that is used to generate and validate JWT tokens.
 */
@Log4j2
@Component
public class JwtService {
    private final AuthConstants authConstants;

    @Autowired
    public JwtService(AuthConstants authConstants) {
        this.authConstants = authConstants;
    }

    /**
     * Get the username from the given token
     * @param token the token to get the username from
     * @return the username from the given token
     */
    public String getUsernameByToken(final String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(authConstants.getSignKey()).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw new AppHttpError.Unauthorized("Token expired");
        } catch (JwtException e) {
            log.debug("invalid token when extracting the username from token.", e);
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
                .setExpiration(new Date(System.currentTimeMillis() + authConstants.getExpirationTimeInMillis()))
                .signWith(authConstants.getSignKey(), SignatureAlgorithm.HS256).compact();
    }

}