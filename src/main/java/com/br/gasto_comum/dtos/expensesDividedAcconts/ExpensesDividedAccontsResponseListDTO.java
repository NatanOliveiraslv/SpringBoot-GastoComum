package com.br.gasto_comum.dtos.expensesDividedAcconts;

import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.enums.Type;

public record ExpensesDividedAccontsResponseListDTO(
        //spending
        Long spendingId,
        Type spendingType,
        String spendingTitle,
        Double spendingValue,
        String spendingUserLogin,
        //expensesDividedAcconts
        Long expensesDividedAccontsId,
        String expensesDividedAccontsStatus,
        Double expensesDividedAccontsValue
) {
    public ExpensesDividedAccontsResponseListDTO(ExpensesDividedAcconts expensesDividedAcconts) {
        this(
                expensesDividedAcconts.getSpending().getId(),
                expensesDividedAcconts.getSpending().getType(),
                expensesDividedAcconts.getSpending().getTitle(),
                expensesDividedAcconts.getSpending().getValue(),
                expensesDividedAcconts.getSpending().getUser().getUsername(),
                expensesDividedAcconts.getId(),
                expensesDividedAcconts.getStatus().toString(),
                expensesDividedAcconts.getValue()
        );
    }
}
