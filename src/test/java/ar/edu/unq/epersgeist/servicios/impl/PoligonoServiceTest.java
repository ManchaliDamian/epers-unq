package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.PoligonoService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.AfterEach;
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

    @Autowired private DataService dataService;
    @Autowired private PoligonoService poligonoService;
    @Autowired private UbicacionService serviceU;

    private Poligono poligonoCuadrado;
    private Poligono poligonoTriangular;
    private Ubicacion ubicacionCuadrada;
    private Ubicacion ubicacionTriangular;
    private Long ubicacionCuadradaId;
    private Long ubicacionTriangularId;

    @BeforeEach
    void setUp() {
        // limpiar las bases de datos antes de cada test
        dataService.eliminarTodo();

        // crear ubicaciones y persistirlas
        ubicacionCuadrada = new Cementerio("La Plata", 4);
        ubicacionTriangular = new Santuario("Quilmes",70);
        ubicacionCuadrada = serviceU.guardar(ubicacionCuadrada, poligonoCuadrado);
        ubicacionTriangular = serviceU.guardar(ubicacionTriangular, poligonoTriangular);
        ubicacionCuadradaId = ubicacionCuadrada.getId();
        ubicacionTriangularId = ubicacionTriangular.getId();

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
            poligonoService.guardar(ubicacionCuadradaId, poligonoCuadrado);
        });

        // verify
        Optional<Poligono> poligonoRecuperado = poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId);
        assertTrue(poligonoRecuperado.isPresent());
        assertEquals(5, poligonoRecuperado.get().getVertices().size());
    }

    @Test
    void testGuardar_MultiplesPoligonos_SeGuardanTodos() {
        // exercise
        poligonoService.guardar(ubicacionCuadradaId, poligonoCuadrado);
        poligonoService.guardar(ubicacionTriangularId, poligonoTriangular);

        // assert
        Optional<Poligono> poligono1 = poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId);
        Optional<Poligono> poligono2 = poligonoService.recuperarPorUbicacionId(ubicacionTriangularId);

        // verify
        assertTrue(poligono1.isPresent());
        assertTrue(poligono2.isPresent());
        assertEquals(5, poligono1.get().getVertices().size());
        assertEquals(4, poligono2.get().getVertices().size());
    }


    @Test
    void testRecuperarPorUbicacionId_PoligonoExiste_RetornaPoligono() {
        // setup
        poligonoService.guardar(ubicacionCuadradaId, poligonoCuadrado);

        // exercise
        Optional<Poligono> resultado = poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId);

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
        poligonoService.guardar(ubicacionCuadradaId, poligonoCuadrado);
        poligonoService.guardar(ubicacionTriangularId, poligonoTriangular);

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
        poligonoService.guardar(ubicacionCuadradaId, poligonoCuadrado);
        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId).isPresent());

        // exercise
        poligonoService.eliminar(ubicacionCuadradaId);

        // verify
        Optional<Poligono> resultado = poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId);
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
        poligonoService.guardar(ubicacionCuadradaId, poligonoCuadrado);
        poligonoService.guardar(ubicacionTriangularId, poligonoTriangular);

        // exercise
        poligonoService.eliminar(ubicacionCuadradaId);

        // verify
        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId).isEmpty());
        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionTriangularId).isPresent());
    }

    @Test
    void testEliminarTodos_ConVariosPoligonos_EliminaTodos() {
        // setup
        poligonoService.guardar(ubicacionCuadradaId, poligonoCuadrado);
        poligonoService.guardar(ubicacionTriangularId, poligonoTriangular);
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

//    @AfterEach
    void eliminar() {
        dataService.eliminarTodo();
    }


}