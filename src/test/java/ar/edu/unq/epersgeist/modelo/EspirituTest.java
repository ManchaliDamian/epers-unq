package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class EspirituTest {
    private Espiritu angel;
    private Espiritu demonio;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Medium mediumConectado;
//    private Coordenada c1;
//    private Coordenada c4;
//    private Coordenada c3;
//    private Coordenada c2;
//    private Poligono poligono;


    @BeforeEach
    void setUp(){
//        c1 = new Coordenada(1.0,1.0);
//        c2 = new Coordenada(2.0,2.0);
//        c3 = new Coordenada(3.0,3.0);
//        c4 = new Coordenada(-1.0,-1.0);
//        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
//        poligono = new Poligono(coordenadas);
        santuario = new Santuario("santuario", 40);
        cementerio = new Cementerio("cementerio", 60);

        mediumConectado = new Medium("Mago",100,90,cementerio);
        angel = new EspirituAngelical("Espiritu",cementerio);
        demonio = new EspirituDemoniaco("Espiritu", santuario);
    }

    @Test
    void aumentaNivelDeConexionDelEspiritu(){
        mediumConectado.conectarseAEspiritu(angel);

        assertEquals(18,angel.getNivelDeConexion());
    }

    @Test
    void espirituRecienSeConectaYNoTieneId(){
        assertNull(angel.getId());
    }

    @Test
    void perderNivelDeConexionConCiertaCantidad(){
        mediumConectado.conectarseAEspiritu(angel);

        angel.perderNivelDeConexion(5);
        assertEquals(13,angel.getNivelDeConexion());
    }

    @Test
    void espirituNoPuedePerderNivelDeConexionUnaVezYSerNegativo(){
        mediumConectado.conectarseAEspiritu(angel);

        angel.perderNivelDeConexion(300);
        assertEquals(0,angel.getNivelDeConexion());
        assertNull(angel.getMediumConectado());
        assertFalse(mediumConectado.getEspiritus().contains(angel));
    }

    @Test
    void espirituNoPuedePerderNivelDeConexionVariasVecesYSerNegativo(){
        mediumConectado.conectarseAEspiritu(angel);

        angel.perderNivelDeConexion(10);
        angel.perderNivelDeConexion(10);
        angel.perderNivelDeConexion(10);
        assertEquals(0,angel.getNivelDeConexion());
        assertNull(angel.getMediumConectado());
        assertFalse(mediumConectado.getEspiritus().contains(angel));
    }


    @Test
    void elEspirituNoTieneMediumConectado(){
        assertNull(angel.getMediumConectado());
    }

    @Test
    void elEspirituTieneUnMediumConectado(){
        angel.setMediumConectado(mediumConectado);
        assertTrue(angel.estaConectado());
    }

    @Test
    void elEspirituDescansa(){
        angel.descansar(santuario);
        assertEquals(40,angel.getNivelDeConexion());
    }

    @Test
    void conectarA_SeteaElMedium(){
        angel.conectarA(mediumConectado);
        assertEquals(angel.getMediumConectado().getId(), mediumConectado.getId());
    }


    @Test
    void conectarA_AumentaLaConexionCorrectamente() {
        angel.setNivelDeConexion(60);
        angel.conectarA(mediumConectado);

        // el aumento debería ser el 20% de 90, o sea 18
        assertEquals(78, angel.getNivelDeConexion());
        assertEquals(angel.getMediumConectado().getId(), mediumConectado.getId());
    }

    @Test
    void conectarA_NoSuperaElMaximoDe100() {
        angel.setNivelDeConexion(95);
        angel.conectarA(mediumConectado);

        assertEquals(100, angel.getNivelDeConexion());
        assertEquals(angel.getMediumConectado().getId(), mediumConectado.getId());
    }

    @Test
    void aumentarConexion_ConManaCero() {
        Medium mediumSinMana = new Medium("Novato", 100, 0, santuario);
        angel.setNivelDeConexion(50);
        angel.conectarA(mediumSinMana);
        assertEquals(50, angel.getNivelDeConexion());
    }

    @Test
    void perderNivelDeConexion_CantidadNegativa() {
        angel.setNivelDeConexion(50);
        assertThrows(IllegalArgumentException.class, () -> {
            angel.perderNivelDeConexion(-10);
        });
    }

    @Test
    void conectarA_MediumNuloLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> {
            angel.conectarA(null);
        });
    }

    @Test
    void descansar_UbicacionNulaLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> {
            angel.descansar(null);
        });
    }
}
