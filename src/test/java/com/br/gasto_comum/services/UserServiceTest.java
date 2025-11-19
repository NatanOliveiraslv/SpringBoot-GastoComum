package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.users.AuthenticationRequestDTO;
import com.br.gasto_comum.dtos.users.AuthenticationResponseDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.dtos.users.UserResponseDTO;
import com.br.gasto_comum.exceptions.UserAlreadyRegistered;
import com.br.gasto_comum.infra.security.SecurityConfigurations;
import com.br.gasto_comum.infra.security.TokenService;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private SecurityConfigurations securityConfiguration;

    @Test
    @DisplayName("Criar usuário com sucesso")
    void createUser_success() {
        UserRequestDTO dto = new UserRequestDTO("user", "pass", "Nome", "Sobrenome", "email@email.com");
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("email@email.com")).thenReturn(false);
        when(securityConfiguration.passwordEncoder()).thenReturn(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder());

        UserResponseDTO response = userService.createUser(dto);

        assertEquals("user", response.login());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Tentar criar usuário já registrado")
    void createUser_alreadyRegistered() {
        UserRequestDTO dto = new UserRequestDTO("user", "pass", "Nome", "Sobrenome", "email@email.com");
        when(userRepository.existsByUsername("user")).thenReturn(true);

        assertThrows(UserAlreadyRegistered.class, () -> userService.createUser(dto));
    }

    @Test
    @DisplayName("Autenticar usuário com sucesso")
    void authenticate_success() {
        AuthenticationRequestDTO dto = new AuthenticationRequestDTO("user", "pass");
        User user = User.builder().username("user").build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new org.springframework.security.core.Authentication() {
            @Override public Object getPrincipal() { return user; }
            @Override public Object getCredentials() { return null; }
            @Override public Object getDetails() { return null; }
            @Override public Collection<? extends GrantedAuthority> getAuthorities() { return null; }
            @Override public boolean isAuthenticated() { return true; }
            @Override public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
            @Override public String getName() { return "user"; }
        });
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(tokenService.generateToken("user")).thenReturn("accessToken");
        when(tokenService.generateRefreshToken("user")).thenReturn("refreshToken");

        AuthenticationResponseDTO response = userService.authenticate(dto);

        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
    }

    @Test
    @DisplayName("Listar usuários com sucesso")
    void listUsers_success() {
        User currentUser = User.builder().id(UUID.randomUUID()).build();
        User user2 = User.builder().id(UUID.randomUUID()).username("user2").build();
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findByIdIsNot(currentUser.getId(), pageable))
                .thenReturn(new PageImpl<>(List.of(user2)));

        var page = userService.listUsers(pageable, currentUser);

        assertEquals(1, page.getTotalElements());
        assertEquals("user2", page.getContent().get(0).login());
    }
}