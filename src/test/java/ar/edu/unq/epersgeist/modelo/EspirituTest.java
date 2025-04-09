package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;
import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituTest {
    private Espiritu espiritu;
    private Ubicacion ubicacion;
    private Ubicacion otraUbicacion;
    private Medium mediumConectado;

    @BeforeEach
    void setUp(){
        otraUbicacion = new Ubicacion("Bernal");
        ubicacion = new Ubicacion("Quilmes");
        mediumConectado = new Medium("Mago",100,50,ubicacion);
        espiritu = new EspirituAngelical(50,"Espiritu",ubicacion);
    }

    @Test
    void aumentaNivelDeConexionDelEspiritu(){
        mediumConectado.conectarseAEspiritu(espiritu);
        espiritu.aumentarConexion(mediumConectado);
        assertEquals(60,espiritu.getNivelDeConexion());
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
        espiritu.conexionEnAumento(mediumConectado);
        assertEquals(60,espiritu.getNivelDeConexion());
    }

    @Test
    void perderNivelDeConexionConCiertaCantidad(){
        espiritu.perderNivelDeConexion(5);
        assertEquals(45,espiritu.getNivelDeConexion());
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
        assertEquals(55,espiritu.getNivelDeConexion());
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
        mediumConectado.setUbicacion(otraUbicacion);
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
