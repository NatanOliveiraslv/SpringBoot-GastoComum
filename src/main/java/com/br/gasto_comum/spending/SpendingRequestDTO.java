package com.br.gasto_comum.spending;

import com.br.gasto_comum.users.User;

public record SpendingRequestDTO(

        Type type,
        Double value,
        String description,
        Long userID

        ) {
}
