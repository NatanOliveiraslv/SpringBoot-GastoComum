package com.br.gasto_comum.exceptions;

public class ExpiredRefreshToken extends RuntimeException {
    public ExpiredRefreshToken() {
        super("Token de atualização inválido ou expirado");
    }

    public ExpiredRefreshToken(String message) {
        super(message);
    }
}
