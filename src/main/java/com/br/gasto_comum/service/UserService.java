package com.br.gasto_comum.service;

import com.br.gasto_comum.infra.DataTokenJWT;
import com.br.gasto_comum.infra.SecurityConfigurations;
import com.br.gasto_comum.infra.TokenService;
import com.br.gasto_comum.users.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityConfigurations securityConfiguration;

    public UserResponseDTO createUser(UserRequestDTO data) {
        if (userRepository.existsByLogin(data.login()) || userRepository.existsByEmail(data.email())) {
            ResponseEntity.badRequest().body(null); // Retorna erro 400 se login ou e-mail já existir
        }
        var userEntity = User.builder().login(data.login())
                .password(securityConfiguration.passwordEncoder().encode(data.password()))
                .firstName(data.firstName())
                .lastName(data.lastName())
                .email(data.email())
                .build();
        userRepository.save(userEntity);

        return new UserResponseDTO(userEntity);
    }

    public DataTokenJWT authenticate(AuthenticationDTO data) {

        var token = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var authentication = authenticationManager.authenticate(token);

        String tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return new DataTokenJWT(tokenJWT);
    }
}
