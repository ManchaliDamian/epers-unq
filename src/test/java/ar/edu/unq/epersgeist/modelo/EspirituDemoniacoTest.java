package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.EspirituNoPuedeMoverse;
import ar.edu.unq.epersgeist.modelo.generador.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituDemoniacoTest {

    private Espiritu demonio;
    private Espiritu angel;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Medium mediumAngel;
    private Medium mediumDemon;

    @BeforeEach
    void setUp() {
        santuario = new Santuario("Quilmes", 70);
        cementerio = new Cementerio("Bernal",60);

        angel = new EspirituAngelical( "EspirituAngelical", santuario);
        demonio = new EspirituDemoniaco( "EspirituDemoniaco", cementerio);
        mediumAngel= new Medium("Mago", 100, 50, santuario);
        mediumDemon = new Medium("Maguito",100, 10, cementerio);

    }

    @Test
    void espirituDemoniacoRecibeAtaque() {
        mediumAngel.conectarseAEspiritu(angel);
        mediumDemon.conectarseAEspiritu(demonio);

        angel.setNivelDeConexion(20);
        demonio.setNivelDeConexion(20);

        Generador.setEstrategia(new GeneradorSecuencial(10));

        angel.atacar(demonio);

        assertEquals(10, demonio.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoSeDesconectaDelMediumPorFaltaDeNivelConexion() {
        mediumAngel.conectarseAEspiritu(angel);
        mediumDemon.conectarseAEspiritu(demonio);

        angel.setNivelDeConexion(20);
        demonio.setNivelDeConexion(5);

        Generador.setEstrategia(new GeneradorSecuencial(30, 5));

        angel.atacar(demonio);

        assertNull(demonio.getMediumConectado());
        assertEquals(0, demonio.getNivelDeConexion());
        assertEquals(0, mediumDemon.getEspiritus().size());
    }

    @Test
    void recibirAtaque_DisminuyeNivelDeConexion() {
        demonio.setNivelDeConexion(30);
        demonio.perderNivelDeConexion(15);

        assertEquals(15, demonio.getNivelDeConexion());
    }

    @Test
    void recibirAtaque_NoBajaDeCero() {
        demonio.setNivelDeConexion(10);
        demonio.perderNivelDeConexion(15);

        assertEquals(0, demonio.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeCementerio_AumentaNivelDeConexion() {
        demonio.setNivelDeConexion(30);

        demonio.recuperarConexionEn(cementerio);

        assertEquals(90, demonio.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeCementerio_NoExcedeMaximo() {
        demonio.setNivelDeConexion(50);

        demonio.recuperarConexionEn(cementerio);

        assertEquals(100, demonio.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeSantuario_NoHaceNada() {
        demonio.setNivelDeConexion(50);

        demonio.recuperarConexionEn(santuario);

        assertEquals(50, demonio.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoPuedeMoverseAUnaUbicacion() {
        mediumDemon.conectarseAEspiritu(demonio);
        mediumDemon.mover(cementerio);
        assertEquals(cementerio, demonio.getUbicacion());
    }
    @Test
    void espirituDemoniacoNoPuedeMoverseAUnaUbicacion_mediumNoSeMueve() {
        mediumDemon.conectarseAEspiritu(demonio);

        assertThrows(EspirituNoPuedeMoverse.class, () -> demonio.mover(santuario));

        assertEquals(cementerio, demonio.getUbicacion());
    }
    @Test
    void espirituDemoniacoNoPuedeMoverseAUnaUbicacion_porqueNoEstaConectado() {
        assertThrows(EspirituNoPuedeMoverse.class, () -> demonio.mover(cementerio));
    }

    @Test
    public void moverDemonioASantuarioCambiaSuUbicacionYLeBaja10DeEnergia(){
        demonio.setNivelDeConexion(80);
        mediumDemon.conectarseAEspiritu(demonio);//82
        mediumDemon.mover(santuario);
        assertEquals(72, demonio.getNivelDeConexion());
        assertEquals("Quilmes", demonio.getUbicacion().getNombre());
    }

    @Test
    public void invocarDemonioACementerioCambiaSuUbicacion(){
        demonio.serInvocadoEn(cementerio);

        assertEquals(cementerio, demonio.getUbicacion());
    }
}



