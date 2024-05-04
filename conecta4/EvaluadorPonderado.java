import java.util.ArrayList;
import java.util.Random;

public class EvaluadorPonderado extends Evaluador {

    double pesoFichasEnLinea;
    double pesoBloqueoOponente;
    double pesoCentroTablero;
    double pesoConexionesPotenciales;

    public EvaluadorPonderado() {
        // Pesos iniciales uniformes (todos a 1.0)
        this.pesoFichasEnLinea = 1.0;
        this.pesoBloqueoOponente = 1.0;
        this.pesoCentroTablero = 1.0;
        this.pesoConexionesPotenciales = 1.0;
    }

    public EvaluadorPonderado(double pesoFichasEnLinea, double pesoBloqueoOponente, double pesoCentroTablero, double pesoConexionesPotenciales) {
        this.pesoFichasEnLinea = pesoFichasEnLinea;
        this.pesoBloqueoOponente = pesoBloqueoOponente;
        this.pesoCentroTablero = pesoCentroTablero;
        this.pesoConexionesPotenciales = pesoConexionesPotenciales;
    }

    public void configurarPesosIniciales(double pesoInicial) {
        this.pesoFichasEnLinea = pesoInicial;
        this.pesoBloqueoOponente = pesoInicial;
        this.pesoCentroTablero = pesoInicial;
        this.pesoConexionesPotenciales = pesoInicial;
    }

    // Implementación de los métodos para la búsqueda iterativa de los pesos óptimos
    public void buscarPesosOptimos(int numeroIteraciones, int numeroPartidasPorIteracion) {
        // Paso 1: Configurar pesos uniformes
        configurarPesosIniciales(1.0);

        // Variables para almacenar los pesos óptimos y su desempeño
        double mejorPesoFichasEnLinea = pesoFichasEnLinea;
        double mejorPesoBloqueoOponente = pesoBloqueoOponente;
        double mejorPesoCentroTablero = pesoCentroTablero;
        double mejorPesoConexionesPotenciales = pesoConexionesPotenciales;
        int mejorPuntaje = evaluarDesempenio();

        // Variable para contabilizar el número de iteraciones sin mejoras
        int iteracionesSinMejora = 0;

        // Iterar para buscar los pesos óptimos
        for (int iteracion = 0; iteracion < numeroIteraciones; iteracion++) {
            // Paso 2: Generar nuevos conjuntos de pesos
            double[] nuevosPesos = generarNuevosPesos();

            // Variable para almacenar el desempeño del nuevo conjunto de pesos
            int nuevoPuntaje = 0;

            // Enfrentar los juegos de pesos actuales y nuevos en varias partidas
            for (int i = 0; i < numeroPartidasPorIteracion; i++) {
                nuevoPuntaje += enfrentarJuegosDePesos(nuevosPesos);
            }

            // Paso 4: Actualizar los pesos si el nuevo conjunto es mejor
            if (nuevoPuntaje > mejorPuntaje) {
                mejorPuntaje = nuevoPuntaje;
                mejorPesoFichasEnLinea = nuevosPesos[0];
                mejorPesoBloqueoOponente = nuevosPesos[1];
                mejorPesoCentroTablero = nuevosPesos[2];
                mejorPesoConexionesPotenciales = nuevosPesos[3];
                iteracionesSinMejora = 0;
            } else {
                iteracionesSinMejora++;
            }

            // Paso 5: Parar la búsqueda si no hay mejoras en las últimas 3 iteraciones
            if (iteracionesSinMejora >= 3) {
                break;
            }
        }

        // Imprimir los pesos óptimos
        System.out.println("Pesos óptimos encontrados:");
        System.out.println("Peso Fichas en línea: " + mejorPesoFichasEnLinea);
        System.out.println("Peso Bloqueo Oponente: " + mejorPesoBloqueoOponente);
        System.out.println("Peso Centro Tablero: " + mejorPesoCentroTablero);
        System.out.println("Peso Conexiones Potenciales: " + mejorPesoConexionesPotenciales);
    }

