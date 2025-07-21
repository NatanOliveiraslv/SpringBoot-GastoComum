package com.br.gasto_comum.dtos.users;

public record AuthenticationRequestDTO(
        String username,
        String password
) {
}
