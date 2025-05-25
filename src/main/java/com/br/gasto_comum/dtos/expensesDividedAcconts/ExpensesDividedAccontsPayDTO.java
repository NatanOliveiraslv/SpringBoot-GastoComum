package com.br.gasto_comum.dtos.expensesDividedAcconts;

import jakarta.validation.constraints.NotNull;

public record ExpensesDividedAccontsPayDTO(
        @NotNull
        double value
) {
}
