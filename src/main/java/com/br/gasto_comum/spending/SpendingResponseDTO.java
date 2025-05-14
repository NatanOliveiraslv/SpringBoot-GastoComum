package com.br.gasto_comum.spending;

public record SpendingResponseDTO(
        Long id,
        Type type,
        String title,
        Double value,
        String description
) {
    public SpendingResponseDTO(Spending spendingEntity) {
        this(
                spendingEntity.getId(),
                spendingEntity.getType(),
                spendingEntity.getTitle(),
                spendingEntity.getValue(),
                spendingEntity.getDescription()

        );
    }
}
