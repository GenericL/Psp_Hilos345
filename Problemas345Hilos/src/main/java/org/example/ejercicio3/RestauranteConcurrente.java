package org.example.ejercicio3;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class RestauranteConcurrente {
    private final BlockingQueue<Pedidos> pedidosQueue;

    public RestauranteConcurrente() {
        this.pedidosQueue = new ArrayBlockingQueue<>(10);
    }

    public void start(){
        System.out.println("🍽️  === RESTAURANTE CON BLOCKING QUEUE ===\n");
        Thread cocinero1 = new Thread(new Cocineros("Chef Mendax", pedidosQueue), "Chef-Mendax");
        Thread cocinero2 = new Thread(new Cocineros("Chef Soraya", pedidosQueue), "Chef-Soraya");
        Thread cocinero3 = new Thread(new Cocineros("Chef Redei", pedidosQueue), "Chef-Redei");
        cocinero1.start();
        cocinero2.start();
        cocinero3.start();
        llegadaClientes();
        try {
            sleep(20000);
            cocinero1.interrupt();
            cocinero2.interrupt();
            cocinero3.interrupt();
            System.out.println("\n🏪 Restaurante cerrado");
        } catch (InterruptedException e) {
            currentThread().interrupt();
            Logger.getLogger(RestauranteConcurrente.class.getName()).log(Level.SEVERE,"Error en el Restaurante", e);
        }
    }

    private void llegadaClientes(){
        for (int i = 0; i < 100; i++) {
            try {
                pedidosQueue.put(new Pedidos());
            } catch (InterruptedException e) {
                currentThread().interrupt();
                Logger.getLogger(RestauranteConcurrente.class.getName()).log(Level.SEVERE,"Error en los Clientes", e);
            }
        }
    }
}
