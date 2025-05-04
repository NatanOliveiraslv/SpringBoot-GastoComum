package com.br.gasto_comum.exceptions;

public class ObjectNotFound extends RuntimeException {
    public ObjectNotFound() {
        super("Id não encontrado");
    }

    public ObjectNotFound(String mensagem) {
        super(mensagem);
    }
}
