package com.br.gasto_comum.dtos.spending;

import com.br.gasto_comum.enums.Type;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record SpendingUpdateDTO(

        @NotNull
        UUID id,
        Type type,
        String title,
        String description ) {
}