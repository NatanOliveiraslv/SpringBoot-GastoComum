package com.br.gasto_comum.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieFactory {

    @Value("${app.env}") // dev | prod
    private String env;

    @Value("${api.security.token.refresh-expiration}")
    private long refreshMaxAgeSeconds;

    public ResponseCookie buildRefreshCookie(String token) {
        boolean prod = "prod".equalsIgnoreCase(env);
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(prod)                       // prod: HTTPS; dev: não precisa
                .sameSite(prod ? "None" : "Lax")    // prod: None (cross-site); dev: Lax
                .path("/")                          // cookie enviado em todas as rotas
                .maxAge(refreshMaxAgeSeconds)
                .build();
    }

    public ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure("prod".equalsIgnoreCase(env))
                .sameSite("None")
                .path("/")
                .maxAge(0) // apaga o cookie
                .build();
    }
}

