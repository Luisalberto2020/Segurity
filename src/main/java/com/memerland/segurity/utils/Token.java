package com.memerland.segurity.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.memerland.segurity.Errors.TokenException;
import com.memerland.segurity.Segurity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@AllArgsConstructor
@Builder
public class Token {
    private static final String SECRET = System.getenv("SECRET_TOKEN");
    private static final String HEADER_STRING = "Token";

    private String sub;
    private String name;
    private boolean admin;
    private LocalDateTime expirationDate;



    public String toStringFormat() {
        return  JWT.create()
                .withSubject(sub)
                .withClaim("name", name)
                .withClaim("admin", admin)
                .withExpiresAt(expirationDate.toInstant(ZoneOffset.UTC))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }



    public static Token getToken(String token) throws TokenException {
        boolean validToken = false;
        DecodedJWT jwt = null;
        try {
            // Verificar el token usando la secret key
             jwt = JWT.require(Algorithm.HMAC512(SECRET))
                    .build()
                    .verify(token);
            if (jwt.getExpiresAt().toInstant().isAfter(LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC))) {
                validToken = true;
            }

            Segurity.instance.getLogger().info("Tken primitivo" + token);


        } catch (Exception e) {
            throw new TokenException("Token invalido" + e.getMessage());

        }
       if (validToken) {

           return new Token(jwt.getSubject(), jwt.getClaim("name").asString(),jwt.getClaim("admin").asBoolean(),
                   jwt.getExpiresAt().toInstant().atZone(java.time.ZoneOffset.UTC).toLocalDateTime());


       }else {
              throw new TokenException("Token invalido");
       }

    }

}
