package com.br.gasto_comum.dtos.spending;

import com.br.gasto_comum.enums.Type;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SpendingRequestDTO(

        @NotNull
        @Enumerated
        Type type,
        @NotBlank
        String title,
        @NotNull
        Double value,
        String description,
        @NotNull
        LocalDate dateSpending
        ) {
}
