package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.InvocacionNoPermitidaException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UbicacionTest {
    private Ubicacion cementerio;
    private Ubicacion santuario;
    private Espiritu espirituAngel;
    private Espiritu espirituDemonio;
    private Coordenada c1;
    private Coordenada c4;
    private Coordenada c3;
    private Coordenada c2;
    private Poligono poligono;


    @BeforeEach
    void setUp() {
//        c1 = new Coordenada(1.0,1.0);
//        c2 = new Coordenada(2.0,2.0);
//        c3 = new Coordenada(3.0,3.0);
//        c4 = new Coordenada(-1.0,-1.0);
//        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
//        poligono = new Poligono(coordenadas);
        cementerio = new Cementerio("Cementerio del Oeste", 50);
        santuario = new Santuario("Santuario del Este", 75);
        espirituAngel = new EspirituAngelical("Angel", cementerio);
        espirituDemonio = new EspirituDemoniaco("Demonio", santuario);
    }

    @Test
    void testConstructorValido() {
        assertEquals("Cementerio del Oeste", cementerio.getNombre());
        assertEquals(50, cementerio.getFlujoDeEnergia());

        assertEquals("Santuario del Este", santuario.getNombre());
        assertEquals(75, santuario.getFlujoDeEnergia());
    }

    @Test
    void testConstructorFlujoDeEnergiaInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cementerio("Cementerio", 150);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Santuario("Santuario", -10);
        });
    }

    @Test
    void testCambiarNombre() {
        cementerio.cambiarNombre("Nuevo Cementerio");
        assertEquals("Nuevo Cementerio", cementerio.getNombre());

        santuario.cambiarNombre("Nuevo Santuario");
        assertEquals("Nuevo Santuario", santuario.getNombre());
    }

    @Test
    void testCementerioInvocarAngelLanzaExcepcion() {
        assertThrows(InvocacionNoPermitidaException.class, () -> {
            cementerio.invocarAngel(espirituAngel);
        });
    }

    @Test
    void testCementerioInvocarDemonio() {
        cementerio.invocarDemonio(espirituDemonio);
        assertEquals(cementerio, espirituDemonio.getUbicacion());
    }

    @Test
    void testCementerioMoverAngel() {
        espirituAngel.setNivelDeConexion(100);
        cementerio.moverAngel(espirituAngel);
        assertEquals(cementerio, espirituAngel.getUbicacion());
        assertEquals(95, espirituAngel.getNivelDeConexion());
    }

    @Test
    void testCementerioMoverDemonio() {
        cementerio.moverDemonio(espirituDemonio);
        assertEquals(cementerio, espirituDemonio.getUbicacion());
        assertEquals(0, espirituDemonio.getNivelDeConexion());
    }

    @Test
    void testCementerioRecuperarConexionComoDemonio() {
        espirituDemonio.setNivelDeConexion(10);
        cementerio.recuperarConexionComoDemonio(espirituDemonio);
        assertEquals(60, espirituDemonio.getNivelDeConexion());
    }

    @Test
    void testCementerioGetCantidadRecuperada() {
        assertEquals(25, cementerio.getCantidadRecuperada());
    }

    @Test
    void testSantuarioInvocarAngel() {
        santuario.invocarAngel(espirituAngel);
        assertEquals(santuario, espirituAngel.getUbicacion());
    }

    @Test
    void testSantuarioInvocarDemonioLanzaExcepcion() {
        assertThrows(InvocacionNoPermitidaException.class, () -> {
            santuario.invocarDemonio(espirituDemonio);
        });
    }

    @Test
    void testSantuarioMoverAngel() {
        santuario.moverAngel(espirituAngel);
        assertEquals(santuario, espirituAngel.getUbicacion());
        assertEquals(0, espirituAngel.getNivelDeConexion());
    }

    @Test
    void testSantuarioMoverDemonio() {
        espirituDemonio.setNivelDeConexion(100);
        santuario.moverDemonio(espirituDemonio);
        assertEquals(santuario, espirituDemonio.getUbicacion());
        assertEquals(90, espirituDemonio.getNivelDeConexion());
    }

    @Test
    void testSantuarioRecuperarConexionComoAngel() {
        espirituAngel.setNivelDeConexion(10);
        santuario.recuperarConexionComoAngel(espirituAngel);
        assertEquals(85, espirituAngel.getNivelDeConexion());
    }

    @Test
    void testSantuarioGetCantidadRecuperada() {
        assertEquals(112, santuario.getCantidadRecuperada());
    }

}