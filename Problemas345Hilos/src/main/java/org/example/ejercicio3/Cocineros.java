package org.example.ejercicio3;




import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cocineros implements Runnable {
    private TipoPlato tipo;
    private final String nombre;
    private final BlockingQueue<Pedidos> mesaPedidos;
    public Cocineros(String nombre, BlockingQueue<Pedidos> mesaPedidos) {
        this.nombre = nombre;
        this.mesaPedidos = mesaPedidos;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Pedidos pedido = mesaPedidos.take();
                tipo = pedido.getTipo();
                Thread.sleep(tipo.tiempoMs);
                System.out.println("✅ Cocinero " + nombre + " ha terminado de cocinar: " + tipo.nombre);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getLogger(Cocineros.class.getName()).log(Level.SEVERE, "Error en el cocinero " + nombre, e);
        }
    }
}
