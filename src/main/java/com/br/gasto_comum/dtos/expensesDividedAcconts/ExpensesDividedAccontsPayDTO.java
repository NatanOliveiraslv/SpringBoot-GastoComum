package com.br.gasto_comum.dtos.expensesDividedAcconts;

import jakarta.validation.constraints.NotBlank;

public record ExpensesDividedAccontsPayDTO(
        @NotBlank
        double value
) {
}
