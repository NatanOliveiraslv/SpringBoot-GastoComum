package com.br.gasto_comum.dtos.File;

import com.br.gasto_comum.models.File;

public record FileResponseDTO (
        String systemFileName,
        String fileType,
        long size,
        String fileDownloadUri
) {

    public FileResponseDTO(File file, String fileDownloadUri) {
        this(
                file.getSystemFileName(),
                file.getMimeType(),
                file.getSize(),
                fileDownloadUri
        );

    }
}
