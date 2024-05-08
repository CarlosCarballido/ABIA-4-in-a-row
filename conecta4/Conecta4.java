import java.util.Scanner;
public class Conecta4 {

    private static boolean evaluarOtraVez;
    private static boolean jugarOtraVez;

    /** Creates a new instance of Conecta4 */
    public Conecta4() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("\nSeleccione el tipo de jugador 1:");
        System.out.println("\n1. Humano");
        System.out.println("\n2. Máquina (minimax con evaluador aleatorio y prof. busqueda 4)");
        System.out.println("\n3. Máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)");
        int tipoJugador1 = sc.nextInt();
    
        Jugador jugador1 = new Jugador(1);
    
        System.out.println("\nSeleccione el tipo de jugador 2:");
        System.out.println("\n1. Humano");
        System.out.println("\n2. Máquina (minimax con evaluador aleatorio y prof. busqueda 4)");
        System.out.println("\n3. Máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)");
        int tipoJugador2 = sc.nextInt();
    
        Jugador jugador2 = new Jugador(2);

        int numeroIteraciones = 0;

        switch (tipoJugador1) {
            case 1:
                jugador1.establecerEstrategia(new EstrategiaHumano());
                DEBUG("Jugador 1: humano");
                break;
            case 2:
                jugador1.establecerEstrategia(new EstrategiaMiniMax(new EvaluadorAleatorio(), 4));
                DEBUG("Jugador 1: máquina (minimax con evaluador aleatorio y prof. busqueda 4)");
                break;
            case 3:
                EvaluadorPonderado evaluadorPonderadoJ1 = new EvaluadorPonderado();
                System.out.println("\nIntroduzca el número de iteraciones con el que ajustar los pesos:");
                numeroIteraciones = sc.nextInt();
                long tiempoBuscarPesosOptimosJ1 = System.currentTimeMillis();
                evaluadorPonderadoJ1.buscarPesosOptimos(numeroIteraciones);
                tiempoBuscarPesosOptimosJ1 = System.currentTimeMillis() - tiempoBuscarPesosOptimosJ1;
                System.out.println("\nTiempo empleado en buscar los pesos óptimos: " + tiempoBuscarPesosOptimosJ1 + " ms");
                jugador1.establecerEstrategia(new EstrategiaAlfaBeta(evaluadorPonderadoJ1, 4));
                DEBUG("Jugador 1: máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)");
                break;
        }
        
        switch (tipoJugador2) {
            case 1:
                jugador2.establecerEstrategia(new EstrategiaHumano());
                DEBUG("Jugador 2: humano");
                break;
            case 2:
                jugador2.establecerEstrategia(new EstrategiaMiniMax(new EvaluadorAleatorio(), 4));
                DEBUG("Jugador 2: máquina (minimax con evaluador aleatorio y prof. busqueda 4)");
                break;
            case 3:
                EvaluadorPonderado evaluadorPonderadoJ2 = new EvaluadorPonderado();
                System.out.println("\nIntroduzca el número de iteraciones con el que ajustar los pesos:");
                numeroIteraciones = sc.nextInt();
                long tiempoBuscarPesosOptimosJ2 = System.currentTimeMillis();
                evaluadorPonderadoJ2.buscarPesosOptimos(numeroIteraciones);
                tiempoBuscarPesosOptimosJ2 = System.currentTimeMillis() - tiempoBuscarPesosOptimosJ2;
                System.out.println("Tiempo empleado en buscar los pesos óptimos: " + tiempoBuscarPesosOptimosJ2 + " ms");
                jugador2.establecerEstrategia(new EstrategiaAlfaBeta(evaluadorPonderadoJ2, 4));
                DEBUG("Jugador 2: máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)");
                break;
        }

        Tablero tablero = new Tablero();
        System.out.println("\nIntroduzca el número de partidas a disputar:");
        int partidasADisputar = sc.nextInt();
        
        do{
            jugar(jugador1, jugador2, tablero, partidasADisputar);
            System.out.println("\n¿Desea jugar otra vez? (s/n)");
            String respuesta = sc.next();
            if (respuesta.equals("s")) {
                jugador1.reiniciarEstadisticas();
                jugador2.reiniciarEstadisticas();
                System.out.println("\nIntroduzca el número de partidas a disputar:");
                partidasADisputar = sc.nextInt();
                jugarOtraVez = true;
            } else {
                jugarOtraVez = false;
            }
         } while (jugarOtraVez);

        jugador1.reiniciarEstadisticas();
        jugador2.reiniciarEstadisticas();

        if (jugador1.getNombreEstrategia().equals("AlfaBeta") || jugador2.getNombreEstrategia().equals("AlfaBeta")) {
            System.out.println("\n¿Desea evaluar los pesos optimos contra los iniciales? (s/n)");
            String respuesta = sc.next();
            if (respuesta.equals("s")) {
                do {
                    compararEstrategias(jugador1, jugador2, tablero);
                    System.out.println("\n¿Desea evaluar otra vez? (s/n)");
                    respuesta = sc.next();
                    if (respuesta.equals("s")) {
                        evaluarOtraVez = true;
                        jugador1.reiniciarEstadisticas();
                        jugador2.reiniciarEstadisticas();
                    } else {
                        evaluarOtraVez = false;
        
                    }
                } while (evaluarOtraVez);
            }
        }
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

