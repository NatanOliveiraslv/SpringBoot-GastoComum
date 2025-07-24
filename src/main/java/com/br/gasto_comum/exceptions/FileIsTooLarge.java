package com.br.gasto_comum.exceptions;

public class FileIsTooLarge extends RuntimeException {
    public FileIsTooLarge() {
        super("O arquivo é muito grande. O limite de tamanho é 16 MB.");
    }

    public FileIsTooLarge(String message) {
        super(message);
    }

}
