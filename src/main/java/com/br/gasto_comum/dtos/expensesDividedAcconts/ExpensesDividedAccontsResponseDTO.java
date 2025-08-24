package com.br.gasto_comum.dtos.expensesDividedAcconts;

import com.br.gasto_comum.enums.Status;
import com.br.gasto_comum.enums.Type;
import com.br.gasto_comum.models.ExpensesDividedAcconts;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExpensesDividedAccontsResponseDTO (
        UUID id,
        UUID userId,
        Status status,
        Double value,
        String spendingTitle,
        Type spendingType,
        String spendingDescription,
        LocalDate spendingDate,
        List<UUID> spendingParticipantsIds
){
    public ExpensesDividedAccontsResponseDTO(ExpensesDividedAcconts expensesDividedAcconts) {
        this(
                expensesDividedAcconts.getId(),
                expensesDividedAcconts.getUser().getId(),
                expensesDividedAcconts.getStatus(),
                expensesDividedAcconts.getValue(),
                expensesDividedAcconts.getSpending().getTitle(),
                expensesDividedAcconts.getSpending().getType(),
                expensesDividedAcconts.getSpending().getDescription(),
                expensesDividedAcconts.getSpending().getDateSpending(),
                expensesDividedAcconts.getSpending().getExpensesDividedAcconts().stream().map(e -> e.getUser().getId()).toList()
        );
    }
}
