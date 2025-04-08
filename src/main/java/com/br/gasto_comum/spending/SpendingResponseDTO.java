package com.br.gasto_comum.spending;

public record SpendingResponseDTO(
        Long id,
        Type type,
        Double value,
        String description,
        String userLogin
) {
    public SpendingResponseDTO(Spending spendingEntity) {
        this(
                spendingEntity.getId(),
                spendingEntity.getType(),
                spendingEntity.getValue(),
                spendingEntity.getDescription(),
                spendingEntity.getUser().getLogin()
        );
    }
}
