package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.generador.Generador;
import ar.edu.unq.epersgeist.modelo.generador.GeneradorSecuencial;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class EspirituAngelicalTest {
    private Espiritu demonio;
    private Espiritu angel;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Medium mediumConectado;

    @BeforeEach
    void setUp(){
        santuario = new Santuario("Quilmes", 70);
        cementerio = new Cementerio("Bernal",60);

        angel = new EspirituAngelical("EspirituAngelical", santuario);
        demonio = new EspirituDemoniaco( "EspirituDemoniaco", cementerio);
        mediumConectado = new Medium("Mago",100,50,santuario);

    }

    @Test
    void espirituAngelicalAtacaConExitoAlDemoniaco() {
        demonio.setNivelDeConexion(20);
        angel.setNivelDeConexion(20);
        Generador.setEstrategia(new GeneradorSecuencial(10));

        angel.atacar(demonio);

        assertEquals(10, demonio.getNivelDeConexion());

    }

    @Test
    void espirituAngelicalFallaAlAtacarAlDemoniaco() {
        angel.setNivelDeConexion(10);
        demonio.setNivelDeConexion(20);

        Generador.setEstrategia(new GeneradorSecuencial(5,100));
        angel.atacar(demonio);

        assertEquals(5, angel.getNivelDeConexion());
        assertEquals(20, demonio.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeSantuario_AumentaNivelDeConexion() {
        Santuario santuario = new Santuario("Test", 30);
        angel.setNivelDeConexion(50);

        angel.recuperarConexionEn(santuario);

        assertEquals(80, angel.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeSantuario_NoExcedeMaximo() {
        Santuario santuario = new Santuario("Test", 60);
        angel.setNivelDeConexion(50);

        angel.recuperarConexionEn(santuario);

        assertEquals(100, angel.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeCementerio_NoHaceNada() {
        angel.setNivelDeConexion(50);

        angel.recuperarConexionEn(cementerio);

        assertEquals(50, angel.getNivelDeConexion());
    }

    @Test
    void espirituAngelicalPuedeMoverseAUnaUbicacion() {
        mediumConectado.conectarseAEspiritu(angel);
        mediumConectado.mover(cementerio);
        assertEquals(cementerio, angel.getUbicacion());
    }

    @Test
    public void moverAngelACementerioCambiaSuUbicacionYLeBaja5DeEnergia(){
        angel.setNivelDeConexion(100);
        mediumConectado.conectarseAEspiritu(angel);//100
        mediumConectado.mover(cementerio);
        assertEquals(95, angel.getNivelDeConexion());
        assertEquals("Bernal", angel.getUbicacion().getNombre());
    }

    @Test
    public void invocarAngelASantuarioCambiaSuUbicacion(){
        angel.serInvocadoEn(santuario);

        assertEquals(santuario, angel.getUbicacion());
    }
}
