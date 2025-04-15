package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;
import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EspirituTest {
    private Espiritu espiritu;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private Medium mediumConectado;
    private GeneradorDeNumeros generadorMock;

    @BeforeEach
    void setUp(){
        bernal = new Ubicacion("Bernal");
        quilmes = new Ubicacion("Quilmes");
        generadorMock = mock(GeneradorDeNumeros.class);

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
    void estaEnLaMismaUbicacionDelMedium(){
        assertTrue(espiritu.esMismaUbicacion(mediumConectado));
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

    @Test
    void elEspirituExcedeDelNivelDeConexiob(){
        mediumConectado.conectarseAEspiritu(espiritu);
        espiritu.setNivelDeConexion(105);
        assertThrows(NivelDeConexionException.class, () -> {
            espiritu.validarNivelDeConexion(105);
        });
    }

    @Test
    void elEspirituNoPuedeAumentarElNivelDeConexion(){

        assertThrows(ConectarException.class, () -> {
            espiritu.aumentarConexion(mediumConectado);
        });
    }

    @Test
    void elEspirituNoTieneMismaUbicacion(){
        mediumConectado.setUbicacion(bernal);
        assertThrows(EspirituNoEstaEnLaMismaUbicacionException.class, () -> {
            espiritu.estaEnLaMismaUbicacion(mediumConectado);
        });
    }

    @Test
    void validarDisponibilidadDelEspirituTest(){
        mediumConectado.conectarseAEspiritu(espiritu);
        assertThrows(ExceptionEspirituOcupado.class, () -> {
            espiritu.validarDisponibilidad();
        });
    }
}
