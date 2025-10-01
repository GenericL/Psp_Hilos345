package org.example.ejercicio3;

public class Pedidos {
    private int idCliente;
    private final TipoPlato tipo;
    private Mesa mesa;
    private final long tiempoEsperando;

    public Pedidos(int idCliente, TipoPlato tipo, Mesa mesa) {
        this.idCliente = idCliente;
        this.tipo = tipo;
        this.mesa = mesa;
        this.tiempoEsperando = System.currentTimeMillis();
    }

    public TipoPlato getTipo() {
        return tipo;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public long getTiempoEsperando() {
        return tiempoEsperando;
    }
}
