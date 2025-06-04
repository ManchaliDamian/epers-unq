package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.servicios.interfaces.PoligonoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class PoligonoServiceTest {

    @Autowired
    private PoligonoService poligonoService;

    private Poligono poligonoCuadrado;
    private Poligono poligonoTriangular;
    private Long ubicacionId1;
    private Long ubicacionId2;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        poligonoService.eliminarTodos();

        ubicacionId1 = 1L;
        ubicacionId2 = 2L;

        // Crear un polígono cuadrado válido
        List<Coordenada> coordenadasCuadrado = Arrays.asList(
            new Coordenada(0.0, 0.0), // esquina inferior izquierda
            new Coordenada(0.0, 1.0), // esquina inferior derecha
            new Coordenada(1.0, 0.0), // esquina superior izquierda
            new Coordenada(1.0, 1.0), // esquina superior derecha
            new Coordenada(0.0, 0.0)  // cerrar el polígono
        );
        poligonoCuadrado = new Poligono(coordenadasCuadrado);

        // Crear un polígono triangular válido
        List<Coordenada> coordenadasTriangulo = Arrays.asList(
            new Coordenada(0.0, 0.0),
            new Coordenada(1.0, 1.0),
            new Coordenada(0.0, 1.0),
            new Coordenada(0.0, 0.0)  // cerrar el polígono
        );
        poligonoTriangular = new Poligono(coordenadasTriangulo);
    }

    @Test
    void testGuardar_PoligonoValido_SeGuardaCorrectamente() {
        // exercise
        assertDoesNotThrow(() -> {
            poligonoService.guardar(ubicacionId1, poligonoCuadrado);
        });

        // verify
        Optional<Poligono> poligonoRecuperado = poligonoService.recuperarPorUbicacionId(ubicacionId1);
        assertTrue(poligonoRecuperado.isPresent());
        assertEquals(5, poligonoRecuperado.get().getVertices().size());
    }

    @Test
    void testGuardar_MultiplesPoligonos_SeGuardanTodos() {
        // exercise
        poligonoService.guardar(ubicacionId1, poligonoCuadrado);
        poligonoService.guardar(ubicacionId2, poligonoTriangular);

        // assert
        Optional<Poligono> poligono1 = poligonoService.recuperarPorUbicacionId(ubicacionId1);
        Optional<Poligono> poligono2 = poligonoService.recuperarPorUbicacionId(ubicacionId2);

        // verify
        assertTrue(poligono1.isPresent());
        assertTrue(poligono2.isPresent());
        assertEquals(5, poligono1.get().getVertices().size());
        assertEquals(4, poligono2.get().getVertices().size());
    }


    @Test
    void testRecuperarPorUbicacionId_PoligonoExiste_RetornaPoligono() {
        // setup
        poligonoService.guardar(ubicacionId1, poligonoCuadrado);

        // exercise
        Optional<Poligono> resultado = poligonoService.recuperarPorUbicacionId(ubicacionId1);

        // assert
        assertTrue(resultado.isPresent());
        Poligono poligono = resultado.get();
        assertEquals(5, poligono.getVertices().size());

        // verificar que las coordenadas se mantienen
        Coordenada primeraCoordenada = poligono.getVertices().getFirst();
        assertEquals(0.0, primeraCoordenada.getLatitud());
        assertEquals(0.0, primeraCoordenada.getLongitud());
    }

    @Test
    void testRecuperarPorUbicacionId_PoligonoNoExiste_RetornaEmpty() {
        assertTrue(poligonoService.recuperarPorUbicacionId(999L).isEmpty());
    }

    @Test
    void testRecuperarTodos_SinPoligonos_RetornaListaVacia() {
        assertTrue(poligonoService.recuperarTodos().isEmpty());
    }

    @Test
    void testRecuperarTodos_ConVariosPoligonos_RetornaTodos() {
        // setup
        poligonoService.guardar(ubicacionId1, poligonoCuadrado);
        poligonoService.guardar(ubicacionId2, poligonoTriangular);

        // exercise
        List<Poligono> resultado = poligonoService.recuperarTodos();

        // verify
        assertEquals(2, resultado.size());

        // verificar que contiene ambos tipos de polígonos
        boolean tieneCuadrado = resultado.stream()
            .anyMatch(p -> p.getVertices().size() == 5);
        boolean tieneTriangulo = resultado.stream()
            .anyMatch(p -> p.getVertices().size() == 4);

        assertTrue(tieneCuadrado);
        assertTrue(tieneTriangulo);
    }

    @Test
    void testEliminar_PoligonoExiste_SeEliminaCorrectamente() {
        // setup
        poligonoService.guardar(ubicacionId1, poligonoCuadrado);
        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionId1).isPresent());

        // exercise
        poligonoService.eliminar(ubicacionId1);

        // verify
        Optional<Poligono> resultado = poligonoService.recuperarPorUbicacionId(ubicacionId1);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testEliminar_PoligonoNoExiste_NoLanzaExcepcion() {
        assertDoesNotThrow(() -> {
            poligonoService.eliminar(999L);
        });
    }

    @Test
    void testEliminar_EliminaUnoDeVarios_SoloEliminaElIndicado() {
        // setup
        poligonoService.guardar(ubicacionId1, poligonoCuadrado);
        poligonoService.guardar(ubicacionId2, poligonoTriangular);

        // exercise
        poligonoService.eliminar(ubicacionId1);

        // verify
        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionId1).isEmpty());
        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionId2).isPresent());
    }

    @Test
    void testEliminarTodos_ConVariosPoligonos_EliminaTodos() {
        // setup
        poligonoService.guardar(ubicacionId1, poligonoCuadrado);
        poligonoService.guardar(ubicacionId2, poligonoTriangular);
        assertEquals(2, poligonoService.recuperarTodos().size());

        // exercise
        poligonoService.eliminarTodos();

        // verify
        List<Poligono> resultado = poligonoService.recuperarTodos();
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testEliminarTodos_SinPoligonos_NoLanzaExcepcion() {
        assertDoesNotThrow(() -> poligonoService.eliminarTodos());
    }
}