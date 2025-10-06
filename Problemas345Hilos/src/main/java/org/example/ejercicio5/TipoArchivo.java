package org.example.ejercicio5;

public enum TipoArchivo {
    DOCUMENTO(10),    // 10MB  -> 1s descarga
    IMAGEN(50),       // 50MB  -> 5s descarga
    VIDEO(500),       // 500MB -> 50s descarga
    JUEGO(2000);      // 2GB   -> 200s descarga

    private final int sizeMB;

    TipoArchivo(int sizeMB) {
        this.sizeMB = sizeMB;
    }

    public int getSizeMB() {
        return sizeMB;
    }
}
