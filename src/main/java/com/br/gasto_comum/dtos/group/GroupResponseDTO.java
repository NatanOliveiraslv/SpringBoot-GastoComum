package com.br.gasto_comum.dtos.group;

import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.models.Group;

public record GroupResponseDTO(
        Long id,
        String name,
        String description,
        Double totalValue
) {
    public GroupResponseDTO(Group group) {
        this(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getTotal_value() != null ? group.getTotal_value() : 0.0
        );
    }
}
