package com.br.gasto_comum.dtos.expensesDividedAcconts;

import com.br.gasto_comum.enums.Status;
import com.br.gasto_comum.models.ExpensesDividedAcconts;

public record ExpensesDividedAccontsResponseDTO (
        Long id,
        String userLogin,
        Status status,
        Double value,
        String spendingTitle
){
    public ExpensesDividedAccontsResponseDTO(ExpensesDividedAcconts expensesDividedAcconts) {
        this(
                expensesDividedAcconts.getId(),
                expensesDividedAcconts.getUser().getLogin(),
                expensesDividedAcconts.getStatus(),
                expensesDividedAcconts.getValue(),
                expensesDividedAcconts.getSpending().getTitle()
        );
    }
}
