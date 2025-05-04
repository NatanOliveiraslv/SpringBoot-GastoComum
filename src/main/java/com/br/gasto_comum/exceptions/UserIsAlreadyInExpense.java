package com.br.gasto_comum.exceptions;

public class UserIsAlreadyInExpense extends RuntimeException {

    public UserIsAlreadyInExpense() {
        super("Usuário já está na despesa");
    }
    public UserIsAlreadyInExpense(String message) {
        super(message);
    }
}
