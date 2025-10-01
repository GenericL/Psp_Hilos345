package org.example.ejercicio3;




import java.util.concurrent.BlockingQueue;
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
    public Cocineros(String nombre, BlockingQueue<Pedidos> mesaPedidos, AtomicLong servidos, AtomicLong tiempoEspera, AtomicLong tiempoLibre) {
        this.nombre = nombre;
        this.mesaPedidos = mesaPedidos;
        this.servidos = servidos;
        this.tiempoEsperaTotal = tiempoEspera;
        this.tiempoEsperando = tiempoLibre;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                long inicioEspera = System.currentTimeMillis();
                Pedidos pedido = mesaPedidos.take();
                long tiempoEspera = System.currentTimeMillis() - inicioEspera;
                tiempoEsperando.addAndGet(tiempoEspera);
                tipo = pedido.getTipo();
                Thread.sleep(tipo.tiempoMs);
                System.out.println("✅ Cocinero " + nombre + " ha terminado de cocinar: " + tipo.nombre);
                servidos.incrementAndGet();
                long tiempoEsperaCliente = System.currentTimeMillis() - pedido.getTiempoEsperando();
                tiempoEsperaTotal.addAndGet(tiempoEsperaCliente);
                pedido.getMesa().notificarPlatoListo();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getLogger(Cocineros.class.getName()).log(Level.SEVERE, "Error en el cocinero " + nombre, e);
        }
    }
}
