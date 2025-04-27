package com.br.gasto_comum.spending;

public record SpendingResponseDetailDTO(
        Long id,
        Type type,
        String title,
        Double value,
        String description,
        String userName,
        String userEmail,
        String registrationDate
) {
    public SpendingResponseDetailDTO(Spending spending) {
        this(
                spending.getId(),
                spending.getType(),
                spending.getTitle(),
                spending.getValue(),
                spending.getDescription(),
                spending.getUser().getFirstName(),
                spending.getUser().getEmail(),
                spending.getRegistration_date().toString()
        );
    }
}
