package com.example.api_gateway.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.api_gateway.jwt.claims.JWTClaims;

import io.jsonwebtoken.Jwts;

@ExtendWith(MockitoExtension.class)
public class JWTParserTest {

    private JWTParser jwtParser;

    private String base64URLEncoded = "SEVMTE9USEVSRUhPV1lPVURPSU5HVE9EQVlJU0VWRVJZVEhJTkdPS0FZ";

    @BeforeEach
    void beforeEach() {
        jwtParser = new JWTParser(base64URLEncoded);
    }

    @Test
    void shouldHandleNullJWT() {
        Optional<JWTClaims> result = jwtParser.parseToken(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleEmptyJWT() {
        Optional<JWTClaims> result = jwtParser.parseToken("");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleJWTNotStartsWithBearer() {
        Optional<JWTClaims> result = jwtParser.parseToken("Baearer ");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldParseJWT() {

        String singed = Jwts.builder()
                .signWith(jwtParser.getSecretKey())
                .claim("sub", "walter")
                .expiration(new Date(System.currentTimeMillis() + 5000))
                .issuedAt(new Date())
                .issuer("http://")
                .id(UUID.randomUUID().toString())
                .compact();

        Optional<JWTClaims> jwtOptional = jwtParser.parseToken(singed);

        assertTrue(jwtOptional.isPresent());

        assertEquals("walter", jwtOptional.get().getSub());
    }
}
