package org.example.ejercicio3;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class RestauranteConcurrente {
    private final BlockingQueue<Mesa> mesasLibres;
    private final BlockingQueue<Pedidos> pedidosQueue;
    private final AtomicLong clientesAtendidos;
    private final AtomicLong tiempoEsperaTotal;
    private final AtomicLong cocineroEsperando;
    private final AtomicLong servidos;
    private int mesasLlenas;
    private final List<Thread> cocinerosList;
    private AtomicBoolean terminado;

    public RestauranteConcurrente() {
        this.cocineroEsperando = new AtomicLong(0);
        this.tiempoEsperaTotal = new AtomicLong(0);;
        this.cocinerosList = new ArrayList<>();
        this.pedidosQueue = new LinkedBlockingQueue<>();
        this.clientesAtendidos = new AtomicLong(0);
        this.servidos = new AtomicLong(0);
        this.mesasLlenas = 0;
        this.mesasLibres = new LinkedBlockingQueue<>();
        terminado = new AtomicBoolean(false);
    }

    public void start(){
        System.out.println("🍽️  === RESTAURANTE CON BLOCKING QUEUE ===\n");
        long inicioSimulacion = System.currentTimeMillis();
        iniciarCocineros();
        iniciarMesa();
        llegadaClientes();
        try {
            terminado.set(true);
            sleep(10000);
            for (Thread thread : cocinerosList) {
                thread.join();
            }

            long tiempoTotal = System.currentTimeMillis() - inicioSimulacion;
            resultadoFinal(tiempoTotal);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            Logger.getLogger(RestauranteConcurrente.class.getName()).log(Level.SEVERE,"Error en el Restaurante", e);
        }
    }

    private void iniciarCocineros(){
        Thread cocinero1 = new Thread(new Cocineros("Chef Mendax", pedidosQueue, servidos,tiempoEsperaTotal,cocineroEsperando,terminado), "Chef-Mendax");
        Thread cocinero2 = new Thread(new Cocineros("Chef Soraya", pedidosQueue, servidos, tiempoEsperaTotal,cocineroEsperando, terminado), "Chef-Soraya");
        Thread cocinero3 = new Thread(new Cocineros("Chef Redei", pedidosQueue, servidos,tiempoEsperaTotal,cocineroEsperando, terminado), "Chef-Redei");
        cocinerosList.add(cocinero1);
        cocinerosList.add(cocinero2);
        cocinerosList.add(cocinero3);
        cocinerosList.forEach(Thread::start);

    }

    private void iniciarMesa(){
        for (int i = 1; i <= 10; i++) {
            Mesa mesa = new Mesa(i);
            mesasLibres.add(mesa);
        }
    }

    private void llegadaClientes(){
        Thread generadorClientes = new Thread(() -> {
            try {
            for (int i = 1; i <= 100; i++) {

                    Mesa mesa = mesasLibres.poll(1000, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (mesa != null) {
                        Cliente cliente = new Cliente(i, pedidosQueue, mesa, mesasLibres, clientesAtendidos);
                        mesa.sentarCliente(cliente);
                        Thread.ofVirtual().start(cliente);
                    } else {
                        mesasLlenas++;
                        System.out.printf("❌ Cliente-%d se va, no hay mesas libres",
                                i);
                    }
                    Thread.sleep(1000);

            }
            } catch (InterruptedException e) {
                currentThread().interrupt();
                Logger.getLogger(RestauranteConcurrente.class.getName()).log(Level.SEVERE,"Error en la llegada de clientes", e);
            }
        });
        generadorClientes.start();
        try {
            generadorClientes.join();
        } catch (InterruptedException e) {
            currentThread().interrupt();
            Logger.getLogger(RestauranteConcurrente.class.getName()).log(Level.SEVERE,"Error en la llegada de clientes", e);
        }
    }

    private void resultadoFinal(long tiempoTotal){
        System.out.println("\n--- ESTADÍSTICAS FINALES ---");
        System.out.printf("Clientes atendidos: %d/%d %s%n",
                clientesAtendidos.get(), 100,
                clientesAtendidos.get() == 100 ? "✅" : "⚠️");
        System.out.printf("Platos servidos: %d%n", servidos.get());
        if (clientesAtendidos.get() > 0) {
            System.out.printf("Tiempo promedio de espera: %.1fs%n",
                    tiempoEsperaTotal.get() / 1000.0 / clientesAtendidos.get());
        }
        System.out.println("Mesa llena (veces): " + mesasLlenas);
        if (tiempoTotal > 0 && clientesAtendidos.get() > 0) {
            int eficiencia = (int)((clientesAtendidos.get() * 100.0) / 100);
            System.out.printf("Eficiencia: %d%%%n", eficiencia);
        }

        System.out.printf("Tiempo total simulación: %.1fs%n", tiempoTotal / 1000.0);
    }
}
