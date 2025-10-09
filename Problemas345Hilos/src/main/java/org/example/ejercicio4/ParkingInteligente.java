package org.example.ejercicio4;


import org.example.constantes.ConstantesParkingInteligente;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class ParkingInteligente {
    private final Semaphore semaforoPlazasNormales;
    private final Semaphore semaforoPlazasVIP;
    private final BlockingQueue<Coche> colaCoches;
    private final BlockingQueue<Plazas> plazasNormales;
    private final BlockingQueue<Plazas> plazasVIP;
    private final AtomicInteger cochesProcesados;
    private final AtomicInteger cochesAtendidos;
    private final AtomicInteger cochesRechazados;
    private final AtomicLong tiempoTotalEstancia;
    private final AtomicInteger ingresosTotales;
    private final AtomicInteger ocupacionMaximaNormal;
    private final AtomicInteger ocupacionMaximaVIP;
    private boolean terminado;
    private Thread generadorCoches;
    private Thread colaManager;

    public ParkingInteligente() {
        this.terminado = false;
        this.ocupacionMaximaVIP = new AtomicInteger(0);
        this.ingresosTotales = new AtomicInteger(0);
        this.ocupacionMaximaNormal = new AtomicInteger(0);
        this.cochesProcesados = new AtomicInteger(0);
        this.cochesAtendidos = new AtomicInteger(0);
        this.cochesRechazados = new AtomicInteger(0);
        this.tiempoTotalEstancia = new AtomicLong(0);
        this.plazasNormales = new LinkedBlockingQueue<>();
        this.plazasVIP = new LinkedBlockingQueue<>();
        this.colaCoches = new LinkedBlockingQueue<>(ConstantesParkingInteligente.COCHES_PERMITIDOS_EN_COLA);
        this.semaforoPlazasNormales = new Semaphore(ConstantesParkingInteligente.PLAZAS_NORMALES);
        this.semaforoPlazasVIP = new Semaphore(ConstantesParkingInteligente.PLAZAS_VIP);
    }

    public void start() {
        System.out.println(ConstantesParkingInteligente.PARKING_INTELIGENTE);
        iniciarPlazas();
        llegadaCoches();

        try {
            Thread.sleep(ConstantesParkingInteligente.TIEMPO_ANTES_CERRADO);
            terminado = true;
            generadorCoches.join();
            colaManager.join();

            resultadoFinal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void iniciarPlazas() {
        for (int i = 1; i <= ConstantesParkingInteligente.PLAZAS_NORMALES; i++) {
            plazasNormales.add(new Plazas(i, TipoVehiculo.NORMAL));
        }
        for (int i = 1; i <= ConstantesParkingInteligente.PLAZAS_VIP; i++) {
            plazasVIP.add(new Plazas(i, TipoVehiculo.VIP));
        }
    }

    private void llegadaCoches() {
        generadorCoches = new Thread(() -> {
            for (int i = 0; i < ConstantesParkingInteligente.CANTIDAD_COCHES; i++) {
                try {
                    TipoVehiculo tipo = Math.random() < 0.8 ? TipoVehiculo.NORMAL : TipoVehiculo.VIP;
                    Coche coche = new Coche(i, tipo,ingresosTotales,tiempoTotalEstancia);
                    System.out.printf(ConstantesParkingInteligente.COCHE_LLEGO_PARKING, coche.getIdCoche(), coche.getTipo().getTipo());
                    cochesProcesados.incrementAndGet();
                    if (colaCoches.offer(coche)) {
                        System.out.printf(ConstantesParkingInteligente.COCHE_ESPERANDO_COLA, coche.getIdCoche());
                        cochesAtendidos.incrementAndGet();
                    }
                    else {
                        System.out.printf(ConstantesParkingInteligente.COCHE_COLA_LLENA, coche.getIdCoche());
                        cochesRechazados.incrementAndGet();
                    }
                    Thread.sleep(ConstantesParkingInteligente.ESPERADA_LLEGADA_COCHE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        colaCochesMeterseEnSemaforo();
        generadorCoches.start();
    }

    private void colaCochesMeterseEnSemaforo() {
        colaManager = new Thread(() -> {
            while (!terminado ) {
                if (ocupacionMaximaVIP.get() < ConstantesParkingInteligente.PLAZAS_VIP - semaforoPlazasVIP.availablePermits()) {
                    ocupacionMaximaVIP.set(ConstantesParkingInteligente.PLAZAS_VIP - semaforoPlazasVIP.availablePermits());
                }
                if (ocupacionMaximaNormal.get() < ConstantesParkingInteligente.PLAZAS_NORMALES - semaforoPlazasNormales.availablePermits()) {
                    ocupacionMaximaNormal.set(ConstantesParkingInteligente.PLAZAS_NORMALES - semaforoPlazasNormales.availablePermits());
                }
                Coche coche = colaCoches.peek();
                if (coche != null) {
                    if (coche.getTipo() == TipoVehiculo.VIP) {
                        cocheVIPElige();
                    } else {
                        cochePlazasNormales();
                    }
                }
            }
        });
        colaManager.start();
    }
    private void cocheVIPElige() {
        if (Math.random() < 0.3) {
            try {
                if (semaforoPlazasVIP.tryAcquire()){
                    semaforoPlazasVIP.acquire();
                    Coche cocheReal = colaCoches.take();
                    cocheReal.setPlazasDisponibles(plazasVIP);
                    cocheReal.setSemaforoUsado(semaforoPlazasVIP);
                    Thread.ofVirtual().start(cocheReal);
                } else {
                    cochePlazasNormales();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.getLogger(ParkingInteligente.class.getName()).severe(e.getMessage());
            }
        } else {
            cochePlazasNormales();
        }
    }
    private void cochePlazasNormales() {
        try {
            if (semaforoPlazasNormales.tryAcquire()) {
                Coche cocheReal =  colaCoches.take();
                cocheReal.setPlazasDisponibles(plazasNormales);
                cocheReal.setSemaforoUsado(semaforoPlazasNormales);
                Thread.ofVirtual().start(cocheReal);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getLogger(ParkingInteligente.class.getName()).severe(e.getMessage());
        }
    }
    private void resultadoFinal() {
        System.out.println(ConstantesParkingInteligente.RESUMEN_DIA);
        System.out.printf(ConstantesParkingInteligente.VEHICULOS_PROCESADOS, cochesProcesados.get());
        System.out.printf(ConstantesParkingInteligente.VEHICULOS_ATENDIDOS, cochesAtendidos.get());
        System.out.printf(ConstantesParkingInteligente.VEHICULOS_RECHAZADOS, cochesRechazados.get());
        System.out.printf(ConstantesParkingInteligente.TIEMPO_MEDIO_ESTANCIA, tiempoTotalEstancia.get()/cochesAtendidos.get() / 1000);
        System.out.printf(ConstantesParkingInteligente.INGRESOS_TOTALES, ingresosTotales.get()/ConstantesParkingInteligente.LONG_Y_SUS_PROBLEMAS);
        System.out.printf(ConstantesParkingInteligente.OCUPACION_MAXIMA, ocupacionMaximaNormal.get() + ocupacionMaximaVIP.get());
    }
}
