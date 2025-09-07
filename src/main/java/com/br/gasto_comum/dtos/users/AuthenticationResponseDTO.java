package com.br.gasto_comum.dtos.users;

public record AuthenticationResponseDTO(
        String accessToken,
        String refreshToken
) {
}
