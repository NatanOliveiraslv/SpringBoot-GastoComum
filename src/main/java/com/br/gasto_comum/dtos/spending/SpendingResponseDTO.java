package com.br.gasto_comum.dtos.spending;

import com.br.gasto_comum.enums.Status;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.enums.Type;

import java.util.UUID;

public record SpendingResponseDTO(
        UUID id,
        String type,
        String title,
        Double value,
        String description,
        int totalParticipants,
        int totalPayingCustomers
) {
    public SpendingResponseDTO(Spending spendingEntity) {
        this(
                spendingEntity.getId(),
                spendingEntity.getType().getNameType(),
                spendingEntity.getTitle(),
                spendingEntity.getValue(),
                spendingEntity.getDescription(),
                spendingEntity.getExpensesDividedAcconts() == null ? 0 : spendingEntity.getExpensesDividedAcconts().size(),
                spendingEntity.getExpensesDividedAcconts() == null ? 0 : (int) spendingEntity.getExpensesDividedAcconts().stream()
                        .filter(expense -> expense.getStatus() == Status.PAID)
                        .count()
        );
    }
}
