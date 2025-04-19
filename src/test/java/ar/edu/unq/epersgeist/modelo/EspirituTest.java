package ar.edu.unq.epersgeist.modelo;


import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;
import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EspirituTest {
    private Espiritu espiritu;
    private Ubicacion quilmes;
    private Medium mediumConectado;

    @BeforeEach
    void setUp(){
        quilmes = new Ubicacion("Quilmes");

        mediumConectado = new Medium("Mago",100,90,quilmes);
        espiritu = new EspirituAngelical("Espiritu",quilmes);
    }

    @Test
    void aumentaNivelDeConexionDelEspiritu(){
        mediumConectado.conectarseAEspiritu(espiritu);

        assertEquals(18,espiritu.getNivelDeConexion());
    }

    @Test
    void espirituRecienSeConectaYNoTieneId(){
        assertNull(espiritu.getId());
    }

    @Test
    void puedeAumentarLaConexionDelEspiritu(){
        mediumConectado.conectarseAEspiritu(espiritu);

        assertEquals(18,espiritu.getNivelDeConexion());
    }

    @Test
    void perderNivelDeConexionConCiertaCantidad(){
        mediumConectado.conectarseAEspiritu(espiritu);

        espiritu.perderNivelDeConexion(5);
        assertEquals(13,espiritu.getNivelDeConexion());
    }

    @Test
    void elEspirituNoTieneMediumConectado(){
        assertNull(espiritu.getMediumConectado());
    }

    @Test
    void elEspirituTieneUnMediumConectado(){
        espiritu.setMediumConectado(mediumConectado);
        //Es falso porque no está libre.
        assertTrue(espiritu.estaConectado());
    }

    @Test
    void elEspirituDescansa(){
        espiritu.descansar();
        assertEquals(5,espiritu.getNivelDeConexion());
    }
}
