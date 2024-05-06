import java.util.Scanner;

public class Conecta4 {

    private static int numeroGanadasJ1;
    private static int numeroGanadasJ2;
    private static int numeroEmpates;

    /** Creates a new instance of Conecta4 */
    public Conecta4() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Seleccione el tipo de jugador 1:");
        System.out.println("1. Humano");
        System.out.println("2. Máquina (minimax con evaluador aleatorio y prof. busqueda 4)");
        System.out.println("3. Máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)");
        int tipoJugador1 = sc.nextInt();
    
        Jugador jugador1 = new Jugador(1);
    
        System.out.println("Seleccione el tipo de jugador 2:");
        System.out.println("1. Humano");
        System.out.println("2. Máquina (minimax con evaluador aleatorio y prof. busqueda 4)");
        System.out.println("3. Máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)");
        int tipoJugador2 = sc.nextInt();
    
        Jugador jugador2 = new Jugador(2);

        int numeroIteraciones = 0;
        int numeroPartidasPorIteracion = 0;
    
        switch (tipoJugador1) {
            case 1:
                jugador1.establecerEstrategia(new EstrategiaHumano());
                DEBUG("Jugador 1: humano\n");
                break;
            case 2:
                jugador1.establecerEstrategia(new EstrategiaMiniMax(new EvaluadorAleatorio(), 4));
                DEBUG("Jugador 1: máquina (minimax con evaluador aleatorio y prof. busqueda 4)\n");
                break;
            case 3:
                EvaluadorPonderado evaluadorPonderadoJ1 = new EvaluadorPonderado();
                System.out.println("Introduzca el número de iteraciones con el que ajustar los pesos:");
                numeroIteraciones = sc.nextInt();
                System.out.println("Introduzca el número de partidas por iteración:");
                numeroPartidasPorIteracion = sc.nextInt();
                evaluadorPonderadoJ1.buscarPesosOptimos(numeroIteraciones, numeroPartidasPorIteracion);
                jugador1.establecerEstrategia(new EstrategiaAlfaBeta(evaluadorPonderadoJ1, 4));
                DEBUG("Jugador 1: máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)\n");
                break;
        }
        
        switch (tipoJugador2) {
            case 1:
                jugador2.establecerEstrategia(new EstrategiaHumano());
                DEBUG("Jugador 2: humano\n");
                break;
            case 2:
                jugador2.establecerEstrategia(new EstrategiaMiniMax(new EvaluadorAleatorio(), 4));
                DEBUG("Jugador 2: máquina (minimax con evaluador aleatorio y prof. busqueda 4)\n");
                break;
            case 3:
                EvaluadorPonderado evaluadorPonderadoJ2 = new EvaluadorPonderado();
                System.out.println("Introduzca el número de iteraciones con el que ajustar los pesos:");
                numeroIteraciones = sc.nextInt();
                System.out.println("Introduzca el número de partidas por iteración:");
                numeroPartidasPorIteracion = sc.nextInt();
                evaluadorPonderadoJ2.buscarPesosOptimos(numeroIteraciones, numeroPartidasPorIteracion);
                jugador2.establecerEstrategia(new EstrategiaAlfaBeta(evaluadorPonderadoJ2, 4));
                DEBUG("Jugador 2: máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)\n");
                break;
        }

        Tablero tablero = new Tablero();
        System.out.println("Introduzca el número de partidas a disputar:");
        int partidasADisputar = sc.nextInt();
        jugar(jugador1, jugador2, tablero, partidasADisputar);
    }

    static String procesarResultado(Tablero tablero) {
        if (tablero.hayEmpate()) {
            return "EMPATE";
        } else if (tablero.ganaJ1()) {
            return "GANA J1";
        } else if (tablero.ganaJ2()) {
            return "GANA J2";
        }
        return "";
    }
    
    

    static void jugar(Jugador jugador1, Jugador jugador2, Tablero tablero, int numPartidas) {
        Scanner sc = new Scanner(System.in);
        boolean jugarOtraVez = true;
    
        while (jugarOtraVez) {
            tablero.inicializar(); // Inicializar el tablero para una nueva partida

            while (!tablero.esFinal()) {
                // Turno del jugador 1
                int resultado = tablero.jugarPartida(jugador1, jugador2);
                if (resultado != 0) {
                    break; // Si hay un ganador o un empate, terminar el juego
                }
                
                // Turno del jugador 2
                resultado = tablero.jugarPartida(jugador2, jugador1);
                
                if (resultado != 0) {
                    break; // Si hay un ganador o un empate, terminar el juego
                }
            }
    
            // Mostrar el resultado de la partida
            String resultadoPartida = procesarResultado(tablero);
            if (jugador1.getNombreEstrategia().equals("Humano") || jugador2.getNombreEstrategia().equals("Humano")){
                tablero.mostrar();
                System.out.println("Resultado de la partida: " + resultadoPartida);
            }
    
            // Incrementar el contador correspondiente al resultado
            if (resultadoPartida.equals("GANA J1")) {
                numeroGanadasJ1++;
            } else if (resultadoPartida.equals("GANA J2")) {
                numeroGanadasJ2++;
            } else if (resultadoPartida.equals("EMPATE")) {
                numeroEmpates++;
            }
            
            if ((!jugador1.getNombreEstrategia().equals("Humano") || !jugador2.getNombreEstrategia().equals("Humano")) && numPartidas > 1) {
                jugarOtraVez = true;
                numPartidas--;
            } else {
                System.out.println("¿Desea jugar otra vez? (s/n)");
                String respuesta = sc.next();
                if (respuesta.equals("s")) {
                    jugarOtraVez = true;
                } else {
                    jugarOtraVez = false;
                }
            }

        }
        // Mostrar el número de partidas ganadas por cada jugador y el número de empates
        System.out.println("Número de partidas ganadas por el jugador 1: " + numeroGanadasJ1);
        System.out.println("Número de partidas ganadas por el jugador 2: " + numeroGanadasJ2);
        System.out.println("Número de empates: " + numeroEmpates);
    }
    
      
    public static final void ERROR_FATAL(java.lang.String mensaje) {
        System.out.println("ERROR FATAL\n\t"+mensaje);
        System.exit(0); // Finalizar aplicacion
    }
    
    public static final void DEBUG(String str) {
        System.out.print("DBG:"+str);
    }
    
    public static final void ERROR(java.lang.String mensaje) {
        System.out.println("ERROR\n\t"+mensaje);
    }
}
