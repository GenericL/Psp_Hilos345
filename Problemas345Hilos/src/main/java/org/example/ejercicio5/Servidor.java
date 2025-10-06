package org.example.ejercicio5;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Servidor {
    private final int numberOfThreads;
    private final ExecutorService executor;

    public Servidor() {
        this.numberOfThreads = 5;
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void procesarDescarga(Cliente cliente, Archivo archivo) {
        Future<String> future =
        executor.submit(() -> {
            System.out.printf("Cliente %d: Iniciando descarga del archivo %s%n", cliente.getId(), archivo.getNombre());
            Thread.sleep(archivo.getTipo().getSizeMB());
            String resultado = archivo.getNombre();
            return resultado;
        });
    }
    public void cerrarExecutor() {
        executor.shutdown();
    }
}
