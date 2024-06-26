package com.example.api_gateway.filter;

import java.util.Optional;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.api_gateway.jwt.JWTParser;
import com.example.api_gateway.jwt.claims.JWTClaims;

@Component
public class AuthenticationGateway extends AbstractGatewayFilterFactory<AuthenticationGateway.Config> {

    private final JWTParser jwtParser;

    public AuthenticationGateway(JWTParser jwtParser) {
        super(Config.class);
        this.jwtParser = jwtParser;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            Optional<JWTClaims> optionalJWT = jwtParser.parseToken(token);

            if (optionalJWT.isEmpty()) {

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            JWTClaims claims = optionalJWT.get();

            exchange = exchange.mutate()
                    .request(r -> r.header("x-jwt-username", claims.getUsername()))
                    .request(r -> r.header("x-jwt-userId", claims.getSub()))
                    .request(r -> r.header("x-jwt-profilePicture", claims.getProfilePicture()))
                    .build();

            return chain.filter(exchange);

        };
    }

    public static class Config {
    }

}
