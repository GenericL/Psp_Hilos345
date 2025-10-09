package org.example.constantes;

public class ConstantesRestaurante {
    private ConstantesRestaurante() {}
    public static String TIME_PATTERN = "HH:mm:ss";

    public static String PEDIDO = "%tT 🍽️ Cliente-%d ha pedido: %s";
    public static String ERROR_CLIENTE = "Error en el cliente";
    public static int TIEMPO_ESPERA_CLIENTE = 2000;

    public static String COCINADO = "✅ Cocinero %s ha terminado de cocinar: %s";
    public static String ERROR_COCINERO = "Error en el cocinero ";

    public static String NO_MESAS_LIBRES = "❌ Cliente-%d se va, no hay mesas libres";
    public static String ERROR_RESTAURANTE = "Error en el Restaurante";
    public static String ERROR_LLEGADA_CLIENTES = "Error en la llegada de clientes";
    public static int NUMERO_MESAS = 10;
    public static int NUMERO_CLIENTES = 100;
    public static int TIEMPO_ESPERA_RESTAURANTE = 10000;
    public static int TIMEOUT = 1000;

    public static String RESTAURANTE_BLOCKINGQUEUE = "🍽️  === RESTAURANTE CON BLOCKING QUEUE ===";
    public static String ESTADISTICAS_FINALES = "\n--- ESTADÍSTICAS FINALES ---";
    public static String CLIENTES_ATENDIDOS = "Clientes atendidos: %d/%d %s%n";
    public static String PLATOS_SERVIDOS = "Platos servidos: %d%n";
    public static String TIEMPO_PROMEDIO_ESPERA = "Tiempo promedio de espera: %.1fs%n";
    public static String MESAS_LLENAS = "Mesa llena (veces): ";
    public static String EFICIENCIA = "Eficiencia: %d%%%n";
    public static String TIEMPO_TOTAL = "Tiempo total simulación: %.1fs%n";

}
