package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.PoligonoIncompletoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PoligonoTest {

    @Test
    void crearPoligono_conMenosDeCuatroVertices_lanzaExcepcion() {
        List<Coordenada> listaPequeña = List.of(
                new Coordenada(0.0, 0.0),
                new Coordenada(1.0, 1.0),
                new Coordenada(0.0, 1.0)
        );

        assertThrows(PoligonoIncompletoException.class,
                () -> new Poligono(listaPequeña));
    }

    @Test
    void crearPoligono_primerUltimoDistintos_lanzaExcepcion() {
        List<Coordenada> listaCierraMal = Arrays.asList(
                new Coordenada(0.0, 0.0),
                new Coordenada(1.0, 0.0),
                new Coordenada(1.0, 1.0),
                new Coordenada(0.0, 1.0)
        );

        assertThrows(PoligonoIncompletoException.class,
                () -> new Poligono(listaCierraMal));
    }

    @Test
    void crearPoligono_verticesConsecutivosIdenticos_lanzaExcepcion() {
        List<Coordenada> listaRepetida = Arrays.asList(
                new Coordenada(0.0, 0.0),
                new Coordenada(0.0, 0.0),
                new Coordenada(1.0, 0.0),
                new Coordenada(0.0, 0.0)
        );

        assertThrows(PoligonoIncompletoException.class,
                () -> new Poligono(listaRepetida));
    }

    @Test
    void crearPoligono_valido_noLanzaExcepcion() {
        List<Coordenada> listaValida = Arrays.asList(
                new Coordenada(0.0, 0.0),
                new Coordenada(0.0, 1.0),
                new Coordenada(1.0, 1.0),
                new Coordenada(1.0, 0.0),
                new Coordenada(0.0, 0.0)
        );

        assertDoesNotThrow(() -> new Poligono(listaValida));
    }
}
