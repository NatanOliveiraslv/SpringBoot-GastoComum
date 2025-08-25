package com.br.gasto_comum.dtos.dashboard;

import com.br.gasto_comum.enums.Type;

public record DashboardCategoriesSpendingsDTO(
        Type enumType,
        String type,
        Long amount
) {
    public DashboardCategoriesSpendingsDTO(Type type, Long amount) {
        this(type, type.getNameType(), amount);
    }
}
