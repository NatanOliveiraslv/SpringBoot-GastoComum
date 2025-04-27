package com.br.gasto_comum.ExpensesDividedAcconts;

import jakarta.validation.constraints.NotNull;

public record ExpensesDividedAccontsRequestDTO(
        @NotNull
        Long userId,
        @NotNull
        Long spendingId
){
}
