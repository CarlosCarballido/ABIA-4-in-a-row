/*
 * Conecta4.java
 *
 */

 import java.util.Scanner;

 /**
  *
  * @author  ribadas
  */
 public class Conecta4 {
     
     private Jugador _jugador1;    
     private Jugador _jugador2;
     private Tablero _tablero;
     
     /** Creates a new instance of Conecta4 */
     public Conecta4() {
     }
     
     /**
      * @param args the command line arguments
      */
     public static void main(String[] args) {
         cargarArgumentos(args);
 
         // Crear jugadores y establecer estrategias
         // Jugador 1: jugador humano
         Jugador jugador1 = new Jugador(1);
         jugador1.establecerEstrategia(new EstrategiaHumano());
         DEBUG("Jugador 1: humano\n");
        
         // Jugador 2: jugador minimax con evaluador aleatorio y prof. busqueda 4
         Jugador jugador2 = new Jugador(2);
 
         Scanner sc = new Scanner(System.in);
         System.out.println("Seleccione el tipo de jugador 2:");
         System.out.println("1. Humano");
         System.out.println("2. Máquina (minimax con evaluador aleatorio y prof. busqueda 4)");
         System.out.println("3. Máquina (minimax con evaluador Conecta4 y prof. busqueda 4)");
         int tipoJugador2 = sc.nextInt();
         switch (tipoJugador2) {
             case 1:
                 jugador2.establecerEstrategia(new EstrategiaHumano());
                 DEBUG("Jugador 2: humano\n");
                 break;
             case 2:
                 jugador2.establecerEstrategia(new EstrategiaMiniMax_AlfaBeta(new EvaluadorAleatorio(), 4));
                 DEBUG("Jugador 2: máquina (minimax con evaluador aleatorio y prof. busqueda 4)\n");
                 break;
             case 3:
                 jugador2.establecerEstrategia(new EstrategiaMiniMax_AlfaBeta(new EvaluadorConecta4(), 4));
                 DEBUG("Jugador 2: máquina (minimax con evaluador Conecta4 y prof. busqueda 4)\n");
                 break;
         }
        
         // Jugar
         Tablero tablero = new Tablero();
         tablero.inicializar();
         jugar(jugador1, jugador2, tablero);
                 
         // Mostrar resultados
         tablero.mostrar();
         if (tablero.hayEmpate()) {
             System.out.println("RESULTADO: Empate");
         }
         if (tablero.ganaJ1()){
             System.out.println("RESULTADO: Gana jugador 1");
         }
         if (tablero.ganaJ2()){
             System.out.println("RESULTADO: Gana jugador 2");
         }
         System.exit(1);
 
     }
 
     private static void cargarArgumentos(String[] args) {
         // procesar parametros de linea de comandos
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
 