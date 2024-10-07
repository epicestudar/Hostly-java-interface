package com.example.test;

public enum TipoQuarto {
    SUITE(200.00), 
    DUPLEX(300.00), 
    STANDARD(150.00);

    private final double valorDiaria;

    TipoQuarto(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }
}
