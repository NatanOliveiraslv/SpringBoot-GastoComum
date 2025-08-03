package com.br.gasto_comum.dtos.group;

import java.util.List;
import java.util.UUID;

public record GroupRequestAddSpendingDTO(
        UUID groupId,
        List<UUID> spendingIds
) {
}
