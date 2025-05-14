package com.br.gasto_comum.exceptions;

public class UserAlreadyRegistered extends RuntimeException {

    public UserAlreadyRegistered() {
        super("Usuário ou e-mail já cadastrado");
    }

    public UserAlreadyRegistered(String message) {
        super(message);
    }
}
