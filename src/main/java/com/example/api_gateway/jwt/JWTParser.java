package com.example.api_gateway.jwt;

import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.api_gateway.jwt.claims.JWTClaims;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTParser {

    private SecretKey secretKey;

    public JWTParser(@Value("${jwt.base64url.encoded.secretKey}") String base64URLencodedString) {
        setSecretKey(base64URLencodedString);
    }

    public Optional<JWTClaims> parseToken(String jwt) throws IllegalArgumentException {

        if (jwt == null || jwt.isEmpty()) {
            return Optional.empty();
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            JWTClaims jwtClaims = new JWTClaims();

            jwtClaims.setUsername((String) claims.get("username"));
            jwtClaims.setProfilePicture((String) claims.get("profilePicture"));
            jwtClaims.setSub(claims.getSubject());
            jwtClaims.setExp(claims.getExpiration().getTime());
            jwtClaims.setIat(claims.getIssuedAt().getTime());
            jwtClaims.setIss(claims.getIssuer());
            jwtClaims.setJti(UUID.fromString(claims.getId()));

            return Optional.of(jwtClaims);
        } catch (JwtException e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }

    public void setSecretKey(String base64URLencodedString) {

        byte[] decoded = Decoders.BASE64URL.decode(base64URLencodedString);

        this.secretKey = Keys.hmacShaKeyFor(decoded);

    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}
