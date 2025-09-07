package com.br.gasto_comum.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.br.gasto_comum.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String SECRET_KEY;
    @Value("${api.security.token.issuer}")
    private static String ISSUER;
    @Value("${api.security.token.expiration}")
    private int TOKEN_EXPIRATION;
    @Value("${api.security.token.refresh-expiration}")
    private int REFRESHTOKEN_EXPIRATION;

    public String generateToken(String username) {
        try {
            // Define o algoritmo HMAC SHA256 para criar a assinatura do token passando a chave secreta definida
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER) // Define o emissor do token
                    .withIssuedAt(creationDate()) // Define a data de emissão do token
                    .withExpiresAt(expirationDate(TOKEN_EXPIRATION)) // Define a data de expiração do token
                    .withSubject(username) // Define o assunto do token (neste caso, o nome de usuário)
                    .sign(algorithm); // Assina o token usando o algoritmo especificado
        } catch (JWTCreationException exception){
            throw new JWTCreationException("Erro ao gerar token.", exception);
        }
    }

    public String generateRefreshToken(String username) {
        try {
            // Define o algoritmo HMAC SHA256 para criar a assinatura do token passando a chave secreta definida
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER) // Define o emissor do token
                    .withIssuedAt(creationDate()) // Define a data de emissão do token
                    .withExpiresAt(expirationDate(REFRESHTOKEN_EXPIRATION)) // Define a data de expiração do token
                    .withSubject(username) // Define o assunto do token (neste caso, o nome de usuário)
                    .sign(algorithm); // Assina o token usando o algoritmo especificado
        } catch (JWTCreationException exception){
            throw new JWTCreationException("Erro ao gerar token.", exception);
        }
    }

    public String getSubject(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (Exception e){
            throw new RuntimeException("Token inválido ou expirado", e);
        }
    }

    private Instant creationDate() {
        return ZonedDateTime.now(ZoneOffset.of("-03:00")).toInstant();
    }

    private Instant expirationDate(int seconds) {
        return LocalDateTime.now().plusSeconds(seconds).toInstant(ZoneOffset.of("-03:00"));
    }

}