    private static void incrementarResultados(String resultadoPartida, Jugador jugador1, Jugador jugador2) {
        if (resultadoPartida.equals("GANA J1")) {
            jugador1.incrementarVictorias();
            jugador2.incrementarDerrotas();
        } else if (resultadoPartida.equals("GANA J2")) {
            jugador1.incrementarDerrotas();
            jugador2.incrementarVictorias();
        } else if (resultadoPartida.equals("EMPATE")) {
            jugador1.incrementarEmpates();
            jugador2.incrementarEmpates();
        }
    }
    

    static void jugar(Jugador jugador1, Jugador jugador2, Tablero tablero, int numPartidas) {
        Scanner sc = new Scanner(System.in);
        jugarOtraVez = true;
    
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
                System.out.println("\nResultado de la partida: " + resultadoPartida);
                incrementarResultados(resultadoPartida, jugador1, jugador2);
                numPartidas--;
            }
    
            
    
            if ((!jugador1.getNombreEstrategia().equals("Humano") || !jugador2.getNombreEstrategia().equals("Humano")) && numPartidas >= 1) {
                incrementarResultados(resultadoPartida, jugador1, jugador2);
                jugarOtraVez = true;
                numPartidas--;
            } else {
                jugarOtraVez = false;
                // Mostrar el número de partidas ganadas por cada jugador y el número de empates
                System.out.println("\nNúmero de partidas ganadas por el jugador 1: " + jugador1.getNumeroVictorias());
                System.out.println("Número de partidas ganadas por el jugador 2: " + jugador2.getNumeroVictorias());
                System.out.println("Número de empates: " + jugador1.getNumeroEmpates());
                System.out.println("Porcentajes de victorias:" + "Jugador 1: " + ((jugador1.getNumeroVictorias() * 100) / (jugador1.getNumeroVictorias()+jugador1.getNumeroEmpates()+jugador2.getNumeroVictorias())) + "%");
                System.out.println("Jugador 2: " + ((jugador2.getNumeroVictorias() * 100) / (jugador1.getNumeroVictorias()+jugador1.getNumeroEmpates()+jugador2.getNumeroVictorias())) + "%");
                System.out.println("Porcentaje de empates: " + ((jugador1.getNumeroEmpates() * 100) / (jugador1.getNumeroVictorias()+jugador1.getNumeroEmpates()+jugador2.getNumeroVictorias())) + "%");
                System.out.println("Tiempo medio por movimiento del jugador 1: " + jugador1.getTiempoMedioMovimiento() + " ns");
                System.out.println("Tiempo medio por movimiento del jugador 2: " + jugador2.getTiempoMedioMovimiento() + " ns");
                System.out.println("Número de nodos evaluados por busqueda promedio del jugador 1: " + jugador1.getNumeroNodosEvaluados());
                System.out.println("Número de nodos evaluados por busqueda promedio del jugador 2: " + jugador2.getNumeroNodosEvaluados());
                System.out.println();
            }
        }
    }
    

    private static void compararEstrategias(Jugador jugador1, Jugador jugador2, Tablero tablero) {    
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca el número de partidas a disputar en la comparacion:");
        int numPartidasComparacion = sc.nextInt();
        if (jugador1.getNombreEstrategia().equals("AlfaBeta") && jugador2.getNombreEstrategia().equals("AlfaBeta")) {
            System.out.println("¿Qué jugador desea que juegue con los pesos óptimos? (1/2)");
            int jugadorOptimo = sc.nextInt();
            System.out.println("Estadísticas después de la comparación:");
            switch (jugadorOptimo) {
                case 1:
                    EvaluadorPonderado evaluadorPonderadoJ2 = new EvaluadorPonderado();
                    evaluadorPonderadoJ2.configurarPesosManualmente(0.1, 0.1, 0.1, 0.1);
                    break;
                case 2:
                    EvaluadorPonderado evaluadorPonderadoJ1 = new EvaluadorPonderado();
                    evaluadorPonderadoJ1.configurarPesosManualmente(0.1, 0.1, 0.1, 0.1);
                    break;
            } 
        } else {
            if (jugador1.getNombreEstrategia().equals("AlfaBeta")) {
                System.out.println("El jugador 1 tiene los pesos optimos y el jugador 2 tiene los pesos iniciales.");
                EvaluadorPonderado ep = new EvaluadorPonderado();
                ep.configurarPesosManualmente(0.1, 0.1, 0.1, 0.1);
                jugador2.establecerEstrategia(new EstrategiaAlfaBeta(ep, 4));
            } else {
                System.out.println("El jugador 2 tiene los pesos optimos y el jugador 1 tiene los pesos iniciales.");
                EvaluadorPonderado ep = new EvaluadorPonderado();
                ep.configurarPesosManualmente(0.1, 0.1, 0.1, 0.1);
                jugador1.establecerEstrategia(new EstrategiaAlfaBeta(ep, 4));
            }
            System.out.println("Estadísticas después de la comparación:");
        }
        jugar(jugador1, jugador2, tablero, numPartidasComparacion);
    }
    public static final void ERROR_FATAL(java.lang.String mensaje) {
        System.out.println("ERROR FATAL\n\t"+mensaje);
        System.exit(0); // Finalizar aplicacion
    }
    
    public static final void DEBUG(String str) {
        System.out.println("DBG:"+str);
    }
    
    public static final void ERROR(java.lang.String mensaje) {
        System.out.println("ERROR\n\t"+mensaje);
    }
}
