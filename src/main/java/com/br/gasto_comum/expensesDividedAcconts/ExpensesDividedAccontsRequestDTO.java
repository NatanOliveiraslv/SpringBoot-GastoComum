package com.br.gasto_comum.expensesDividedAcconts;

import jakarta.validation.constraints.NotNull;

public record ExpensesDividedAccontsRequestDTO(
        @NotNull
        Long userId,
        @NotNull
        Long spendingId
){
}
