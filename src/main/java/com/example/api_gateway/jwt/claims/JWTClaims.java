package com.example.api_gateway.jwt.claims;

import java.util.UUID;

import lombok.Data;

@Data
public class JWTClaims {

    private String sub;
    private String username;
    private String profilePicture;
    private String iss;
    private long exp;
    private long iat;
    private UUID jti;

}
