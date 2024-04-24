public class EstrategiaMiniMax_AlfaBeta extends EstrategiaMiniMax {
    public EstrategiaMiniMax_AlfaBeta() {
        super();
    }

    public EstrategiaMiniMax_AlfaBeta(Evaluador evaluador, int capaMaxima) {
        super(evaluador, capaMaxima);
    }

    public int MINIMAX(Tablero tablero, int jugador, int capa, int alfa, int beta) {
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

                    beta = minimo2(beta, MINIMAX(nuevoTablero, Jugador.alternarJugador(jugador), capa + 1, alfa, beta));
                    nuevoTablero = null;
                    if (beta <= alfa) {
                        return beta;
                    }
                }
            }
            return beta;
        } else {
            for (int col = 0; col < Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) {
                    Tablero nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col, jugador);
                    nuevoTablero.obtenerGanador();

                    alfa = maximo2(alfa, MINIMAX(nuevoTablero, Jugador.alternarJugador(jugador), capa + 1, alfa, beta));
                    nuevoTablero = null;
                    if (beta <= alfa) {
                        return alfa;
                    }
                }
            }
            return alfa;
        }
    }
}
