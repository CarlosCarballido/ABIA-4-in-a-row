public class EstrategiaMiniMax extends Estrategia {
    /* Estrategia que implementa una busqueda MINIMAX
     * 
     * Los parametros de la busqueda (funcion de evaluacion + cota máxima)
     * se establecen al crear el objeto o con las funciones
     * "establecerEvaluador()" y "establecerCapaMaxima()"
     */
     
    private long tiempoTotalMovimientos = 0;
    private int movimientosRealizados = 0;
    private int nodosEvaluados = 0;
    private int busquedasRealizadas = 0;

    protected Evaluador _evaluador;
    protected int _capaMaxima;

    protected int _jugadorMAX; // - guarda el identificador del jugador
                             //   que hace el papel de MAX
			     // - necesario al hacer las evaluaciones
			     //   de posiciones finales (ganador, perdedor, empate)
			     //   en el caso base de la recursividad del MINIMAX 
   
    /** Creates a new instance of EstrategiaMiniMax */
    public EstrategiaMiniMax() {
    }
    
    public EstrategiaMiniMax(Evaluador evaluador, int capaMaxima) {
       this.establecerEvaluador(evaluador);  
       this.establecerCapaMaxima(capaMaxima);
    }

    @Override
    public String getNombre() {
        return "MINIMAX";
    }

    @Override
    protected int getTiempoMedioMovimiento() {
        if (movimientosRealizados == 0) {
            return 0; // No se ha realizado ningún movimiento, tiempo medio es 0
        } else {
            return (int) (tiempoTotalMovimientos / movimientosRealizados); // Tiempo medio en milisegundos
        }
    }

    @Override
    protected String getNumeroNodosEvaluados() {
        return String.valueOf(nodosEvaluados/busquedasRealizadas); // Return the number of nodes evaluated
    }
    
    public int buscarMovimiento(Tablero tablero, int jugador) {
        busquedasRealizadas++;
        long tiempoInicio = System.nanoTime();
        // Implementa primera capa del MINIMAX + seleccion jugada mas prometedora
	// 
	// 
        //    capa O -> capa MAX -> maximiza
        //    devuelve la columna con mayor evaluacion
	
        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col,valorSucesor;
        int mejorPosicion=-1;  // Movimiento nulo
        int mejorValor=_evaluador.MINIMO; // Minimo  valor posible 

        _jugadorMAX = jugador; // - anota el identificador del jugador que
                               //   tiene el papel de MAX
                               // - necesario para evaluar posiciones finales
        for (col=0; col<Tablero.NCOLUMNAS; col++) {
            if (movimientosPosibles[col]) { //se puede añadir ficha en columna
                // crear nuevo tablero y comprobar ganador
                nuevoTablero = (Tablero) tablero.clone();
                nuevoTablero.anadirFicha(col,jugador);
                nuevoTablero.obtenerGanador();

                // evaluarlo (OJO: cambiar jugador, establecer capa a 1)
                valorSucesor = MINIMAX(nuevoTablero,Jugador.alternarJugador(jugador),1);     
                nodosEvaluados++; // Increment the counter of nodes evaluated           
                nuevoTablero = null; // Ya no se necesita 
                
                // tomar mejor valor            
                if (valorSucesor >= mejorValor) {
                    mejorValor = valorSucesor;
                    mejorPosicion = col;
                }
            }
        }
        long tiempoFin = System.nanoTime();
        tiempoTotalMovimientos += tiempoFin - tiempoInicio;
        movimientosRealizados++;
        return(mejorPosicion);        
    }
    
    
    public int MINIMAX(Tablero tablero, int jugador, int capa) {
        // Implementa la propagación de valores MINIMAX propiamente dicha
	// a partir del segundo nivel (capa 1)
       
        // Casos base
        if (tablero.hayEmpate()) {
            return(0);
        }
	// la evaluacion de posiciones finales (caso base recursididad)
	// se hace SIEMPRE desde la prespectiva de MAX
	// -> se usa el identificador del jugador MAX (1 o 2) guardado
	//    en la llamada a buscarMovimiento()
        if (tablero.esGanador(_jugadorMAX)){ // gana MAX
            return(_evaluador.MAXIMO);
        }
        if (tablero.esGanador(Jugador.alternarJugador(_jugadorMAX))){ // gana el otro
            return(_evaluador.MINIMO);
        } 
        if (capa == (_capaMaxima)) { // alcanza nivel maximo
            return(_evaluador.valoracion(tablero, _jugadorMAX));
        }

       // Recursividad sobre los sucesores
        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col,valor,valorSucesor;
        
        if (esCapaMIN(capa)) {
           valor = _evaluador.MAXIMO; // valor máximo 
        }
        else {
           valor = _evaluador.MINIMO; // valor mínimo
        }
        for (col=0; col<Tablero.NCOLUMNAS; col++) {
            if (movimientosPosibles[col]) { //se puede añadir ficha en columna
                // crear nuevo tablero y comprobar ganador
                nuevoTablero = (Tablero) tablero.clone();
                nuevoTablero.anadirFicha(col,jugador);
                nuevoTablero.obtenerGanador();
                
                // evaluarlo (OJO: cambiar jugador e incrementar capa)
                valorSucesor = MINIMAX(nuevoTablero,Jugador.alternarJugador(jugador),(capa+1));
                nodosEvaluados++; // Increment the counter of nodes evaluated
                nuevoTablero = null; // Ya no se necesita 
                // tomar minimo o maximo             
                if (esCapaMIN(capa)) {
                    valor = minimo2(valor, valorSucesor);
                }
                else {
                    valor = maximo2(valor, valorSucesor);
                }
            }
        }
        return(valor);
    }
    
   public void establecerCapaMaxima(int capaMaxima) {
      _capaMaxima = capaMaxima;
   }
   
   public void establecerEvaluador(Evaluador evaluador) {
      _evaluador = evaluador;
   }
    protected static final boolean esCapaMIN(int capa) {
        return((capa % 2)==1); // es impar
    }
    
    private static final boolean esCapaMAX(int capa) {
        return((capa % 2)==0); // es par
    }
    
    protected static final int maximo2(int v1, int v2) {
        if (v1 > v2)
            return(v1);
        else
            return(v2);
    }
    
    protected static final int minimo2(int v1, int v2) {
        if (v1 < v2)
            return(v1);
        else
            return(v2);    
    }
    
}  // Fin clase EstartegiaMINIMAX
