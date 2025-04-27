package com.br.gasto_comum.spending;

import jakarta.validation.constraints.NotNull;

public record SpendingDeleteDTO(
        @NotNull
        Long userId
) {
}
