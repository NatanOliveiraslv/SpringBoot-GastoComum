package com.br.gasto_comum.dtos.File;

import com.br.gasto_comum.models.File;

public record FileResponseDTO (
        Long id,
        String systemFileName,
        String fileType,
        long size
) {

    public FileResponseDTO(File file) {
        this(
                file.getId(),
                file.getSystemFileName(),
                file.getMimeType(),
                file.getSize()
        );

    }
}
