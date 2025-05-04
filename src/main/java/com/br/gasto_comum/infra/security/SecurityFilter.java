package com.br.gasto_comum.infra.security;

import com.br.gasto_comum.users.UserRepository;
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

@Configuration
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkIfEndpointIsNotPublic(request)) {
            String token = getToken(request); // Recupera o token do cabeçalho Authorization da requisição
            if (token != null) {
                try {
                    String subject = tokenService.getSubject(token); // Obtém o assunto (neste caso, o nome de usuário) do token
                    var user = userRepository.findByLogin(subject); // Busca o usuário pelo login (que é o assunto do token)
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }catch (Exception e){
                    logger.warn("Falha na autenticação: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.setContentType("application/json");
                    response.getWriter().write("{\"erro\": \"Falha de autenticação: token inválido\"}");
                    return; // <- impede que continue o fluxo!
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"erro\": \"Falha de autenticação: token ausente\"}");
                return;
            }
        }
        filterChain.doFilter(request, response); // Continua o processamento da requisição
    }

    private String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfigurations.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }

}
