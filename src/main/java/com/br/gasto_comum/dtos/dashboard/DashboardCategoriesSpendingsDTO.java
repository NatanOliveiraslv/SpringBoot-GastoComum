package com.br.gasto_comum.dtos.dashboard;

import com.br.gasto_comum.enums.Type;

public record DashboardCategoriesSpendingsDTO(
        String name,
        Long amount
) {
    public DashboardCategoriesSpendingsDTO(Type type, Long amount) {
        this(type.getNameType(), amount);
    }
}
