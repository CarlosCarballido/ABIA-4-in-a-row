public class PruebasMiniMax {

    public static void main(String[] args) {
        // Crear un tablero
        Tablero tablero = new Tablero();
        
        // Crear jugadores con la estrategia MINIMAX sin poda ALFA-BETA
        Jugador jugador1 = new Jugador(1);
        Jugador jugador2 = new Jugador(2);
        
        // Establecer la estrategia MINIMAX sin poda ALFA-BETA con diferentes profundidades
        
        for (int i = 3; i <= 6; i++) {
            jugador1.establecerEstrategia(new EstrategiaMiniMax(new EvaluadorAleatorio(), i));
            jugador2.establecerEstrategia(new EstrategiaMiniMax(new EvaluadorAleatorio(), i));
            ejecutarPrueba(tablero, jugador1, jugador2, i);
        }
    }
    
    public static void ejecutarPrueba(Tablero tablero, Jugador jugador1, Jugador jugador2, int profundidad) {        
        // Ejecutar la partida
        tablero.jugarPartida(jugador1, jugador2);
        
        // Imprimir el resultado de la partida
        System.out.println("Profundidad: " + profundidad + ", Heurística: MINIMAX sin poda ALFA-BETA, Resultado: " + tablero.ganador());
    }
}
