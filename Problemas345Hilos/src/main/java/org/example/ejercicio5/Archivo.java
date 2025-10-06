package org.example.ejercicio5;

public class Archivo {
    private final String nombre;
    private final TipoArchivo tipo;

    public Archivo(String nombre, TipoArchivo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }
    public Archivo(){
        int nombreTipo = (int)(Math.random() * 4);
        switch (nombreTipo){
            case 0 -> this.nombre = "Documento_" + (int)(Math.random() * 1000);
            case 1 -> this.nombre = "Imagen_" + (int)(Math.random() * 1000);
            case 2 -> this.nombre = "Video_" + (int)(Math.random() * 1000);
            case 3 -> this.nombre = "Juego_" + (int)(Math.random() * 1000);
            default -> this.nombre = "Archivo_" + (int)(Math.random() * 1000);
        }
        TipoArchivo[] tipos = TipoArchivo.values();
        this.tipo = tipos[nombreTipo];
    }

    public String getNombre() {
        return nombre;
    }

    public TipoArchivo getTipo() {
        return tipo;
    }
}
