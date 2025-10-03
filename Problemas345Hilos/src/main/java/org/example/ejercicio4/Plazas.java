package org.example.ejercicio4;

public class Plazas {
    private final int idPlaza;
    private final TipoVehiculo tipo;
    private boolean ocupada;

    public Plazas(int idPlaza, TipoVehiculo tipo) {
        this.idPlaza = idPlaza;
        this.tipo = tipo;
        this.ocupada = false;
    }

    public int getIdPlaza() {
        return idPlaza;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void ocupar() {
        this.ocupada = true;
    }

    public void liberar() {
        this.ocupada = false;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }
}
