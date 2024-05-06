public class PruebasMiniMax {

    public static void main(String[] args) {
        // Crear un tablero
        Tablero tablero = new Tablero();
        
        // Crear jugadores con la estrategia MINIMAX sin poda ALFA-BETA
        Jugador jugador1 = new Jugador(1);
        Jugador jugador2 = new Jugador(2);
        
        // Establecer la estrategia MINIMAX sin poda ALFA-BETA con diferentes profundidades
        EstrategiaMiniMax minimaxProfundidad3 = new EstrategiaMiniMax(new EvaluadorAleatorio(), 3);
        EstrategiaMiniMax minimaxProfundidad4 = new EstrategiaMiniMax(new EvaluadorAleatorio(), 4);
        EstrategiaMiniMax minimaxProfundidad5 = new EstrategiaMiniMax(new EvaluadorAleatorio(), 5);
        EstrategiaMiniMax minimaxProfundidad6 = new EstrategiaMiniMax(new EvaluadorAleatorio(), 6);
        
        jugador1.establecerEstrategia(minimaxProfundidad3);
        jugador2.establecerEstrategia(minimaxProfundidad3);
        
        // Ejecutar pruebas con diferentes profundidades
        ejecutarPrueba(tablero, jugador1, jugador2, 3);
        ejecutarPrueba(tablero, jugador1, jugador2, 4);
        ejecutarPrueba(tablero, jugador1, jugador2, 5);
        ejecutarPrueba(tablero, jugador1, jugador2, 6);
    }
    
    public static void ejecutarPrueba(Tablero tablero, Jugador jugador1, Jugador jugador2, int profundidad) {        
        // Ejecutar la partida
        tablero.jugarPartida(jugador1, jugador2);
        
        // Imprimir el resultado de la partida
        System.out.println("Profundidad: " + profundidad + ", Heurística: MINIMAX sin poda ALFA-BETA, Resultado: " + tablero.ganador());
    }
}
