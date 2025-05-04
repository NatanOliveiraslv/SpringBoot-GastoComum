package com.br.gasto_comum.exceptions;

public class UnauthorizedUser extends RuntimeException {

    public UnauthorizedUser() {
        super("Usuário não possui autorização");
    }

    public UnauthorizedUser(String message) {
        super(message);
    }
}
