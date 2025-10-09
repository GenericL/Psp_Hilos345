package org.example.ejercicio3;


import org.example.constantes.ConstantesRestaurante;

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
    private final AtomicBoolean terminado;

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
        System.out.println(ConstantesRestaurante.RESTAURANTE_BLOCKINGQUEUE);
        long inicioSimulacion = System.currentTimeMillis();
        iniciarCocineros();
        iniciarMesa();
        llegadaClientes();
        try {
            terminado.set(true);
            sleep(ConstantesRestaurante.TIEMPO_ESPERA_RESTAURANTE);
            for (Thread thread : cocinerosList) {
                thread.join();
            }

            long tiempoTotal = System.currentTimeMillis() - inicioSimulacion;
            resultadoFinal(tiempoTotal);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            Logger.getLogger(RestauranteConcurrente.class.getName()).log(Level.SEVERE,ConstantesRestaurante.ERROR_RESTAURANTE, e);
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
        for (int i = 1; i <= ConstantesRestaurante.NUMERO_MESAS; i++) {
            Mesa mesa = new Mesa(i);
            mesasLibres.add(mesa);
        }
    }

    private void llegadaClientes(){
        Thread generadorClientes = new Thread(() -> {
            try {
            for (int i = 1; i <= ConstantesRestaurante.NUMERO_CLIENTES; i++) {

                    Mesa mesa = mesasLibres.poll(ConstantesRestaurante.TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (mesa != null) {
                        Cliente cliente = new Cliente(i, pedidosQueue, mesa, mesasLibres, clientesAtendidos);
                        mesa.sentarCliente(cliente);
                        Thread.ofVirtual().start(cliente);
                    } else {
                        mesasLlenas++;
                        System.out.printf(ConstantesRestaurante.NO_MESAS_LIBRES,
                                i);
                    }
                    Thread.sleep(ConstantesRestaurante.TIMEOUT);

            }
            } catch (InterruptedException e) {
                currentThread().interrupt();
                Logger.getLogger(RestauranteConcurrente.class.getName()).log(Level.SEVERE,ConstantesRestaurante.ERROR_LLEGADA_CLIENTES, e);
            }
        });
        generadorClientes.start();
        try {
            generadorClientes.join();
        } catch (InterruptedException e) {
            currentThread().interrupt();
            Logger.getLogger(RestauranteConcurrente.class.getName()).log(Level.SEVERE,ConstantesRestaurante.ERROR_LLEGADA_CLIENTES, e);
        }
    }

    private void resultadoFinal(long tiempoTotal){
        System.out.println(ConstantesRestaurante.ESTADISTICAS_FINALES);
        System.out.printf(ConstantesRestaurante.CLIENTES_ATENDIDOS,
                clientesAtendidos.get(), 100,
                clientesAtendidos.get() == 100 ? "✅" : "⚠️");
        System.out.printf(ConstantesRestaurante.PLATOS_SERVIDOS, servidos.get());
        if (clientesAtendidos.get() > 0) {
            System.out.printf(ConstantesRestaurante.TIEMPO_PROMEDIO_ESPERA,
                    tiempoEsperaTotal.get() / 1000.0 / clientesAtendidos.get());
        }
        System.out.println(ConstantesRestaurante.MESAS_LLENAS + mesasLlenas);
        if (tiempoTotal > 0 && clientesAtendidos.get() > 0) {
            int eficiencia = (int)((clientesAtendidos.get() * 100.0) / 100);
            System.out.printf(ConstantesRestaurante.EFICIENCIA, eficiencia);
        }

        System.out.printf(ConstantesRestaurante.TIEMPO_TOTAL, tiempoTotal / 1000.0);
    }
}
