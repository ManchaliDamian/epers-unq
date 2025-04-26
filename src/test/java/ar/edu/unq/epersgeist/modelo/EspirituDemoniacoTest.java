package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituDemoniacoTest {

    private EspirituDemoniaco espirituDemoniaco;
    private EspirituAngelical espirituAngelical;
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
        Cementerio cementerio = new Cementerio("Test", 30);
        espirituDemoniaco.setNivelDeConexion(50);

        espirituDemoniaco.recibirEfectoDe(cementerio);

        assertEquals(80, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeCementerio_NoExcedeMaximo() {
        Cementerio cementerio = new Cementerio("Test", 60);
        espirituDemoniaco.setNivelDeConexion(50);

        espirituDemoniaco.recibirEfectoDe(cementerio);

        // 50 + 60 = 110, pero el máximo es 100
        assertEquals(100, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void recibirAtaque_DisminuyeNivelDeConexion() {
        espirituDemoniaco.setNivelDeConexion(30);
        espirituDemoniaco.recibirAtaque(15);

        assertEquals(15, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void recibirAtaque_NoBajaDeCero() {
        espirituDemoniaco.setNivelDeConexion(10);
        espirituDemoniaco.recibirAtaque(15);

        assertEquals(0, espirituDemoniaco.getNivelDeConexion());
    }
}



