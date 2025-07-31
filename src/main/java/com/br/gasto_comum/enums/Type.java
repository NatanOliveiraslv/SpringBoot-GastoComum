package com.br.gasto_comum.enums;

public enum Type {
    COMIDA("Comida"),
    TRANSPORTE("Transporte"),
    UTILITARIAS("Utilitárias"),
    CASA("Casa"),
    ENTRETENIMENTO("Entretenimento"),
    SAUDE("Saúde"),
    SHOPPING("Shopping"),
    EDUCACAO("Educação"),
    VIAGEM("Viagem"),
    OUTROS("Outros");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getNameType() {
        return type;
    }
}
