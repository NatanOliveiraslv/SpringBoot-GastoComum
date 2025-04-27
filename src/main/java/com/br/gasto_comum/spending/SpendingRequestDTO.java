package com.br.gasto_comum.spending;

import com.br.gasto_comum.users.User;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpendingRequestDTO(

        @Enumerated
        Type type,
        @NotBlank
        String title,
        @NotNull
        Double value,
        String description,
        @NotNull
        Long userId

        ) {
}
