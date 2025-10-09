package org.example.ejercicio3;




import org.example.constantes.ConstantesRestaurante;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cocineros implements Runnable {
    private TipoPlato tipo;
    private final String nombre;
    private final BlockingQueue<Pedidos> mesaPedidos;
    private final AtomicLong servidos;
    private final AtomicLong tiempoEsperaTotal;
    private final AtomicLong tiempoEsperando;
    private final AtomicBoolean terminado;
    public Cocineros(String nombre, BlockingQueue<Pedidos> mesaPedidos, AtomicLong servidos, AtomicLong tiempoEspera, AtomicLong tiempoLibre, AtomicBoolean terminado) {
        this.nombre = nombre;
        this.mesaPedidos = mesaPedidos;
        this.servidos = servidos;
        this.tiempoEsperaTotal = tiempoEspera;
        this.tiempoEsperando = tiempoLibre;
        this.terminado = terminado;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !terminado.get()) {
                long inicioEspera = System.currentTimeMillis();
                Pedidos pedido = mesaPedidos.take();
                long tiempoEspera = System.currentTimeMillis() - inicioEspera;
                tiempoEsperando.addAndGet(tiempoEspera);
                tipo = pedido.getTipo();
                Thread.sleep(tipo.tiempoMs);
                System.out.printf(ConstantesRestaurante.COCINADO, nombre,tipo.nombre);
                servidos.incrementAndGet();
                long tiempoEsperaCliente = System.currentTimeMillis() - pedido.getTiempoEsperando();
                tiempoEsperaTotal.addAndGet(tiempoEsperaCliente);
                pedido.getMesa().notificarPlatoListo();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getLogger(Cocineros.class.getName()).log(Level.SEVERE, ConstantesRestaurante.ERROR_COCINERO + nombre, e);
        }
    }
}
