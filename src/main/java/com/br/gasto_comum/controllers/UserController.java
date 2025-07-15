package com.br.gasto_comum.controllers;

import com.br.gasto_comum.dtos.users.AuthenticationDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.dtos.users.UserResponseDTO;
import com.br.gasto_comum.infra.security.DataTokenJWT;
import com.br.gasto_comum.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Transactional
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO data) {
        return ResponseEntity.ok(userService.createUser(data));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<DataTokenJWT> authenticate(@RequestBody @Valid AuthenticationDTO data) {
        return ResponseEntity.ok(userService.authenticate(data));
    }
}
