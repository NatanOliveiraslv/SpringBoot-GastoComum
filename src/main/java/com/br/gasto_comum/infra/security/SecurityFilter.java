package com.br.gasto_comum.infra.security;

import com.br.gasto_comum.models.User;
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
import org.springframework.util.AntPathMatcher; // Importar AntPathMatcher

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Configuration
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Log para depuração
        System.out.println("SecurityFilter: Processando requisição para URI: " + request.getRequestURI());

        if (checkIfEndpointIsNotPublic(request)) {
            String token = getToken(request);
            if (token != null) {
                try {
                    String subject = tokenService.getSubject(token);
                    Optional<User> userOptional = userRepository.findByUsername(subject);

                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("SecurityFilter: Usuário '" + subject + "' autenticado com sucesso via token.");
                    } else {
                        logger.warn("SecurityFilter: Usuário não encontrado para o assunto do token: " + subject);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                        response.setContentType("application/json");
                        response.getWriter().write("{\"erro\": \"Falha de autenticação: usuário não encontrado\"}");
                        return;
                    }
                } catch (Exception e) {
                    logger.warn("SecurityFilter: Falha na autenticação do token: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.setContentType("application/json");
                    response.getWriter().write("{\"erro\": \"Falha de autenticação: token inválido ou erro interno\"}");
                    return;
                }
            } else {
                logger.warn("SecurityFilter: Token ausente para URI protegida: " + request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"erro\": \"Falha de autenticação: token ausente\"}");
                return;
            }
        } else {
            System.out.println("SecurityFilter: URI '" + request.getRequestURI() + "' é um endpoint público. Pulando verificação de token.");
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

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        for (String publicEndpoint : SecurityConfigurations.ENDPOINTS_PERMIT_ALL) {
            // Se o requestURI corresponde a um dos padrões públicos
            if (pathMatcher.match(publicEndpoint, requestURI)) {
                return false;
            }
        }
        return true;
    }
}