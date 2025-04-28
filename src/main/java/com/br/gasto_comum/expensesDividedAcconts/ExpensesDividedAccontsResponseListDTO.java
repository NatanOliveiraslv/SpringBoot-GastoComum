package com.br.gasto_comum.expensesDividedAcconts;

import com.br.gasto_comum.spending.Type;

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
                expensesDividedAcconts.getSpending().getUser().getLogin(),
                expensesDividedAcconts.getId(),
                expensesDividedAcconts.getStatus().toString(),
                expensesDividedAcconts.getValue()
        );
    }
}