    private double[] generarNuevosPesos() {
        // Incrementar o decrementar un 10% los pesos actuales
        double incremento = 0.1;
        double[] nuevosPesos = {pesoFichasEnLinea, pesoBloqueoOponente, pesoCentroTablero, pesoConexionesPotenciales};
        for (int i = 0; i < nuevosPesos.length; i++) {
            nuevosPesos[i] += nuevosPesos[i] * incremento;
            // Asegurar que los pesos no sean negativos
            if (nuevosPesos[i] < 0) {
                nuevosPesos[i] = 0;
            }
        }
        return nuevosPesos;
    }

    // Método auxiliar para enfrentar juegos de pesos y contar el resultado
    private int enfrentarJuegosDePesos(double[] pesosOponente) {
        // Crear dos evaluadores ponderados con los pesos actuales y los pesos del oponente
        EvaluadorPonderado evaluadorActual = new EvaluadorPonderado(pesoFichasEnLinea, pesoBloqueoOponente, pesoCentroTablero, pesoConexionesPotenciales);
        EvaluadorPonderado evaluadorOponente = new EvaluadorPonderado(pesosOponente[0], pesosOponente[1], pesosOponente[2], pesosOponente[3]);

        // Crear dos jugadores con los evaluadores correspondientes
        Jugador jugadorActual = new Jugador(1);
        jugadorActual.establecerEstrategia(evaluadorActual);
        Jugador jugadorOponente = new Jugador(2);
        jugadorOponente.establecerEstrategia(evaluadorOponente);

        // Jugar una partida entre los dos jugadores
        Tablero tablero = new Tablero();
        String resultado = jugarPartida(tablero, jugadorActual, jugadorOponente);

        // Contabilizar el resultado
        if (resultado.equals("GANADO_J1")) {
            return 1;
        } else if (resultado.equals("GANADO_J2")) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int valoracion(Tablero tablero, int jugador) {
        int valoracion = 0;

        // Evaluación de cantidad de fichas en línea
        valoracion += pesoFichasEnLinea * contarFichasEnLinea(tablero, jugador);

        // Evaluación de bloqueo de oponente
        valoracion += pesoBloqueoOponente * contarFichasEnLinea(tablero, Jugador.alternarJugador(jugador));

        // Evaluación del centro del tablero
        valoracion += pesoCentroTablero * evaluarCentro(tablero, jugador);

        // Evaluación de conexiones potenciales
        valoracion += pesoConexionesPotenciales * contarConexionesPotenciales(tablero, jugador);

        return valoracion;
    }

    // Método auxiliar para contar la cantidad de fichas del jugador en línea
    private int contarFichasEnLinea(Tablero tablero, int jugador) {
        int fichasEnLinea = 0;
        for (int fila = 0; fila < Tablero.NFILAS; fila++) {
            for (int columna = 0; columna < Tablero.NCOLUMNAS; columna++) {
                if (tablero.obtenerCasilla(fila, columna) == jugador) {
                    // Contar horizontalmente
                    fichasEnLinea += contarFichasEnDireccion(tablero, jugador, fila, columna, 0, 1);
                    // Contar verticalmente
                    fichasEnLinea += contarFichasEnDireccion(tablero, jugador, fila, columna, 1, 0);
                    // Contar diagonalmente (hacia la derecha y hacia abajo)
                    fichasEnLinea += contarFichasEnDireccion(tablero, jugador, fila, columna, 1, 1);
                    // Contar diagonalmente (hacia la izquierda y hacia abajo)
                    fichasEnLinea += contarFichasEnDireccion(tablero, jugador, fila, columna, 1, -1);
                }
            }
        }
        return fichasEnLinea;
    }

    // Método auxiliar para contar fichas en una dirección
    private int contarFichasEnDireccion(Tablero tablero, int jugador, int fila, int columna, int dirFila, int dirColumna) {
        int contador = 0;
        int filaActual = fila;
        int columnaActual = columna;
        while (tablero.esCasillaValida(filaActual, columnaActual) && tablero.obtenerCasilla(filaActual, columnaActual) == jugador) {
            contador++;
            filaActual += dirFila;
            columnaActual += dirColumna;
        }
        return contador >= 4 ? 1 : 0; // Se devuelve 1 si hay 4 fichas en línea, 0 en caso contrario
    }

    // Método auxiliar para evaluar la presencia de fichas del jugador en el centro del tablero
    private int evaluarCentro(Tablero tablero, int jugador) {
        int centro = 0;
        int centroColumna = Tablero.NCOLUMNAS / 2;
        for (int fila = 0; fila < Tablero.NFILAS; fila++) {
            if (tablero.obtenerCasilla(fila, centroColumna) == jugador) {
                centro++;
            }
        }
        return centro;
    }

    // Método auxiliar para contar las conexiones potenciales del jugador
    private int contarConexionesPotenciales(Tablero tablero, int jugador) {
        int conexionesPotenciales = 0;

        // Iterar sobre todas las posiciones del tablero
        for (int fila = 0; fila < Tablero.NFILAS; fila++) {
            for (int columna = 0; columna < Tablero.NCOLUMNAS; columna++) {
                // Si la casilla está ocupada por una ficha del jugador
                if (tablero.obtenerCasilla(fila, columna) == jugador) {
                    // Verificar conexiones potenciales en todas las direcciones
                    conexionesPotenciales += contarConexionesPotencialesEnDireccion(tablero, jugador, fila, columna, -1, 0); // Izquierda
                    conexionesPotenciales += contarConexionesPotencialesEnDireccion(tablero, jugador, fila, columna, 1, 0);  // Derecha
                    conexionesPotenciales += contarConexionesPotencialesEnDireccion(tablero, jugador, fila, columna, 0, -1); // Arriba
                    conexionesPotenciales += contarConexionesPotencialesEnDireccion(tablero, jugador, fila, columna, 0, 1);  // Abajo
                    conexionesPotenciales += contarConexionesPotencialesEnDireccion(tablero, jugador, fila, columna, -1, -1); // Diagonal superior izquierda
                    conexionesPotenciales += contarConexionesPotencialesEnDireccion(tablero, jugador, fila, columna, -1, 1);  // Diagonal superior derecha
                    conexionesPotenciales += contarConexionesPotencialesEnDireccion(tablero, jugador, fila, columna, 1, -1);  // Diagonal inferior izquierda
                    conexionesPotenciales += contarConexionesPotencialesEnDireccion(tablero, jugador, fila, columna, 1, 1);   // Diagonal inferior derecha
                }
            }
        }
        return conexionesPotenciales;
    }

    // Método auxiliar para contar las conexiones potenciales en una dirección
    private int contarConexionesPotencialesEnDireccion(Tablero tablero, int jugador, int fila, int columna, int dirFila, int dirColumna) {
        // Posición actual
        int filaActual = fila;
        int columnaActual = columna;
        // Contador de conexiones potenciales
        int conexionesPotenciales = 0;
        // Contador de fichas del jugador
        int fichasJugador = 0;
        // Contador de fichas del oponente
        int fichasOponente = 0;

        // Moverse en la dirección dada y contar las fichas
        while (tablero.esCasillaValida(filaActual, columnaActual) && tablero.obtenerCasilla(filaActual, columnaActual) == jugador) {
            fichasJugador++;
            filaActual += dirFila;
            columnaActual += dirColumna;
        }
        // Verificar si hay un espacio vacío después de las fichas del jugador
        if (tablero.esCasillaValida(filaActual, columnaActual) && tablero.obtenerCasilla(filaActual, columnaActual) == Tablero.CASILLA_VACIA) {
            filaActual += dirFila;
            columnaActual += dirColumna;
            // Moverse en la misma dirección y contar las fichas del oponente
            while (tablero.esCasillaValida(filaActual, columnaActual) && tablero.obtenerCasilla(filaActual, columnaActual) == Jugador.alternarJugador(jugador)) {
                fichasOponente++;
                filaActual += dirFila;
                columnaActual += dirColumna;
            }
            // Verificar si hay al menos una ficha del oponente después del espacio vacío
            if (fichasOponente > 0) {
                // Si hay al menos tres fichas del jugador y una del oponente, hay una conexión potencial
                if (fichasJugador >= 3) {
                    conexionesPotenciales++;
                }
            }
        }
        return conexionesPotenciales;
    }
}