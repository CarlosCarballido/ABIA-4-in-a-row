public class EstrategiaAlfaBeta extends EstrategiaMiniMax {

    private long tiempoTotalMovimientos = 0;
    private int movimientosRealizados = 0;
    private int nodosEvaluados = 0; // New variable to count nodes evaluated

    public EstrategiaAlfaBeta() {
        super();
    }

    public EstrategiaAlfaBeta(Evaluador evaluador, int capaMaxima) {
        super(evaluador, capaMaxima);
    }

    @Override
    protected int getTiempoMedioMovimiento() {
        if (movimientosRealizados == 0) {
            return 0; // No se ha realizado ningún movimiento, tiempo medio es 0
        } else {
            return (int) (tiempoTotalMovimientos / movimientosRealizados); // Tiempo medio en milisegundos
        }
    }
    
    @Override
    public String getNombre() {
        return "AlfaBeta";
    }

    @Override
    protected String getNumeroNodosEvaluados() {
        return String.valueOf(nodosEvaluados); // Return the number of nodes evaluated
    }

    public int ALFABETA(Tablero tablero, int jugador, int capa, int alfa, int beta) {
        long tiempoInicio = System.currentTimeMillis();
         // Increment the counter for each node evaluated
        if (tablero.hayEmpate()) {
            return 0;
        }
        if (tablero.esGanador(super._jugadorMAX)) {
            return super._evaluador.MAXIMO;
        }
        if (tablero.esGanador(Jugador.alternarJugador(super._jugadorMAX))) {
            return super._evaluador.MINIMO;
        }
        if (capa == super._capaMaxima) {
            return super._evaluador.valoracion(tablero, super._jugadorMAX);
        }

        boolean movimientosPosibles[] = tablero.columnasLibres();

        if (esCapaMIN(capa)) {
            for (int col = 0; col < Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) {
                    Tablero nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col, jugador);
                    nuevoTablero.obtenerGanador();
                    nodosEvaluados++;
                    beta = minimo2(beta, ALFABETA(nuevoTablero, Jugador.alternarJugador(jugador), capa + 1, alfa, beta));
                    nuevoTablero = null;
                    if (beta <= alfa) {
                        return beta;
                    }
                }
            }
            movimientosRealizados++;
            long tiempoFin = System.currentTimeMillis();
            tiempoTotalMovimientos += (tiempoFin - tiempoInicio);
            return beta;
        } else {
            for (int col = 0; col < Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) {
                    Tablero nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col, jugador);
                    nuevoTablero.obtenerGanador();
                    nodosEvaluados++;
                    alfa = maximo2(alfa, ALFABETA(nuevoTablero, Jugador.alternarJugador(jugador), capa + 1, alfa, beta));
                    nuevoTablero = null;
                    if (beta <= alfa) {
                        return alfa;
                    }
                }
            }
            movimientosRealizados++;
            long tiempoFin = System.currentTimeMillis();
            tiempoTotalMovimientos += (tiempoFin - tiempoInicio);
            return alfa;
        }
    }
}
