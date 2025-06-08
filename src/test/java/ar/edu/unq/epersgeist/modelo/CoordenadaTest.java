package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.CoordenadaFueraDeRangoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordenadaTest {

    @Test
    void crearCoordenada_RangoValido_NoLanzaExcepcion() {
        assertDoesNotThrow(() -> new Coordenada(  0.0,    0.0));
        assertDoesNotThrow(() -> new Coordenada( 90.0,  180.0));
        assertDoesNotThrow(() -> new Coordenada(-90.0, -180.0));
        assertDoesNotThrow(() -> new Coordenada( 45.5,   73.2));
        assertDoesNotThrow(() -> new Coordenada(-23.7,  12.3));
    }

    @Test
    void crearCoordenada_LatitudMayor90_LanzaExcepcion() {
        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> new Coordenada(90.1, 0.0));
    }

    @Test
    void crearCoordenada_LatitudMenorMinus90_LanzaExcepcion() {
        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> new Coordenada(-90.1, 0.0));
    }

    @Test
    void crearCoordenada_LongitudMayor180_LanzaExcepcion() {
        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> new Coordenada(0.0, 180.1));
    }

    @Test
    void crearCoordenada_LongitudMenorMinus180_LanzaExcepcion() {
        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> new Coordenada(0.0, -180.1));
    }

    @Test
    void crearCoordenada_LatitudNull_LanzaExcepcion() {
        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> new Coordenada(null, 0.0));
    }

    @Test
    void crearCoordenada_LongitudNull_LanzaExcepcion() {
        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> new Coordenada(0.0, null));
    }
}

