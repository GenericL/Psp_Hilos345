package org.example.ejercicio3;

import java.util.concurrent.CountDownLatch;

public class Mesa {
    private final int numero;
    private Cliente cliente;
    private final CountDownLatch latch;

    public Mesa(int idMesa) {
        this.numero = idMesa;
        this.latch = new CountDownLatch(1);
    }
    public synchronized void sentarCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void esperarPlatoListo() throws InterruptedException {
        latch.await();
    }
    public synchronized void liberarMesa() {
        this.cliente = null;
    }
    public void notificarPlatoListo() {
        latch.countDown();
    }
}
