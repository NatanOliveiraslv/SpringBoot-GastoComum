package com.br.gasto_comum.dtos.users;

import com.br.gasto_comum.models.User;

public record UserResponseDTO(
        Long id,
        String login,
        String firstName,
        String lastName,
        String email
) {
    public UserResponseDTO(User userEntity) {
        this(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail()
        );
    }
}
