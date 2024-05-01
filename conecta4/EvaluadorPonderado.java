public class EvaluadorPonderado extends Evaluador {
    // Pesos para los rasgos de evaluación
    private static int PESO_FICHAS_EN_LINEA = 100;
    private static int PESO_BLOQUEO_OPPONENTE = -50;
    private static int PESO_CENTRO_TABLERO = 30;
    private static int PESO_CONEXIONES_POTENCIALES = 80;

    // Método para ajustar manualmente los pesos
    public void ajustarPesosManualmente(int PESO_FICHAS_EN_LINEA, int pesoBloqueoOponente, int pesoCentroTablero, int pesoConexionesPotenciales) {
        EvaluadorPonderado.PESO_FICHAS_EN_LINEA = PESO_FICHAS_EN_LINEA;
        EvaluadorPonderado.PESO_BLOQUEO_OPPONENTE = pesoBloqueoOponente;
        EvaluadorPonderado.PESO_CENTRO_TABLERO = pesoCentroTablero;
        EvaluadorPonderado.PESO_CONEXIONES_POTENCIALES = pesoConexionesPotenciales;
    }

    public static void ajustarPesosAutomaticamente(String resultadoUltimaPartida) {
        // Factor de ajuste para incrementar o decrementar los pesos
        double factorAjuste = 0.1; // Este valor puede ser ajustado según sea necesario

        // Ajustar los pesos en función del resultado de la última partida
        if (resultadoUltimaPartida == "GANADO") {
            // Si se ganó la última partida, aumentar los pesos
            PESO_FICHAS_EN_LINEA *= (1 + factorAjuste);
            PESO_BLOQUEO_OPPONENTE *= (1 + factorAjuste);
            PESO_CENTRO_TABLERO *= (1 + factorAjuste);
            PESO_CONEXIONES_POTENCIALES *= (1 + factorAjuste);
        } else if (resultadoUltimaPartida == "PERDIDO") {
            // Si se perdió la última partida, disminuir los pesos
            PESO_FICHAS_EN_LINEA *= (1 - factorAjuste);
            PESO_BLOQUEO_OPPONENTE *= (1 - factorAjuste);
            PESO_CENTRO_TABLERO *= (1 - factorAjuste);
            PESO_CONEXIONES_POTENCIALES *= (1 - factorAjuste);
        } else {
            // No se hace nada si el resultado fue un empate
        }
        // No se hace nada si el resultado fue un empate
    }

    @Override
    public int valoracion(Tablero tablero, int jugador) {
        int valoracion = 0;

        // Evaluación de cantidad de fichas en línea
        valoracion += PESO_FICHAS_EN_LINEA * contarFichasEnLinea(tablero, jugador);

        // Evaluación de bloqueo de oponente
        valoracion += PESO_BLOQUEO_OPPONENTE * contarFichasEnLinea(tablero, Jugador.alternarJugador(jugador));

        // Evaluación del centro del tablero
        valoracion += PESO_CENTRO_TABLERO * evaluarCentro(tablero, jugador);

        // Evaluación de conexiones potenciales
        valoracion += PESO_CONEXIONES_POTENCIALES * contarConexionesPotenciales(tablero, jugador);

        return valoracion;
    }

    // Otros métodos de la clase

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