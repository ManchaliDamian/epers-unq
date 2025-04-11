package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EspirituDemoniacoTest {

    private EspirituDemoniaco espirituDemoniaco;
    private EspirituAngelical espirituAngelical;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private Medium mediumConectado;
    private GeneradorDeNumeros generadorMock;

    @BeforeEach
    void setUp() {
        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");
        generadorMock = mock(GeneradorDeNumeros.class);

        espirituAngelical = new EspirituAngelical(30, "EspirituAngelical", quilmes, generadorMock);
        espirituDemoniaco = new EspirituDemoniaco(25, "EspirituDemoniaco", bernal, generadorMock);
        mediumConectado = new Medium("Mago", 100, 50, quilmes);

        espirituDemoniaco.setMediumConectado(mediumConectado);
        espirituAngelical.setMediumConectado(mediumConectado);
    }

    @Test
    void espirituDemoniacoRecibeAtaque() {
        when(generadorMock.entre(1, 10)).thenReturn(5);
        when(generadorMock.entre(1, 100)).thenReturn(20);

        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(10, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoSeDesconectaDelMediumPorFaltaDeNivelConexion() {
        espirituDemoniaco.setNivelDeConexion(0);

        when(generadorMock.entre(1, 10)).thenReturn(5);
        when(generadorMock.entre(1, 100)).thenReturn(20);

        espirituAngelical.atacar(espirituDemoniaco);

        assertNull(espirituDemoniaco.getMediumConectado());
        assertEquals(0, espirituDemoniaco.getNivelDeConexion());
        assertEquals(0, mediumConectado.getEspiritus().size());
    }
}

