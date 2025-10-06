package org.example.ejercicio5;

import java.util.concurrent.CompletableFuture;
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

    public Future<String> procesarDescarga(Cliente cliente, Archivo archivo) {
        CompletableFuture<String> future = new CompletableFuture<>();
        executor.submit(() -> {

        });
        return future;
    }
    public void cerrarExecutor() {
        executor.shutdown();
    }
}
