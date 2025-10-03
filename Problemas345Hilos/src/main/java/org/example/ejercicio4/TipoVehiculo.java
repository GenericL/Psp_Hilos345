package org.example.ejercicio4;

public enum TipoVehiculo {
    NORMAL(2.0, "Normal"),  // 1€/minuto
    VIP(4.5, "VIP");     // 2€/minuto

    private final double tarifaPorMinuto;
    private final String tipo;

    TipoVehiculo(double tarifaPorMinuto, String tipo) {
        this.tarifaPorMinuto = tarifaPorMinuto;
        this.tipo = tipo;
    }

    public double getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public String getTipo() {
        return tipo;
    }
}
