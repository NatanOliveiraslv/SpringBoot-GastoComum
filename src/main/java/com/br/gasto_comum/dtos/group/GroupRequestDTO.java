package com.br.gasto_comum.dtos.group;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record GroupRequestDTO(
        @NotNull
        String name,
        String description,
        List<UUID> spendingIds
){
}
