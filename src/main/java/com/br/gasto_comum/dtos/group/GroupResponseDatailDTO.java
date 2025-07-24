package com.br.gasto_comum.dtos.group;

import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.models.Group;

import java.util.List;
import java.util.UUID;

public record GroupResponseDatailDTO(
        UUID id,
        String name,
        String description,
        Double totalValue,
        String userName,
        List<SpendingResponseDTO> spendings
) {
    public GroupResponseDatailDTO(Group group) {
        this(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getTotal_value() != null ? group.getTotal_value() : 0.0,
                group.getUser().getUsername(),
                group.getSpendings().stream()
                        .map(SpendingResponseDTO::new)
                        .toList()
        );
    }
}
