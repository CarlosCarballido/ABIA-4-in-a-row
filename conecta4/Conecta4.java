import java.util.Scanner;

public class Conecta4 {

    /** Creates a new instance of Conecta4 */
    public Conecta4() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        cargarArgumentos(args);
        
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
                EvaluadorPonderado EvaluadorPonderadoJ1 = new EvaluadorPonderado();
                jugador1.establecerEstrategia(new EstrategiaAlfaBeta(EvaluadorPonderadoJ1, 4));
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
                EvaluadorPonderado EvaluadorPonderadoJ2 = new EvaluadorPonderado();
                jugador2.establecerEstrategia(new EstrategiaAlfaBeta(EvaluadorPonderadoJ2, 4));
                DEBUG("Jugador 2: máquina (AlfaBeta con evaluador Ponderado y prof. busqueda 4)\n");
                break;
        }
            
        boolean jugarOtraPartida = true;
        int numeroPartidas = 1;
        if (tipoJugador1 != 1 && tipoJugador2 != 1) {
            System.out.println("Introduce el número de partidas a jugar:");
            numeroPartidas = sc.nextInt();
        }
        int numeroEmpates = 0;
        int numeroGanadasJ1 = 0;
        int numeroGanadasJ2 = 0;
        do {            
            // Jugar
            Tablero tablero = new Tablero();
            tablero.inicializar();
            jugar(jugador1, jugador2, tablero);
            
            // Mostrar resultados
            if (tipoJugador1 == 1 || tipoJugador2 == 1) {
                tablero.mostrar();
            }
    
            // Procesar resultado
            String resultado = procesarResultado(tablero);

            int ultimaJ1 = 0;
            int ultimaJ2 = 0;

            if (tipoJugador1 == 1 || tipoJugador2 == 1) {
                System.out.println("Resultado: " + resultado);
            }
            if (resultado.equals("EMPATE")) {
                ultimaJ1 = 1;
                ultimaJ2 = 1;
                numeroEmpates++;
            } else if (resultado.equals("GANA J1")) {
                ultimaJ1 = 3;
                ultimaJ2 = 0;
                numeroGanadasJ1++;
            } else if (resultado.equals("GANA J2")) {
                ultimaJ1 = 0;
                ultimaJ2 = 3;
                numeroGanadasJ2++;
            }
    
            // Ajustar pesos del evaluador ponderado
            if (tipoJugador1 == 3) {
                EvaluadorPonderado EvaluadorPonderadoJ1 = new EvaluadorPonderado(); // Initialize EvaluadorPonderadoJ1
                EvaluadorPonderadoJ1.ajustarPesosAutomaticamente(1.0, 5.0, ultimaJ1, (numeroEmpates + numeroGanadasJ1 + numeroGanadasJ2));
                jugador1.establecerEstrategia(new EstrategiaAlfaBeta(EvaluadorPonderadoJ1, 4));
            }
            if (tipoJugador2 == 3) {
                EvaluadorPonderado EvaluadorPonderadoJ2 = new EvaluadorPonderado(); // Initialize EvaluadorPonderadoJ2
                EvaluadorPonderadoJ2.ajustarPesosAutomaticamente(1.0, 5.0, ultimaJ2, (numeroEmpates + numeroGanadasJ1 + numeroGanadasJ2));
                jugador2.establecerEstrategia(new EstrategiaAlfaBeta(EvaluadorPonderadoJ2, 4));
            }

            
    
            // Preguntar si desea volver a jugar
            if (tipoJugador1 == 1 || tipoJugador2 == 1) {
                System.out.println("¿Desea jugar otra partida? (s/n)");
                char respuesta = sc.next().charAt(0);
                jugarOtraPartida = (respuesta == 's' || respuesta == 'S');
            } else {
                numeroPartidas--;
                jugarOtraPartida = numeroPartidas > 0;
            }
        } while (jugarOtraPartida);
        System.out.println("Partidas Ganadas por el jugador 1: " + numeroGanadasJ1);
        System.out.println("Partidas Ganadas por el jugador 2: " + numeroGanadasJ2);
        System.out.println("Número de Empates: " + numeroEmpates);
    }
    
    private static void cargarArgumentos(String[] args) {
        // TODO Auto-generated method stub
    }

    private static String procesarResultado(Tablero tablero) {
        if (tablero.hayEmpate()) {
            return "EMPATE";
        } else if (tablero.ganaJ1()) {
            return "GANA J1";
        } else if (tablero.ganaJ2()) {
            return "GANA J2";
        }
        return "";
    }
    
    

    private static void jugar(Jugador jugador1, Jugador jugador2, Tablero tablero) {
        int turno=0;
        Jugador jugadorActual;
        int movimiento;
        boolean posicionesPosibles[];
        
       // comprobar tablero: necesario para establecer si es o no un tablero final
        tablero.obtenerGanador();
        while(!tablero.esFinal()){
            turno++;
            // establecer jugador del turno actual
            if ((turno%2) == 1) { // turno impar -> jugador1
                jugadorActual = jugador1;
            }
            else {// turno par -> jugador2
                jugadorActual = jugador2;
            }
            // obtener movimiento: llama al jugador que tenga el turno,
        // que, a su vez, llamará a la estrategia que se le asigno al crearlo
            movimiento = jugadorActual.obtenerJugada(tablero);
            // comprobar si es correcto
            if ((movimiento>=0) && (movimiento<Tablero.NCOLUMNAS)) {
                posicionesPosibles = tablero.columnasLibres();
                if (posicionesPosibles[movimiento]) {
                    tablero.anadirFicha(movimiento, jugadorActual.getIdentificador());
                    // comprobar ganador
                    tablero.obtenerGanador();
                }
                else {
                    ERROR_FATAL("Columna completa. Juego Abortado.");
                }
            }
            else {
              ERROR_FATAL("Movimiento invalido. Juego Abortado.");
            }            
        }        
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
