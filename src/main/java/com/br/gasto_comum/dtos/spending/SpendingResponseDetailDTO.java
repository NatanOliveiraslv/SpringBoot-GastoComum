package com.br.gasto_comum.dtos.spending;

import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseDTO;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.enums.Type;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SpendingResponseDetailDTO(
        UUID id,
        String type,
        String title,
        Double value,
        String description,
        String userName,
        String userEmail,
        LocalDate dateSpending,
        List<ExpensesDividedAccontsResponseDTO> expensesDividedAcconts

) {
    public SpendingResponseDetailDTO(Spending spending) {
        this(
                spending.getId(),
                spending.getType().getNameType(),
                spending.getTitle(),
                spending.getValue(),
                spending.getDescription(),
                spending.getUser().getFirstName(),
                spending.getUser().getEmail(),
                spending.getDateSpending(),
                spending.getExpensesDividedAcconts().stream()
                        .map(ExpensesDividedAccontsResponseDTO::new)
                        .toList()
        );
    }
}
