import java.util.ArrayList;
import java.util.Vector;
import java.io.*;

public class Tablero {
    
    public static final int NFILAS = 6;
    public static final int NCOLUMNAS = 7;
    public static final int NOBJETIVO = 4;
    
    private static final String MARCA_J1 = "X";
    private static final String MARCA_J2 = "O";    
    private static final String MARCA_VACIO = " ";
    private static final String[] MARCAS = {MARCA_VACIO, MARCA_J1, MARCA_J2};
    
    private static final int VACIO = 0;
    private static final int JUGADOR1 = 1;    
    private static final int JUGADOR2 = 2;
    private static final int EMPATE = -1;
    
    private int[][] _casillas;
    private int[] _posicionLibre;    
    private int _ganador = EMPATE;
    
    
    /** Creates a new instance of Tablero */
    public Tablero() {
        this._casillas = new int[NCOLUMNAS][NFILAS];
        this._posicionLibre = new int[NCOLUMNAS];
        this.inicializar();
    }
    
    protected Object clone() {
        Tablero result = new Tablero();
        result.copiarCasillas(this._casillas);
        result.copiarPosicionLibre(this._posicionLibre);
        return(result);        
    }
    
    public boolean equals(Object obj) {
        int col, fila;
        
        Tablero tablero = (Tablero) obj;
        return(tablero.casillasIguales(_casillas));
    }
    
    public String toString() {
        String result = new String();
        int col, fila;
        
        for (fila=NFILAS-1; fila >= 0; fila--) {
            result += "|";
            for (col=0; col < NCOLUMNAS; col++) {
                result += (MARCAS[_casillas[col][fila]]+"|");
            }
            result += "\n";
        }
        return(result);
    }
    
    public boolean[] columnasLibres() {
        boolean [] result = new boolean[NCOLUMNAS];
        int col;
        
        for (col =0; col < NCOLUMNAS; col++){
           result[col] = (_posicionLibre[col]< NFILAS);
        }
        return(result);
    }
    
    boolean esFinal() {
        return(_ganador != 0);
    }
    
   /* Función que chequea el estado del tablero para comprobar
    * si es una posicion final y tomar nota de ello en el atributo
    * privado _ganador
    * 
    * Debe ser llamada una vez creado el tablero, porque las demas
    * funciones consultarán la variable _ganador (en lugar de reevaluar
    * el tablero de nuevo cada vez )
    */
    
    public void obtenerGanador() {
        int col, fila, jugador;
                
        _ganador = 0; // no hay ganador
        for (col=0; col < NCOLUMNAS; col++) {
            for (fila=0; fila < NFILAS; fila++) {
                jugador = _casillas[col][fila];
                if (jugador != VACIO) {
                    if (hayLineaVertical(col,fila,jugador) ||
                        hayLineaHorizontal(col,fila,jugador) ||
                        hayLineaDiagonal(col,fila,jugador)) {
                            _ganador = jugador;
                            return;
                        }
                }
            }
        }
        // no gana ninguno de los dos
        // comprobar si hay empate (columnas agotadas)
        boolean empate=true;
        for (col=0; col < NCOLUMNAS; col++) {
          empate = empate && (_posicionLibre[col] == NFILAS);  
        }
        if (empate) {
            _ganador = EMPATE;
        }
    }
    
    void inicializar() {
        int col, fila;
        
        for (col=0; col < NCOLUMNAS; col++) {
            for (fila=0; fila < NFILAS; fila++) {
                _casillas[col][fila] = VACIO;
            }
            _posicionLibre[col] = 0;
        }
        _ganador=0;
    }
    
    public void mostrar() {
        int col;
        
        System.out.println();
        System.out.print(this.toString());
        
        // Solo para ponerlo bonito
        System.out.print("_");
        for (col=0; col < NCOLUMNAS; col++) {
           System.out.print("__");   
        }
        System.out.println();
        System.out.print("|");
        for (col=0; col < NCOLUMNAS; col++) {
           System.out.print(col+"|");   
        }
        System.out.println();
    }
    
    public boolean ganaJ1() {
        return(_ganador == JUGADOR1);
    }
    
    public boolean ganaJ2() {
        return(_ganador == JUGADOR2);
    }
    
    public boolean hayEmpate() {
        return(_ganador == EMPATE);
    }
    
    public void anadirFicha(int columna, int jugador) {
        if (_posicionLibre[columna] < NCOLUMNAS-1) {
            _casillas[columna][_posicionLibre[columna]] = jugador;
            _posicionLibre[columna]++;
        }
    }
    
    private boolean hayLineaVertical(int col, int fila, int jugador) {
        int j;
        int numCasillas = 0;
        
        for (j = fila; j < NFILAS; j++) {
           if (_casillas[col][j] == jugador) {
               numCasillas++;
           }
           else {
               break;
           }
        }
        return (numCasillas >= NOBJETIVO);        
    }
    
