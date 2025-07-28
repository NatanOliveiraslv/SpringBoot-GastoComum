package com.br.gasto_comum.controllers;

import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.dtos.users.UserResponseDTO;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> listUser(@RequestParam(required = false) String searchQuery, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {

        Page<UserResponseDTO> userPage;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Se um termo de busca foi fornecido, busque por nome OU e-mail
            userPage = userService.findUsersByNameOrEmailContaining(searchQuery, pageable);
        } else {
            // Caso contrário, liste todos (paginado)
            userPage = userService.listUsers(pageable);
        }

        return ResponseEntity.ok(userPage);
    }

}
