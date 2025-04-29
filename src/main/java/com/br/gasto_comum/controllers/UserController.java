package com.br.gasto_comum.controllers;

import com.br.gasto_comum.users.User;
import com.br.gasto_comum.users.UserRepository;
import com.br.gasto_comum.users.UserRequestDTO;
import com.br.gasto_comum.users.UserResponseDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO data) {
        if (userRepository.existsByLogin(data.login()) || userRepository.existsByEmail(data.email())) {
            return ResponseEntity.badRequest().body(null); // Retorna erro 400 se login ou e-mail já existir
        }
        var userEntity = new User(data);
        userRepository.save(userEntity);

        return ResponseEntity.ok(new UserResponseDTO(userEntity));
    }


}