    private boolean hayLineaHorizontal(int col, int fila, int jugador) {
       int i;
        int numCasillas = 0;
        
        for (i = col; i < NCOLUMNAS; i++) {
           if (_casillas[i][fila] == jugador) {
               numCasillas++;
           }
           else {
               break;
           }
        }
        return (numCasillas >= NOBJETIVO);             
    }
    
    private boolean hayLineaDiagonal(int col, int fila, int jugador) {
        int i,j,k;
        int numCasillas = 0;
        
        //diagonales "crecientes"
        for (k=0; k < NOBJETIVO; k++) {
           i = col+k;
           j = fila+k;
           if ((i < NCOLUMNAS) && (j < NFILAS)) {
               if (_casillas[i][j] == jugador) {
                   numCasillas++;
               }
               else {
                   break;
               }
           }
           else {
               break;
           }
        }
        if (numCasillas >= NOBJETIVO) {
            return(true);
        }
        
        //diagonales "decrecientes"
        numCasillas=0;
        for (k=0; k < NOBJETIVO; k++) {
           i = col+k;
           j = fila-k;
           if ((i < NCOLUMNAS) && (j >= 0)) {
               if (_casillas[i][j] == jugador) {
                   numCasillas++;
               }
               else {
                   break;
               }
           }
           else {
               break;
           }
        }
        return (numCasillas >= NOBJETIVO);                
    }    
    
    private void copiarCasillas(int[][] casillas) {
        int col, fila;
        
        for (col=0; col < NCOLUMNAS; col++) {
            for (fila=0; fila < NFILAS; fila++) {
                this._casillas[col][fila] = casillas[col][fila];
            }
        }       
    }    
    
    private void copiarPosicionLibre(int[] posicionLibre) {
        int col, fila;
        
        for (col=0; col < NCOLUMNAS; col++) {
            this._posicionLibre[col] = posicionLibre[col];
        }       
    }
    
    public boolean casillasIguales(int[][] casillas) {
       int col, fila;
       for (col=0; col < NCOLUMNAS; col++) {
          for (fila=0; fila < NFILAS; fila++) {
              if (this._casillas[col][fila] != casillas[col][fila]){
                  return(false);  // Son distintos -> salir devolviendo falso
              }
          }
       }               
       return (true); // Si llega todas las casillas son iguales
    }
    
    public boolean finalJuego() {
        return(_ganador != 0);
    }
    
    public int ganador() {
        return(_ganador);
    }
    
    public boolean esGanador(int jugador) {
        return(jugador == _ganador);
    }
    
    public static final int CASILLA_VACIA = 0;

    public int obtenerCasilla(int fila, int columna) {
        // Verificar si las coordenadas están dentro de los límites del tablero
        if (fila < 0 || fila >= NFILAS || columna < 0 || columna >= NCOLUMNAS) {
            // Devolver un valor que represente una casilla fuera del tablero
            return -1; // Por ejemplo, podríamos devolver -1 para indicar que la casilla está fuera del tablero
        }
        // Devolver el valor de la casilla en las coordenadas dadas
        return _casillas[columna][fila];
    }

    public boolean esCasillaValida(int fila, int columna) {
        // Verificar si las coordenadas están dentro de los límites del tablero
        return (fila >= 0 && fila < NFILAS && columna >= 0 && columna < NCOLUMNAS);
    }

    public int jugarPartida(Jugador jugador12, Jugador jugador22) {
        int turno = 0;
        Jugador jugadorActual;
        int movimiento;
        boolean posicionesPosibles[];

        // comprobar tablero: necesario para establecer si es o no un tablero final
        obtenerGanador();
        while (!esFinal()) {
            turno++;
            // establecer jugador del turno actual
            if ((turno % 2) == 1) { // turno impar -> jugador1
                jugadorActual = jugador12;
            } else {// turno par -> jugador2
                jugadorActual = jugador22;
            }
            // obtener movimiento: llama al jugador que tenga el turno,
            // que, a su vez, llamará a la estrategia que se le asigno al crearlo
            movimiento = jugadorActual.obtenerJugada(this);
            // comprobar si es correcto
            if ((movimiento >= 0) && (movimiento < NCOLUMNAS)) {
                posicionesPosibles = columnasLibres();
                if (posicionesPosibles[movimiento]) {
                    anadirFicha(movimiento, jugadorActual.getIdentificador());
                    // comprobar ganador
                    obtenerGanador();
                } else {
                    System.out.println("Columna completa. Juego Abortado.");
                }
            } else {
                System.out.println("Movimiento invalido. Juego Abortado.");
            }
        }
        return _ganador;
    }
}
