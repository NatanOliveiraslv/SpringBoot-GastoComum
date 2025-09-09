package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.File.FileResponseDTO;
import com.br.gasto_comum.dtos.users.AuthenticationRequestDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.dtos.users.UserResponseDTO;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.exceptions.UserAlreadyRegistered;
import com.br.gasto_comum.dtos.users.AuthenticationResponseDTO;
import com.br.gasto_comum.infra.security.SecurityConfigurations;
import com.br.gasto_comum.infra.security.TokenService;
import com.br.gasto_comum.models.File;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;


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
    @Autowired
    private FileService fileService;
    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    // Deixado como public para ser acessado no OAuth2AuthenticationSuccessHandler
    // Define o tempo de vida do token de atualização (refresh token) como 7 dias
    public static final Duration refreshTokenTtl = Duration.ofDays(7);

    public UserResponseDTO createUser(UserRequestDTO data) {
        if (userRepository.existsByUsername(data.userName()) || userRepository.existsByEmail(data.email())) {
            throw new UserAlreadyRegistered();// Retorna erro 400 se login ou e-mail já existir
        }
        var userEntity = User.builder().username(data.userName())
                .password(securityConfiguration.passwordEncoder().encode(data.password()))
                .firstName(data.firstName())
                .lastName(data.lastName())
                .email(data.email())
                .build();
        userRepository.save(userEntity);

        return new UserResponseDTO(userEntity);
    }

    public AuthenticationResponseDTO authenticate(final AuthenticationRequestDTO data) {

        // Authenticate the user
        var token = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var authentication = authenticationManager.authenticate(token);

        // Generate JWT access token
        String accessToken = tokenService.generateToken(((User) authentication.getPrincipal()).getUsername());

        // Fetch user and create refresh token
        User user = userRepository.findByUsername(data.username()).orElseThrow(() -> new UnauthorizedUser("Usário não autorizado"));

        String refreshToken = tokenService.generateRefreshToken(user.getUsername());

        return new AuthenticationResponseDTO(accessToken, refreshToken);
    }

    public AuthenticationResponseDTO refreshToken(String refreshToken) {
        String subject = tokenService.getSubject(refreshToken); // Valida o token JWT
        final var newAccessToken = tokenService.generateToken(subject);
        return new AuthenticationResponseDTO(newAccessToken, refreshToken);
    }

    public Page<UserResponseDTO> listUsers(Pageable pageable, User currentUser) {
        return userRepository.findByIdIsNot(currentUser.getId(), pageable)
                .map(UserResponseDTO::new);
    }

    public Page<UserResponseDTO> findUsersByNameOrEmailContaining(String searchQuery, Pageable pageable, User currentUser) {
        return userRepository.findByFirstNameOrEmailContainingIgnoreCaseAndIdIsNot(searchQuery, currentUser.getId(), pageable).map(UserResponseDTO::new);
    }

    public FileResponseDTO uploadProfilePicture(MultipartFile file, User user) throws IOException, NoSuchAlgorithmException {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("Arquivo não pode ser nulo ou vazio");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (!List.of("image/jpeg", "image/png", "image/gif").contains(contentType)) {
            throw new ValidationException("Tipo de arquivo inválido. Apenas imagens JPEG, PNG e GIF são permitidas.");
        }

        File profilePicture = fileService.uploadFile(file);
        user.setProfilePicture(profilePicture);
        userRepository.save(user);
        return new FileResponseDTO(profilePicture);
    }

    public Resource downloadProfilePicture(UUID user) {

        List<String> defaultProfilePictureUrls = List.of(
                "default_avatar_1.jpg",
                "default_avatar_2.jpg",
                "default_avatar_3.jpg",
                "default_avatar_4.jpg"
        );

        var userEntity = userRepository.findById(user).orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        if (userEntity.getProfilePicture() == null) {
            Random random = new Random();
            int index = random.nextInt(defaultProfilePictureUrls.size());
            return new ClassPathResource("/static/profile_pictures/" + defaultProfilePictureUrls.get(index));
        }
        String systemFileName = userEntity.getProfilePicture().getSystemFileName();
        return fileSystemStorageService.loadFileAsResource(systemFileName);
    }

    public UserResponseDTO getCurrentUser(User user) {
        return new UserResponseDTO(user);
    }
}
