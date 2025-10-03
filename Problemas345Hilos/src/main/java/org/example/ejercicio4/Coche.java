package org.example.ejercicio4;


import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class Coche {
    private final int idCoche;
    private final TipoVehiculo tipo;

    public Coche(int idCoche, TipoVehiculo tipo) {
        this.idCoche = idCoche;
        this.tipo = tipo;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public int getIdCoche() {
        return idCoche;
    }

    public void getPlaza(Semaphore semaphore, AtomicInteger dinero, AtomicLong tiempoEstancia) {
        try {
            System.out.printf("Coche-%d (%s) ha aparcado.%n", idCoche, tipo.getTipo());
            long tiempoEstanciaFinal = Math.round(Math.random() * 20000) + 10000;
            Thread.sleep((tiempoEstanciaFinal));
            tiempoEstancia.addAndGet(tiempoEstanciaFinal);
            System.out.printf("Coche-%d (%s) sale - - Pagó %.2f€ %n", idCoche, tipo.getTipo(), tipo.getTarifaPorMinuto());
            dinero.addAndGet((int) tipo.getTarifaPorMinuto()*10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getLogger(Coche.class.getName()).severe("Coche interrumpido");
        }
    }
}
