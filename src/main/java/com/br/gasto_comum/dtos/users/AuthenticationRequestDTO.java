package com.br.gasto_comum.dtos.users;

public record AuthenticationRequestDTO(
        String login,
        String password
) {
}
