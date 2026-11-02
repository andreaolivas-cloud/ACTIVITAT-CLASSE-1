import java.util.Scanner;

class Servidor {
    String nombre;
    String origen;
    String critico;
    int intentosTotales;
    int intentosFallidos;
 
    boolean datosValidos(){
        boolean nombreOk = nombre != null && nombre.trim().length() > 0;
        boolean origenOk = origen != null && (origen.trim().equalsIgnoreCase("interno") || origen.trim().equalsIgnoreCase("externo"));
        boolean criticoOk = critico != null && (critico.trim().equalsIgnoreCase("si") || critico.trim().equalsIgnoreCase("no"));
        boolean totalesOk = intentosTotales > 0;
        boolean fallidosOk = intentosFallidos >= 0 && intentosFallidos <= intentosTotales;
        return nombreOk && origenOk && criticoOk && totalesOk && fallidosOk;
    }

    String evaluarEstado(){
        if (!datosValidos()){
            return "DATOS INVALIDOS";
        }
        String o = origen.trim();
        String c = critico.trim();
        if (intentosFallidos >= 5 || (c.equalsIgnoreCase("si") && o.equalsIgnoreCase("externo") && intentosFallidos >= 3)){
            return "BLOQUEADO";
        }
        if (intentosFallidos >= 3 || (o.equalsIgnoreCase("externo") && intentosFallidos >= 2)){
            return "ALERTA";
        }
        return "NORMAL";
    }

    double porcentajeFallos(){
        if (!datosValidos()){
            return 0.0;
        }
        return ((double) intentosFallidos / intentosTotales) * 100;
    }

    void mostrarResumen() {
        if (!datosValidos()) {
            System.out.printf("%s | N/D | %s\n", nombre, evaluarEstado());
        } else {
            System.out.printf("%s | %d/%d | %.1f%% | %s\n", nombre, intentosFallidos, intentosTotales, porcentajeFallos(), evaluarEstado());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA DE MONITOREO DE SERVIDORES ===");
       
        System.out.println("\nIntroduce los datos del Servidor 1:");
        Servidor s1 = leerServidor(scanner);

        System.out.println("\nIntroduce los datos del Servidor 2:");
        Servidor s2 = leerServidor(scanner);

        System.out.println("\n========== REPORTE DE SEGURIDAD ==========");
        s1.mostrarResumen();
        s2.mostrarResumen();
        System.out.println();

        mostrarAtencionPrioritaria(s1, s2);
       
        scanner.close();
    }

    public static Servidor leerServidor(Scanner scanner) {
        Servidor s = new Servidor();
       
        System.out.print("  Nombre: ");
        s.nombre = scanner.next();
       
        System.out.print("  Origen (interno/externo): ");
        s.origen = scanner.next();
       
        System.out.print("  Critico (si/no): ");
        s.critico = scanner.next();
       
        System.out.print("  Intentos totales: ");
        s.intentosTotales = scanner.nextInt();
       
        System.out.print("  Intentos fallidos: ");
        s.intentosFallidos = scanner.nextInt();
       
        return s;
    }

    public static void mostrarAtencionPrioritaria(Servidor s1, Servidor s2) {
        String estado1 = s1.evaluarEstado();
        String estado2 = s2.evaluarEstado();

        if (estado1.equals("DATOS INVALIDOS") || estado2.equals("DATOS INVALIDOS")) {
            System.out.println("ATENCION PRIORITARIA:\nCOMPARACION NO DISPONIBLE");
        } else {
            int prioridad1 = obtenerPrioridad(estado1);
            int prioridad2 = obtenerPrioridad(estado2);

            if (prioridad1 > prioridad2) {
                System.out.println("ATENCION PRIORITARIA:\n" + s1.nombre);
            } else if (prioridad2 > prioridad1) {
                System.out.println("ATENCION PRIORITARIA:\n" + s2.nombre);
            } else {
                System.out.println("ATENCION PRIORITARIA:\nIGUAL PRIORIDAD");
            }
        }
    }

    public static int obtenerPrioridad(String estado) {
        if (estado.equals("BLOQUEADO")) return 3;
        if (estado.equals("ALERTA")) return 2;
        if (estado.equals("NORMAL")) return 1;
        return 0;
    }
}

/*
   PREGUNTAS CONCEPTUALES:
   
   1. Por que dos objetos de la misma clase pueden almacenar valores diferentes en sus atributos?
      Porque cada objeto tiene su propia instancia en memoria con sus propios atributos independientes,
      lo que significa que modificar los valores de un objeto no altera los del otro.

   2. Que diferencia existe entre "Servidor b = a;" y "Servidor b = new Servidor();"?
      - "Servidor b = a;" copia la referencia en memoria, haciendo que ambas variables apunten al mismo objeto
        (si modificas 'b', tambien se modifica 'a').
      - "Servidor b = new Servidor();" crea un objeto completamente nuevo e independiente en memoria.

   3. Por que es importante comprobar "datosValidos()" antes de calcular el porcentaje?
      Para evitar errores logicos o excepciones en tiempo de ejecucion, como una division por cero
      (cuando intentosTotales es 0) o procesar datos incorrectos que distorsionen los resultados.
*/
