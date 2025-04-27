package com.br.gasto_comum.ExpensesDividedAcconts;

import com.br.gasto_comum.spending.Spending;
import com.br.gasto_comum.users.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

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
