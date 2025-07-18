package com.br.gasto_comum.dtos.users;

import java.util.UUID;

public record AuthenticationResponseDTO(
        String accessToken,
        UUID refreshToken
) {
}
