package com.br.gasto_comum.dtos.expensesDividedAcconts;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ExpensesDividedAccontsRequestDTO(
        @NotNull
        UUID spendingId,
        @NotNull
        UUID userId
){
}
