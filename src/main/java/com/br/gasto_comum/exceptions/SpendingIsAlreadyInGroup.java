package com.br.gasto_comum.exceptions;

public class SpendingIsAlreadyInGroup extends RuntimeException {

    public SpendingIsAlreadyInGroup() {
        super("Gasto já está no grupo");
    }
    public SpendingIsAlreadyInGroup(String message) {
        super(message);
    }
}
