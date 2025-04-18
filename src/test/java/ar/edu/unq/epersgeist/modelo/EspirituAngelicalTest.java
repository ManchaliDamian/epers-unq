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

    @BeforeEach
    void setUp(){
        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");

        espirituAngelical = new EspirituAngelical("EspirituAngelical", quilmes);
        espirituDemoniaco = new EspirituDemoniaco( "EspirituDemoniaco", bernal);
        mediumConectado = new Medium("Mago",100,50,quilmes);
        espirituDemoniaco.setMediumConectado(mediumConectado);
        espirituAngelical.setMediumConectado(mediumConectado);
    }

    @Test
    void espirituAngelicalAtacaConExitoAlDemoniaco() {
        espirituDemoniaco.setNivelDeConexion(20);
        espirituAngelical.setNivelDeConexion(20);
        Generador.setEstrategia(new GeneradorSecuencial(10));

        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(10, espirituDemoniaco.getNivelDeConexion());

    }

    @Test
    void espirituAngelicalFallaAlAtacarAlDemoniaco() {
        espirituAngelical.setNivelDeConexion(10);
        espirituDemoniaco.setNivelDeConexion(20);

        Generador.setEstrategia(new GeneradorSecuencial(100));
        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(5, espirituAngelical.getNivelDeConexion());
        assertEquals(20, espirituDemoniaco.getNivelDeConexion());
    }


}
