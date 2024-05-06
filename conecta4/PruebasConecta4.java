public class PruebasConecta4 {

    public static void main(String[] args) {
        // Profundidades máximas de búsqueda
        int[] profundidades = {3, 4, 5, 6};

        // Heurísticas utilizadas
        Evaluador[] evaluadores = {
            new EvaluadorAleatorio(),
            new EvaluadorInicial(),
            new EvaluadorPonderado()
        };

        // Ejecutar las pruebas para cada combinación de profundidad máxima y heurística
        for (int profundidad : profundidades) {
            for (Evaluador evaluador : evaluadores) {
                ejecutarPrueba(profundidad, evaluador);
            }
        }
    }

    public static void ejecutarPrueba(int profundidad, Evaluador evaluador) {
        Tablero tablero = new Tablero();
        Jugador jugador1 = new Jugador(1);
        Jugador jugador2 = new Jugador(2);
        
        // Configurar estrategias de juego para los jugadores
        jugador1.establecerEstrategia(new EstrategiaMiniMax(evaluador, profundidad));
        jugador2.establecerEstrategia(new EstrategiaMiniMax(new EvaluadorAleatorio(), profundidad));

        // Ejecutar la partida
        int resultado = tablero.jugarPartida(jugador1, jugador2);

        // Imprimir resultados
        System.out.println("Profundidad: " + profundidad + ", Heurística: " + evaluador.getClass().getSimpleName() + ", Resultado: " + resultado);
    }
}

class EvaluadorInicial extends EvaluadorPonderado{

    EvaluadorInicial() {
        EvaluadorPonderado evaluadorPonderadoJ2 = new EvaluadorPonderado();
        evaluadorPonderadoJ2.configurarPesosManualmente(0.1, 0.1, 0.1, 0.1);
    }
}