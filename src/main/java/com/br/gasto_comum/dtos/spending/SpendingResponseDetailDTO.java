package com.br.gasto_comum.dtos.spending;

import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseDTO;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.enums.Type;

import java.util.List;

public record SpendingResponseDetailDTO(
        Long id,
        String type,
        String title,
        Double value,
        String description,
        String userName,
        String userEmail,
        String registrationDate,
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
                spending.getRegistration_date().toString(),
                spending.getExpensesDividedAcconts().stream()
                        .map(ExpensesDividedAccontsResponseDTO::new)
                        .toList()
        );
    }
}
