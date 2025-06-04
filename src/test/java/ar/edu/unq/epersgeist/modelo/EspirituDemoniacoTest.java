package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.generador.*;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituDemoniacoTest {

    private Espiritu demonio;
    private Espiritu angel;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Medium mediumAngel;
    private Medium mediumDemon;
    private Coordenada c1;
    private Coordenada c4;
    private Coordenada c3;
    private Coordenada c2;
    private Poligono poligono;

    @BeforeEach
    void setUp() {
        c1 = new Coordenada(1.0,1.0);
        c2 = new Coordenada(2.0,2.0);
        c3 = new Coordenada(3.0,3.0);
        c4 = new Coordenada(-1.0,-1.0);
        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
        poligono = new Poligono(coordenadas);
        santuario = new Santuario("Quilmes", 70, poligono);
        cementerio = new Cementerio("Bernal",60, poligono);

        angel = new EspirituAngelical( "EspirituAngelical", santuario,c1);
        demonio = new EspirituDemoniaco( "EspirituDemoniaco", cementerio, c1);
        mediumAngel= new Medium("Mago", 100, 50, santuario,c1);
        mediumDemon = new Medium("Maguito",100, 10, cementerio,c1);

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



