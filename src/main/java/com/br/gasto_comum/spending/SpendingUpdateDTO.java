package com.br.gasto_comum.spending;

import jakarta.validation.constraints.NotNull;


public record SpendingUpdateDTO(

        @NotNull
        Long id,
        Type type,
        String title,
        String description ) {
}