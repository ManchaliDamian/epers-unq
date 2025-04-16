package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituDemoniacoTest {

    private EspirituDemoniaco espirituDemoniaco;
    private EspirituAngelical espirituAngelical;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private Medium mediumAngel;
    private Medium mediumDemon;

    @BeforeEach
    void setUp() {
        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");

        espirituAngelical = new EspirituAngelical( "EspirituAngelical", quilmes);
        espirituDemoniaco = new EspirituDemoniaco( "EspirituDemoniaco", bernal);
        mediumAngel= new Medium("Mago", 100, 50, quilmes);
        mediumDemon = new Medium("Maguito",100, 10, bernal);

    }

    @Test
    void espirituDemoniacoRecibeAtaque() {
        mediumAngel.conectarseAEspiritu(espirituAngelical);
        mediumDemon.conectarseAEspiritu(espirituDemoniaco);

        espirituAngelical.setNivelDeConexion(20);
        espirituDemoniaco.setNivelDeConexion(20);

        // Ataca con éxito, baja 10 puntos
        Generador.setEstrategia(new GeneradorFijo(10));

        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(10, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoSeDesconectaDelMediumPorFaltaDeNivelConexion() {
        mediumAngel.conectarseAEspiritu(espirituAngelical);
        mediumDemon.conectarseAEspiritu(espirituDemoniaco);

        espirituAngelical.setNivelDeConexion(20);
        espirituDemoniaco.setNivelDeConexion(5);

        Generador.setEstrategia(new GeneradorFijo(100));

        espirituAngelical.atacar(espirituDemoniaco);

        assertNull(espirituDemoniaco.getMediumConectado());
        assertEquals(0, espirituDemoniaco.getNivelDeConexion());
        assertEquals(0, mediumDemon.getEspiritus().size());
    }
}



