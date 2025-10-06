package org.example.ejercicio5;

public class Cliente implements Runnable {
    private final int id;
    private final Servidor servidor;

    public Cliente(int id, Servidor servidor) {
        this.id = id;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        Archivo archivo = new Archivo();
        servidor.procesarDescarga(this, archivo);
    }

    public int getId() {
        return id;
    }
}
