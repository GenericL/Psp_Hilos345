package org.example.ejercicio3;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class Cliente implements Runnable {
    private TipoPlato tipo;
    private final int id;
    private final BlockingQueue<Pedidos> mesaPedidos;
    private final Mesa mesa;
    private final BlockingQueue<Mesa> mesasLibres;
    private final AtomicLong atendidos;

    public Cliente(int id, BlockingQueue<Pedidos> mesaPedidos, Mesa mesa, BlockingQueue<Mesa> mesasLibres, AtomicLong atendidos) {
        this.id = id;
        this.mesaPedidos = mesaPedidos;
        this.mesa = mesa;
        this.atendidos = atendidos;
        this.tipo = TipoPlato.values()[(int) (Math.random() * TipoPlato.values().length)];

        this.mesasLibres = mesasLibres;
    }


    @Override
    public void run() {
        String tiempo = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(tiempo + "🍽️ Cliente-" + id + " ha pedido: " + tipo.nombre);
        try {
            mesaPedidos.put(new Pedidos(id, tipo,mesa));
            mesa.esperarPlatoListo();
            Thread.sleep(2000);
            mesa.liberarMesa();
            mesasLibres.put(mesa);
            atendidos.incrementAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getLogger(Cliente.class.getName()).severe("Error en el cliente " + id + ": " + e.getMessage());
        }
    }

    public TipoPlato getTipo() {
        return tipo;
    }

    public int getId() {
        return id;
    }
}
