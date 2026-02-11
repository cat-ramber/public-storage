package me.code.publicStorage.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class JWTService {
    private static final Algorithm algorithm=Algorithm.HMAC256(System.getenv("SALT"));
    private static final JWTVerifier jwtVerifier= JWT.require(algorithm).withIssuer("publicStorage").build();

    public String createToken(UUID userId){
     return  JWT.create().withIssuer("publicStorage")
                .withSubject(userId.toString())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS)).sign(algorithm);
    }
    public UUID checkToken(String token){
        DecodedJWT jwt= jwtVerifier.verify(token);
        return UUID.fromString(jwt.getSubject());
    }
}
