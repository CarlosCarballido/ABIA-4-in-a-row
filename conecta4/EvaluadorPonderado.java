import java.util.Random;

public class EvaluadorPonderado extends Evaluador {

    private static final double INITIAL_WEIGHT = 0.1;
    private static final double WEIGHT_INCREMENT = 1.0;

    private double pesoFichasEnLinea;
    private double pesoBloqueoOponente;
    private double pesoCentroTablero;
    private double pesoConexionesPotenciales;

    public EvaluadorPonderado() {
        this.pesoFichasEnLinea = INITIAL_WEIGHT;
        this.pesoBloqueoOponente = INITIAL_WEIGHT;
        this.pesoCentroTablero = INITIAL_WEIGHT;
        this.pesoConexionesPotenciales = INITIAL_WEIGHT;
    }

    public void configurarPesosManualmente(double pesoFichasEnLinea, double pesoBloqueoOponente, double pesoCentroTablero, double pesoConexionesPotenciales) {
        this.pesoFichasEnLinea = pesoFichasEnLinea;
        this.pesoBloqueoOponente = pesoBloqueoOponente;
        this.pesoCentroTablero = pesoCentroTablero;
        this.pesoConexionesPotenciales = pesoConexionesPotenciales;
    }

    public void buscarPesosOptimos(int numeroIteraciones) {
        // Paso 1: Configurar pesos uniformes
        configurarPesosManualmente(INITIAL_WEIGHT, INITIAL_WEIGHT, INITIAL_WEIGHT, INITIAL_WEIGHT);

        // Variable para almacenar los pesos óptimos y su desempeño
        double mejorPesoFichasEnLinea = pesoFichasEnLinea;
        double mejorPesoBloqueoOponente = pesoBloqueoOponente;
        double mejorPesoCentroTablero = pesoCentroTablero;
        double mejorPesoConexionesPotenciales = pesoConexionesPotenciales;

        // Variable para almacenar el puntaje óptimo
        int mejorPuntaje = 0;
        // Variable para contabilizar el número de iteraciones sin mejoras
        int iteracionesSinMejora = 0;

        // Iterar para buscar los pesos óptimos
        for (int i = 0; i < numeroIteraciones; i++) {
            // Paso 2: Generar nuevos conjuntos de pesos
            double[] nuevosPesos = generarNuevosPesos();

            // Variable para almacenar el desempeño del nuevo conjunto de pesos
            int nuevoPuntaje = 0;

            // Enfrentar los juegos de pesos actuales y nuevos en varias partidas
            
                // Alternar quién comienza primero
                if (i % 2 == 0) {
                    nuevoPuntaje += enfrentarJuegosDePesos(nuevosPesos);
                } else {
                    nuevoPuntaje -= enfrentarJuegosDePesos(nuevosPesos);
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

            // Paso 5: Parar la búsqueda si no hay mejoras en las últimas 30 iteraciones
            if (iteracionesSinMejora >= 30) {
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
        double[] nuevosPesos = new double[4];
        nuevosPesos[0] = pesoFichasEnLinea + (new Random().nextDouble() * 2 * WEIGHT_INCREMENT - WEIGHT_INCREMENT);
        nuevosPesos[1] = pesoBloqueoOponente + (new Random().nextDouble() * 2 * WEIGHT_INCREMENT - WEIGHT_INCREMENT);
        nuevosPesos[2] = pesoCentroTablero + (new Random().nextDouble() * 2 * WEIGHT_INCREMENT - WEIGHT_INCREMENT);
        nuevosPesos[3] = pesoConexionesPotenciales + (new Random().nextDouble() * 2 * WEIGHT_INCREMENT - WEIGHT_INCREMENT);
        return nuevosPesos;
    }

    private int enfrentarJuegosDePesos(double[] nuevosPesos) {
        EvaluadorPonderado evaluadorPonderado = new EvaluadorPonderado();
        evaluadorPonderado.configurarPesosManualmente(nuevosPesos[0], nuevosPesos[1], nuevosPesos[2], nuevosPesos[3]);

        Jugador jugador1 = new Jugador(1);
        Jugador jugador2 = new Jugador(2);

        EvaluadorPonderado evaluadorPonderadoJ1 = new EvaluadorPonderado();

        jugador1.establecerEstrategia(new EstrategiaAlfaBeta(evaluadorPonderadoJ1, 4));
        jugador2.establecerEstrategia(new EstrategiaMiniMax(new EvaluadorAleatorio(), 4));

        int puntaje = 0;
        for (int i = 0; i < 10; i++) {
            Tablero tablero = new Tablero();
            int resultado = tablero.jugarPartida(jugador1, jugador2);
            if (resultado == 1) {
                puntaje++;
            } else if (resultado == 2) {
                puntaje--;
            }
        }
        return puntaje;
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