package com.br.gasto_comum.dtos.expensesDividedAcconts;

import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.enums.Type;

import java.util.UUID;

public record ExpensesDividedAccontsResponseListDTO(
        //spending
        UUID spendingId,
        Type spendingType,
        String spendingTitle,
        Double spendingValue,
        String spendingUserLogin,
        //expensesDividedAcconts
        UUID expensesDividedAccontsId,
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
