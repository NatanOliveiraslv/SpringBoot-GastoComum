package com.br.gasto_comum.dtos.spending;

import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.enums.Type;

public record SpendingResponseDTO(
        Long id,
        String type,
        String title,
        Double value,
        String description,
        int totalParticipants
) {
    public SpendingResponseDTO(Spending spendingEntity) {
        this(
                spendingEntity.getId(),
                spendingEntity.getType().getNameType(),
                spendingEntity.getTitle(),
                spendingEntity.getValue(),
                spendingEntity.getDescription(),
                spendingEntity.getExpensesDividedAcconts() == null ? 0 : spendingEntity.getExpensesDividedAcconts().size()
        );
    }
}
