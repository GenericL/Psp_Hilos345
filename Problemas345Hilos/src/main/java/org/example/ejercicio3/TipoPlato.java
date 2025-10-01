package org.example.ejercicio3;

public enum TipoPlato {
    ENSALADA( "Ensalada",2000),    // 2 segundos
    PASTA("Pasta",3000),       // 3 segundos
    PIZZA("Pizza",4000),       // 4 segundos
    CARNE("Carne",5000)       // 5 segundos
    ;
    public final int tiempoMs;
    public final String nombre;
    TipoPlato(String nombre, int tiempoMs ) {
        this.tiempoMs = tiempoMs;
        this.nombre = nombre;
    }
}
