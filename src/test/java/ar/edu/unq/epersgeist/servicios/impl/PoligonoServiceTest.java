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

        List<Coordenada> coordenadasCuadrado = Arrays.asList(
            new Coordenada(0.0, 0.0),
            new Coordenada(0.0, 1.0),
            new Coordenada(1.0, 1.0),
            new Coordenada(1.0, 0.0),
            new Coordenada(0.0, 0.0)
        );
        poligonoCuadrado = new Poligono(coordenadasCuadrado);

        List<Coordenada> coordenadasTriangulo = Arrays.asList(
            new Coordenada(3.0, 3.0),
            new Coordenada(4.0, 4.0),
            new Coordenada(3.0, 4.0),
            new Coordenada(3.0, 3.0)
        );
        poligonoTriangular = new Poligono(coordenadasTriangulo);

        ubicacionCuadrada = new Cementerio("La Plata", 4);
        ubicacionTriangular = new Santuario("Quilmes",70);
        ubicacionCuadrada = serviceU.guardar(ubicacionCuadrada, poligonoCuadrado);
        ubicacionTriangular = serviceU.guardar(ubicacionTriangular, poligonoTriangular);
        ubicacionCuadradaId = ubicacionCuadrada.getId();
        ubicacionTriangularId = ubicacionTriangular.getId();
    }

    @Test
    void testGuardar_PoligonoValido_SeGuardaCorrectamente() {

        Optional<Poligono> poligonoRecuperado = poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId);
        assertTrue(poligonoRecuperado.isPresent());
        assertEquals(5, poligonoRecuperado.get().getVertices().size());
    }

    @Test
    void testGuardar_MultiplesPoligonos_SeGuardanTodos() {
        Optional<Poligono> poligono1 = poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId);
        Optional<Poligono> poligono2 = poligonoService.recuperarPorUbicacionId(ubicacionTriangularId);

        assertTrue(poligono1.isPresent());
        assertTrue(poligono2.isPresent());
        assertEquals(5, poligono1.get().getVertices().size());
        assertEquals(4, poligono2.get().getVertices().size());
    }


    @Test
    void testRecuperarPorUbicacionId_PoligonoExiste_RetornaPoligono() {

        Optional<Poligono> resultado = poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId);


        assertTrue(resultado.isPresent());
        Poligono poligono = resultado.get();
        assertEquals(5, poligono.getVertices().size());

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
        dataService.eliminarTodo();
        assertTrue(poligonoService.recuperarTodos().isEmpty());
    }

    @Test
    void testRecuperarTodos_ConVariosPoligonos_RetornaTodos() {

        List<Poligono> resultado = poligonoService.recuperarTodos();

        assertEquals(2, resultado.size());

        boolean tieneCuadrado = resultado.stream()
            .anyMatch(p -> p.getVertices().size() == 5);
        boolean tieneTriangulo = resultado.stream()
            .anyMatch(p -> p.getVertices().size() == 4);

        assertTrue(tieneCuadrado);
        assertTrue(tieneTriangulo);
    }

    @Test
    void testEliminar_PoligonoExiste_SeEliminaCorrectamente() {

        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId).isPresent());

        poligonoService.eliminar(ubicacionCuadradaId);

        Optional<Poligono> resultado = poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testEliminar_PoligonoNoExiste_NoLanzaExcepcion() {
        assertDoesNotThrow(() ->
            poligonoService.eliminar(999L)
        );
    }

    @Test
    void testEliminar_EliminaUnoDeVarios_SoloEliminaElIndicado() {

        poligonoService.eliminar(ubicacionCuadradaId);

        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionCuadradaId).isEmpty());
        assertTrue(poligonoService.recuperarPorUbicacionId(ubicacionTriangularId).isPresent());
    }

    @Test
    void testEliminarTodos_ConVariosPoligonos_EliminaTodos() {
        assertEquals(2, poligonoService.recuperarTodos().size());

        poligonoService.eliminarTodos();

        List<Poligono> resultado = poligonoService.recuperarTodos();
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testEliminarTodos_SinPoligonos_NoLanzaExcepcion() {
        assertDoesNotThrow(() -> poligonoService.eliminarTodos());
    }

    @AfterEach
    void eliminar() {
        dataService.eliminarTodo();
    }

}