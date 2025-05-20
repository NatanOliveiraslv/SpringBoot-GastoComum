package com.br.gasto_comum.dtos.spending;

import com.br.gasto_comum.enums.Type;
import jakarta.validation.constraints.NotNull;


public record SpendingUpdateDTO(

        @NotNull
        Long id,
        Type type,
        String title,
        String description ) {
}