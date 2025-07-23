package com.br.gasto_comum.infra.security;

import com.br.gasto_comum.models.User; // Importar a classe User
import com.br.gasto_comum.repositorys.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional; // Importar Optional

@Configuration
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkIfEndpointIsNotPublic(request)) {
            String token = getToken(request);
            if (token != null) {
                try {
                    String subject = tokenService.getSubject(token);
                    Optional<User> userOptional = userRepository.findByUsername(subject);

                    if (userOptional.isPresent()) { // Verifica se o usuário foi encontrado
                        User user = userOptional.get(); // Extrai o objeto User do Optional
                        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        logger.warn("Usuário não encontrado para o assunto do token: " + subject);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                        response.setContentType("application/json");
                        response.getWriter().write("{\"erro\": \"Falha de autenticação: usuário não encontrado\"}");
                        return;
                    }
                } catch (Exception e) {
                    logger.warn("Falha na autenticação: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.setContentType("application/json");
                    response.getWriter().write("{\"erro\": \"Falha de autenticação: token inválido ou erro interno\"}");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"erro\": \"Falha de autenticação: token ausente\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // É crucial que ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED contenha o caminho exato.
        // Se você tiver paths com curingas (e.g., /public/**), a lógica de verificação
        // precisará ser mais sofisticada, talvez usando AntPathMatcher.
        return !Arrays.asList(SecurityConfigurations.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }
}