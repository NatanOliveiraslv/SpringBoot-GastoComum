package com.br.gasto_comum.users;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(

        @NotBlank
        String login,
        String password,
        String firstName,
        String lastName,
        String email
) {
        public UserRequestDTO(User user) {
                this(
                        user.getLogin(),
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                );
        }
}
