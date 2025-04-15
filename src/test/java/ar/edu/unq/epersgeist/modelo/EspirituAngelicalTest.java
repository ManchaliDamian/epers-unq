package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EspirituAngelicalTest {
    private EspirituDemoniaco espirituDemoniaco;
    private EspirituAngelical espirituAngelical;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private Medium mediumConectado;
    private GeneradorDeNumeros generadorMock;

    @BeforeEach
    void setUp(){
        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");
        generadorMock = mock(GeneradorDeNumeros.class);

        espirituAngelical = new EspirituAngelical("EspirituAngelical", quilmes);
        espirituDemoniaco = new EspirituDemoniaco( "EspirituDemoniaco", bernal);
        mediumConectado = new Medium("Mago",100,50,quilmes);
        espirituDemoniaco.setMediumConectado(mediumConectado);
        espirituAngelical.setMediumConectado(mediumConectado);
    }

    @Test
    void espirituAngelicalAtacaConExitoAlDemoniaco() {
        when(generadorMock.entre(1, 10)).thenReturn(5);
        when(generadorMock.entre(1, 100)).thenReturn(30);

        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(10, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void espirituAngelicalFallaAlAtacarAlDemoniaco() {
        when(generadorMock.entre(1, 10)).thenReturn(3);
        when(generadorMock.entre(1, 100)).thenReturn(80);

        espirituAngelical.setNivelDeConexion(10);
        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(5, espirituAngelical.getNivelDeConexion());
    }


}
