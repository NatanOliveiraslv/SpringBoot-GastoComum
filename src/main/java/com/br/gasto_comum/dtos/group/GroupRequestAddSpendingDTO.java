package com.br.gasto_comum.dtos.group;

import java.util.List;

public record GroupRequestAddSpendingDTO(
        Long groupId,
        List<Long> spendingId
) {
}
