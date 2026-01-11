package com.cloud.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.cloud.auth.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {
    @Value("${api.security.token}")
    private String secret;
    @Value("${api.security.issuer}")
    private String issuer;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getUuid().toString())
                    .withClaim("email", user.getEmail())
                    .withClaim("scope", user.getRole().toString())
                    .withClaim("Origin", user.getUserOrigin().toString())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error while creating token!", exception);
        }
    }

    private Instant generateExpirationDate(){
        return Instant.now().plus(Duration.ofMinutes(15));
    }
}
