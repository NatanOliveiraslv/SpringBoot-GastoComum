package com.br.gasto_comum.infra.security;

import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.models.RefreshToken;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.RefreshTokenRepository;
import com.br.gasto_comum.repositorys.UserRepository;
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
import java.time.Instant;

import static com.br.gasto_comum.services.UserService.refreshTokenTtl;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
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

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UnauthorizedUser("Usário não autorizado"));

        String token = tokenService.generateToken(username);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(refreshTokenTtl));
        refreshTokenRepository.save(refreshToken);

        String redirectUrl = FRONTEND_URL + "/login-success?accessToken=" + token + "&refreshToken=" + refreshToken.getId();
        response.sendRedirect(redirectUrl);
    }
}