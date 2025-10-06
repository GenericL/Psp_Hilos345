package org.example.ejercicio4;


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
        this.colaCoches = new LinkedBlockingQueue<>(10);
        this.semaforoPlazasNormales = new Semaphore(20);
        this.semaforoPlazasVIP = new Semaphore(5);
    }

    public void start() {
        System.out.println("--- Parking Inteligente ---");
        iniciarPlazas();
        llegadaCoches();

        try {
            Thread.sleep(50000);
            generadorCoches.join();
            colaManager.join();
            terminado = true;
            resultadoFinal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void iniciarPlazas() {
        for (int i = 1; i <= 20; i++) {
            plazasNormales.add(new Plazas(i, TipoVehiculo.NORMAL));
        }
        for (int i = 1; i <= 5; i++) {
            plazasVIP.add(new Plazas(i, TipoVehiculo.VIP));
        }
    }

    private void llegadaCoches() {
        generadorCoches = new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                try {
                    TipoVehiculo tipo = Math.random() < 0.8 ? TipoVehiculo.NORMAL : TipoVehiculo.VIP;
                    Coche coche = new Coche(i, tipo,ingresosTotales,tiempoTotalEstancia);
                    System.out.printf("Coche-%d (%s) ha llegado al parking.%n", coche.getIdCoche(), coche.getTipo().getTipo());
                    cochesProcesados.incrementAndGet();
                    if (colaCoches.offer(coche)) {
                        System.out.println("Coche-" + coche.getIdCoche() + " esperando en cola.");
                        cochesAtendidos.incrementAndGet();
                    }
                    else {
                        System.out.println("Coche-" + coche.getIdCoche() + " se ha ido, cola llena.");
                        cochesRechazados.incrementAndGet();
                    }
                    Thread.sleep(1000);
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
            while (!colaCoches.isEmpty() || !terminado) {
                if (ocupacionMaximaVIP.get() < 5 - semaforoPlazasVIP.availablePermits()) {
                    ocupacionMaximaVIP.set(5 - semaforoPlazasVIP.availablePermits());
                }
                if (ocupacionMaximaNormal.get() < 20 - semaforoPlazasNormales.availablePermits()) {
                    ocupacionMaximaNormal.set(20 - semaforoPlazasNormales.availablePermits());
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
        System.out.println("--- RESUMEN DEL DÍA ---");
        System.out.printf("Vehiculos procesados: %d%n", cochesProcesados.get());
        System.out.printf("Vehiculos atendidos: %d%n", cochesAtendidos.get());
        System.out.printf("Vehiculos rechazados: %d%n", cochesRechazados.get());
        System.out.printf("Tiempo promedio de estancia: %ds%n", tiempoTotalEstancia.get()/cochesAtendidos.get() / 1000);
        System.out.printf("Ingresos totales: %d€%n", ingresosTotales.get()/10);
        System.out.printf("Ocupación máxima: %d", ocupacionMaximaNormal.get() + ocupacionMaximaVIP.get());
    }
}
