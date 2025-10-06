package org.example.ejercicio5;


import java.util.logging.Logger;

public class SistemaDeDescarga {
    private final Servidor servidor;

    public SistemaDeDescarga() {
        this.servidor = new Servidor();
    }

    public void start() {
        System.out.println("=== GESTOR DE DESCARGAS ===");
        generadorClientes();
    }

    private void generadorClientes() {
        Thread generadorDeClientes = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                Cliente cliente = new Cliente(i, servidor);
                new Thread(cliente).start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Logger.getLogger(SistemaDeDescarga.class.getName()).severe(e.getMessage());
                }
            }
        });
        generadorDeClientes.start();
    }
}
