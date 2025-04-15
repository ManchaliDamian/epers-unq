package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EspirituDemoniacoTest {

    private EspirituDemoniaco espirituDemoniaco;
    private EspirituAngelical espirituAngelical;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private Medium mediumAngel;
    private Medium mediumDemon;
    private GeneradorDeNumeros generadorMock;

    @BeforeEach
    void setUp() {
        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");
        generadorMock = mock(GeneradorDeNumeros.class);

        espirituAngelical = new EspirituAngelical( "EspirituAngelical", quilmes, generadorMock);
        espirituDemoniaco = new EspirituDemoniaco( "EspirituDemoniaco", bernal, generadorMock);
        mediumAngel= new Medium("Mago", 100, 50, quilmes);
        mediumDemon = new Medium("Maguito",100, 10, bernal);

    }

    @Test
    void espirituDemoniacoRecibeAtaque() {
        mediumAngel.conectarseAEspiritu(espirituAngelical);
        mediumDemon.conectarseAEspiritu(espirituDemoniaco);
        when(generadorMock.entre(1, 10)).thenReturn(5);
        when(generadorMock.entre(1, 100)).thenReturn(20);

        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(2, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoSeDesconectaDelMediumPorFaltaDeNivelConexion() {
        mediumAngel.conectarseAEspiritu(espirituAngelical);
        mediumDemon.conectarseAEspiritu(espirituDemoniaco);


        when(generadorMock.entre(1, 10)).thenReturn(2);
        when(generadorMock.entre(1, 100)).thenReturn(1);

        espirituAngelical.atacar(espirituDemoniaco);

        assertNull(espirituDemoniaco.getMediumConectado());
        assertEquals(0, espirituDemoniaco.getNivelDeConexion());
        assertEquals(0, mediumDemon.getEspiritus().size());
    }
}

