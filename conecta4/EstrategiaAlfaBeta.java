public class EstrategiaAlfaBeta extends Estrategia {
    private long tiempoTotalMovimientos = 0;
    private int movimientosRealizados = 0;
    private int nodosEvaluados = 0;
    private int busquedasRealizadas = 0;


    protected Evaluador _evaluador;
    protected int _capaMaxima;

    protected int _jugadorMAX;

    public EstrategiaAlfaBeta() {
    }

    public EstrategiaAlfaBeta(Evaluador evaluador, int capaMaxima) {
        this.establecerEvaluador(evaluador);
        this.establecerCapaMaxima(capaMaxima);
    }

    @Override
    public String getNombre() {
        return "AlfaBeta";
    }

    @Override
    protected int getTiempoMedioMovimiento() {
        if (movimientosRealizados == 0) {
            return 0;
        } else {
            return (int) (tiempoTotalMovimientos / movimientosRealizados);
        }
    }

    @Override
    protected String getNumeroNodosEvaluados() {
        return String.valueOf(nodosEvaluados/busquedasRealizadas);
    }

    public int buscarMovimiento(Tablero tablero, int jugador) {
        busquedasRealizadas++;
        long tiempoInicio = System.nanoTime();
        int alpha = _evaluador.MINIMO;
        int beta = _evaluador.MAXIMO;
        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col, valorSucesor;
        int mejorPosicion = -1;
        int mejorValor = _evaluador.MINIMO;

        _jugadorMAX = jugador;
        for (col = 0; col < Tablero.NCOLUMNAS; col++) {
            if (movimientosPosibles[col]) {
                nuevoTablero = (Tablero) tablero.clone();
                nuevoTablero.anadirFicha(col, jugador);
                nuevoTablero.obtenerGanador();
                valorSucesor = MINIMAX_ALFABETA(nuevoTablero, Jugador.alternarJugador(jugador), 1, alpha, beta);
                nodosEvaluados++;
                nuevoTablero = null;
                if (valorSucesor >= mejorValor) {
                    mejorValor = valorSucesor;
                    mejorPosicion = col;
                }
            }
        }
        long tiempoFin = System.nanoTime();
        tiempoTotalMovimientos += tiempoFin - tiempoInicio;
        movimientosRealizados++;
        return (mejorPosicion);
    }

    public int MINIMAX_ALFABETA(Tablero tablero, int jugador, int capa, int alpha, int beta) {
        if (tablero.hayEmpate()) {
            return (0);
        }
        if (tablero.esGanador(_jugadorMAX)) {
            return (_evaluador.MAXIMO);
        }
        if (tablero.esGanador(Jugador.alternarJugador(_jugadorMAX))) {
            return (_evaluador.MINIMO);
        }
        if (capa == (_capaMaxima)) {
            return (_evaluador.valoracion(tablero, _jugadorMAX));
        }

        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col, valor, valorSucesor;

        if (esCapaMIN(capa)) {
            valor = _evaluador.MAXIMO;
            for (col = 0; col < Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) {
                    nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col, jugador);
                    nuevoTablero.obtenerGanador();
                    valorSucesor = MINIMAX_ALFABETA(nuevoTablero, Jugador.alternarJugador(jugador), (capa + 1), alpha, beta);
                    nodosEvaluados++;
                    nuevoTablero = null;
                    valor = minimo2(valor, valorSucesor);
                    beta = minimo2(beta, valor);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        } else {
            valor = _evaluador.MINIMO;
            for (col = 0; col < Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) {
                    nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col, jugador);
                    nuevoTablero.obtenerGanador();
                    valorSucesor = MINIMAX_ALFABETA(nuevoTablero, Jugador.alternarJugador(jugador), (capa + 1), alpha, beta);
                    nodosEvaluados++;
                    nuevoTablero = null;
                    valor = maximo2(valor, valorSucesor);
                    alpha = maximo2(alpha, valor);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return (valor);
    }

    public void establecerCapaMaxima(int capaMaxima) {
        _capaMaxima = capaMaxima;
    }

    public void establecerEvaluador(Evaluador evaluador) {
        _evaluador = evaluador;
    }

    protected static final boolean esCapaMIN(int capa) {
        return ((capa % 2) == 1);
    }

    private static final int maximo2(int v1, int v2) {
        if (v1 > v2)
            return (v1);
        else
            return (v2);
    }

    private static final int minimo2(int v1, int v2) {
        if (v1 < v2)
            return (v1);
        else
            return (v2);
    }
}
