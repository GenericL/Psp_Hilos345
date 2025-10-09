package org.example.constantes;

public class ConstantesParkingInteligente {
    private ConstantesParkingInteligente() {}
    public static String TIME_PATTERN = "HH:mm:ss";

    public static String COCHE_HA_APARCADO = "Coche-%d (%s) ha aparcado.%n";
    public static String COCHE_HA_SALIDO = "Coche-%d (%s) sale - Plaza-%s %s - Pagó %.2f€ %n";
    public static String COCHE_LLEGO_PARKING = "Coche-%d (%s) ha llegado al parking.%n";
    public static String COCHE_ESPERANDO_COLA = "Coche-%d esperando en cola.%n";
    public static String COCHE_COLA_LLENA = "Coche-%d se ha ido, cola llena.%n";
    public static long ESTANCIA_FIJA = 10000;
    public static long ESTANCIA_ALEATORIA = 20000;
    public static int CANTIDAD_COCHES = 30;

    public static String PARKING_INTELIGENTE = "--- Parking Inteligente ---";
    public static String RESUMEN_DIA = "--- RESUMEN DEL DÍA ---";
    public static String VEHICULOS_PROCESADOS = "Vehículos procesados: %d%n";
    public static String VEHICULOS_ATENDIDOS = "Vehículos atendidos: %d%n";
    public static String VEHICULOS_RECHAZADOS = "Vehículos rechazados: %d%n";
    public static String TIEMPO_MEDIO_ESTANCIA = "Tiempo promedio de estancia: %ds%n";
    public static String INGRESOS_TOTALES = "Ingresos totales: %d€%n";
    public static String OCUPACION_MAXIMA = "Ocupación máxima: %d";
    public static long TIEMPO_ANTES_CERRADO = 50000;
    public static int ESPERADA_LLEGADA_COCHE = 1000;
    public static int PLAZAS_VIP = 5;
    public static int PLAZAS_NORMALES = 20;
    public static int COCHES_PERMITIDOS_EN_COLA = 10;

    public static int LONG_Y_SUS_PROBLEMAS = 10;
}

