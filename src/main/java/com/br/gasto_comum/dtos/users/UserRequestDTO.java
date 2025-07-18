package com.br.gasto_comum.dtos.users;

import com.br.gasto_comum.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(

        @NotBlank
        String userName,
        @NotBlank
        String password,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotBlank
        String email
) {
        public UserRequestDTO(User user) {
                this(
                        user.getUsername(),
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                );
        }
}
