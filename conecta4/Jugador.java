/*
 * Jugador.java
 *
 * Created on 10 de enero de 2004, 17:19
 */

/**
 *
 * @author  ribadas
 */
public class Jugador {
    
    private Estrategia _estrategia;
    private int _identificador;
    private int victorias;
    private int empates;
    private int derrotas;
    
    /** Creates a new instance of Jugador */
    public Jugador() {
    }
    
    public Jugador(int identificador) {
        _identificador = identificador;
        victorias = 0;
        empates = 0;
        derrotas = 0;
    }
    
    public void establecerEstrategia(Estrategia estrategia) {
        _estrategia = estrategia;
    }

    public String getNombreEstrategia() {
        return _estrategia.getNombre();
    }
    
    public int obtenerJugada(Tablero tablero) {
        return(_estrategia.buscarMovimiento(tablero, _identificador));
    }
    
    public void setIdentificador(int identificador) {
        _identificador = identificador;
    }

    public int getIdentificador() {
        return(_identificador);
    }
    
    public static final int alternarJugador(int jugadorActual) {
        return(((jugadorActual%2)+1));  // Alterna entre jugador 1 y 2
    }

    public void incrementarVictorias() {
        victorias++;
    }

    public void incrementarEmpates() {
        empates++;
    }

    public void incrementarDerrotas() {
        derrotas++;
    }

    public void reiniciarEstadisticas() {
        victorias = 0;
        empates = 0;
        derrotas = 0;
    }

    public int getNumeroPartidasJugadas() {
        return (getNumeroVictorias() + getNumeroEmpates() + getNumeroDerrotas());
    }

    public int getNumeroVictorias() {
        return victorias;
    }

    public int getNumeroEmpates() {
        return empates;
    }

    public int getNumeroDerrotas() {
        return derrotas;
    }

    public int getTiempoMedioMovimiento() {
        return _estrategia.getTiempoMedioMovimiento();
    }

    public String getNumeroNodosEvaluados() {
        return _estrategia.getNumeroNodosEvaluados();
    }
}
