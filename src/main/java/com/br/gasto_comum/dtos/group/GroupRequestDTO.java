package com.br.gasto_comum.dtos.group;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record GroupRequestDTO(
        @NotBlank(message = "O nome do grupo é obrigatório")
        String name,
        String description,
        List<UUID> spendingIds
){
}
