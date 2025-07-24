package com.br.gasto_comum.dtos.Document;

public record DocumentResponseDTO (
        String link
) {

    public DocumentResponseDTO(String link) {
        this.link = link;
    }
}
