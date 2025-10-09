package org.example.ejercicio4;


import org.example.constantes.ConstantesParkingInteligente;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Coche implements Runnable {
    private final int idCoche;
    private final TipoVehiculo tipo;
    private final AtomicInteger dinero;
    private final AtomicLong tiempoEstancia;
    private BlockingQueue<Plazas> plazasDisponibles;
    private Semaphore semaforoUsado;

    public Coche(int idCoche, TipoVehiculo tipo, AtomicInteger dinero, AtomicLong tiempoEstancia) {
        this.idCoche = idCoche;
        this.tipo = tipo;
        this.dinero = dinero;
        this.tiempoEstancia = tiempoEstancia;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public int getIdCoche() {
        return idCoche;
    }

    @Override
    public void run() {
        try {
            Plazas plaza = plazasDisponibles.take();
            plaza.ocupar();
            System.out.printf(ConstantesParkingInteligente.COCHE_HA_APARCADO, idCoche, tipo.getTipo());
            long tiempoEstanciaFinal = Math.round(Math.random() * ConstantesParkingInteligente.ESTANCIA_ALEATORIA) + ConstantesParkingInteligente.ESTANCIA_FIJA;
            Thread.sleep((tiempoEstanciaFinal));
            tiempoEstancia.addAndGet(tiempoEstanciaFinal);
            System.out.printf(ConstantesParkingInteligente.COCHE_HA_SALIDO, idCoche, tipo.getTipo(), plaza.getIdPlaza(),plaza.getTipo(), tipo.getTarifaPorMinuto());
            dinero.addAndGet((int) tipo.getTarifaPorMinuto()*ConstantesParkingInteligente.LONG_Y_SUS_PROBLEMAS);
            plaza.liberar();
            plazasDisponibles.put(plaza);
            semaforoUsado.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getLogger(Coche.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void setPlazasDisponibles(BlockingQueue<Plazas> plazasDisponibles) {
        this.plazasDisponibles = plazasDisponibles;
    }

    public void setSemaforoUsado(Semaphore semaforoUsado) {
        this.semaforoUsado = semaforoUsado;
    }
}
