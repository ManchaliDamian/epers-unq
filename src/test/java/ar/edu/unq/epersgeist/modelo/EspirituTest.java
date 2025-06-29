package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.Conflict.EspirituDominadoException;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituNoDominableException;
import ar.edu.unq.epersgeist.modelo.generador.Generador;
import ar.edu.unq.epersgeist.modelo.generador.GeneradorSecuencial;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EspirituTest {
    private Espiritu angel;
    private Espiritu demonio;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Medium mediumConectado;

    @BeforeEach
    void setUp(){
        santuario = new Santuario("santuario", 40);
        cementerio = new Cementerio("cementerio", 60);

        mediumConectado = new Medium("Medium",100,90, cementerio);
        angel = new EspirituAngelical("Angel",cementerio);
        demonio = new EspirituDemoniaco("Demonio", santuario);
    }

    @Test
    void combatirMayorAtaque(){
        Generador.setEstrategia(new GeneradorSecuencial(30, 5));
        angel.combatir(demonio);
        assertEquals(70, demonio.getVida());
        assertEquals(98, angel.getVida()); //pierde vida por haber iniciado combate

        assertEquals(1, angel.getBatallasJugadas());
        assertEquals(1, angel.getBatallasGanadas());
        assertEquals(0, angel.getBatallasPerdidas());

        assertEquals(1, demonio.getBatallasJugadas());
        assertEquals(0, demonio.getBatallasGanadas());
        assertEquals(1, demonio.getBatallasPerdidas());
    }

    @Test
    void combatirMayorDefensa(){
        Generador.setEstrategia(new GeneradorSecuencial(5, 40));
        angel.combatir(demonio);
        assertEquals(58, angel.getVida());
        assertEquals(100, demonio.getVida());//gano, no pierde vida

        assertEquals(1, angel.getBatallasJugadas());
        assertEquals(0, angel.getBatallasGanadas());
        assertEquals(1, angel.getBatallasPerdidas());

        assertEquals(1, demonio.getBatallasJugadas());
        assertEquals(1, demonio.getBatallasGanadas());
        assertEquals(0, demonio.getBatallasPerdidas());
    }

    @Test
    void dominar(){
        demonio.setNivelDeConexion(40);
        angel = angel.dominar(demonio);
        assertEquals(angel, demonio.getDominador());
    }
    @Test
    void intentarDominarAMas50DeEnergia(){
        demonio.setNivelDeConexion(50);
        angel = angel.dominar(demonio);
        assertNull(demonio.getDominador());
    }
    @Test
    void dominarSiendoDominado(){
        demonio.setNivelDeConexion(40);
        angel.setNivelDeConexion(40);
        angel = angel.dominar(demonio);

        assertThrows(EspirituNoDominableException.class,() -> demonio.dominar(angel));
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
        Espiritu otroDominador = new EspirituAngelical("Dominador", cementerio);
        angel.setDominador(otroDominador);
        assertThrows(EspirituDominadoException.class, () ->
            angel.conectarA(mediumConectado)
        );
    }

    @Test
    void conectarA_AumentaLaConexionCorrectamente() {
        angel.setNivelDeConexion(60);
        angel.conectarA(mediumConectado);

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
        assertThrows(IllegalArgumentException.class, () ->
            angel.perderNivelDeConexion(-10)
        );
    }

    @Test
    void conectarA_MediumNuloLanzaExcepcion() {
        assertThrows(NullPointerException.class, () ->
            angel.conectarA(null)
        );
    }

    @Test
    void descansar_UbicacionNulaLanzaExcepcion() {
        assertThrows(NullPointerException.class, () ->
            angel.descansar(null)
        );
    }
}
