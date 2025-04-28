package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.generador.Generador;
import ar.edu.unq.epersgeist.modelo.generador.GeneradorSecuencial;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituDemoniacoTest {

    private Espiritu espirituDemoniaco;
    private Espiritu espirituAngelical;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Medium mediumAngel;
    private Medium mediumDemon;

    @BeforeEach
    void setUp() {
        santuario = new Santuario("Quilmes", 70);
        cementerio = new Cementerio("Bernal",60);

        espirituAngelical = new EspirituAngelical( "EspirituAngelical", santuario);
        espirituDemoniaco = new EspirituDemoniaco( "EspirituDemoniaco", cementerio);
        mediumAngel= new Medium("Mago", 100, 50, santuario);
        mediumDemon = new Medium("Maguito",100, 10, cementerio);

    }

    @Test
    void espirituDemoniacoRecibeAtaque() {
        mediumAngel.conectarseAEspiritu(espirituAngelical);
        mediumDemon.conectarseAEspiritu(espirituDemoniaco);

        espirituAngelical.setNivelDeConexion(20);
        espirituDemoniaco.setNivelDeConexion(20);

        Generador.setEstrategia(new GeneradorSecuencial(10));

        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(10, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoSeDesconectaDelMediumPorFaltaDeNivelConexion() {
        mediumAngel.conectarseAEspiritu(espirituAngelical);
        mediumDemon.conectarseAEspiritu(espirituDemoniaco);

        espirituAngelical.setNivelDeConexion(20);
        espirituDemoniaco.setNivelDeConexion(5);

        Generador.setEstrategia(new GeneradorSecuencial(30, 5));

        espirituAngelical.atacar(espirituDemoniaco);

        assertNull(espirituDemoniaco.getMediumConectado());
        assertEquals(0, espirituDemoniaco.getNivelDeConexion());
        assertEquals(0, mediumDemon.getEspiritus().size());
    }

    @Test
    void recibirEfectoDeCementerio_AumentaNivelDeConexion() {
        espirituDemoniaco.setNivelDeConexion(30);

        espirituDemoniaco.recuperarConexionEn(cementerio);

        assertEquals(90, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeCementerio_NoExcedeMaximo() {
        espirituDemoniaco.setNivelDeConexion(50);

        espirituDemoniaco.recuperarConexionEn(cementerio);

        assertEquals(100, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeSantuario_NoHaceNada() {
        espirituDemoniaco.setNivelDeConexion(50);

        espirituDemoniaco.recuperarConexionEn(santuario);

        assertEquals(50, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void recibirAtaque_DisminuyeNivelDeConexion() {
        espirituDemoniaco.setNivelDeConexion(30);
        espirituDemoniaco.perderNivelDeConexion(15);

        assertEquals(15, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void recibirAtaque_NoBajaDeCero() {
        espirituDemoniaco.setNivelDeConexion(10);
        espirituDemoniaco.perderNivelDeConexion(15);

        assertEquals(0, espirituDemoniaco.getNivelDeConexion());
    }
}



