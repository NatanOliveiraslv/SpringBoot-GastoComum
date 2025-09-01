package com.br.gasto_comum.infra.security;

import com.br.gasto_comum.services.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;
    @Value("${frontend.url}")
    private String FRONTEND_URL;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof CustomOAuth2User customUser) {
            username = customUser.getUsername();
        } else if (principal instanceof DefaultOidcUser oidcUser) {
            username = oidcUser.getEmail(); // ou getAttribute("email")
        } else {
            throw new IllegalStateException("Tipo de usuário não suportado: " + principal.getClass());
        }

        String token = tokenService.generateToken(username);
        String redirectUrl = FRONTEND_URL + "/login-success?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}