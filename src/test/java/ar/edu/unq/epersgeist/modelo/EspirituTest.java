package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.EspirituDominadoException;
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
    private Coordenada c1;




    @BeforeEach
    void setUp(){
        c1 = new Coordenada(1.0,1.0);
        santuario = new Santuario("santuario", 40);
        cementerio = new Cementerio("cementerio", 60);

        mediumConectado = new Medium("Medium",100,90, cementerio, c1);
        angel = new EspirituAngelical("Angel",cementerio,c1);
        demonio = new EspirituDemoniaco("Demonio", santuario,c1);
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
    void conectarA_EspirituDominado_LanzaExcepcion() {
        Espiritu otroDominador = new EspirituAngelical("Dominador", cementerio, c1);
        angel.setDominador(otroDominador);
        assertThrows(EspirituDominadoException.class, () -> {
            angel.conectarA(mediumConectado);
        });
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
        Medium mediumSinMana = new Medium("Novato", 100, 0, santuario, c1);
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
