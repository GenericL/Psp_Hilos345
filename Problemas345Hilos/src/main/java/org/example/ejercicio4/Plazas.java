package org.example.ejercicio4;

import java.util.concurrent.atomic.AtomicBoolean;

public class Plazas {
    private final int idPlaza;
    private final TipoVehiculo tipo;
    private AtomicBoolean ocupada;

    public Plazas(int idPlaza, TipoVehiculo tipo) {
        this.idPlaza = idPlaza;
        this.tipo = tipo;
        this.ocupada = new AtomicBoolean(false);
    }

    public int getIdPlaza() {
        return idPlaza;
    }

    public boolean isOcupada() {
        return ocupada.get();
    }

    public void ocupar() {
        this.ocupada = new AtomicBoolean(true);
    }

    public void liberar() {
        this.ocupada = new AtomicBoolean(false);
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }
}
