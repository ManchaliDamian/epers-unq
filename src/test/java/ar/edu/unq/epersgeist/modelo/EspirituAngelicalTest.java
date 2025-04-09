package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        espirituAngelical = new EspirituAngelical(30,"EspirituAngelical",quilmes);
        espirituDemoniaco = new EspirituDemoniaco(25,"EspirituDemoniaco",bernal);
        mediumConectado = new Medium("Mago",100,50,quilmes);
        espirituDemoniaco.setMediumConectado(mediumConectado);
        espirituAngelical.setMediumConectado(mediumConectado);
    }

    @Test
    void espirituAngelicarAtacaConExitoAlDemoniaco(){
        espirituAngelical.atacar(espirituDemoniaco);
        assertEquals(10,espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void AngelicalFallaAlAtacarAlDemoniaco(){
        espirituAngelical.setNivelDeConexion(10);
        espirituAngelical.atacar(espirituDemoniaco);
        assertEquals(5,espirituAngelical.getNivelDeConexion());
    }

}
