package org.example.ejercicio3;

public class Pedidos {
    private final TipoPlato tipo;

    public Pedidos() {
        this.tipo = TipoPlato.values()[(int) (Math.random() * TipoPlato.values().length)];
    }

    public TipoPlato getTipo() {
        return tipo;
    }
}
