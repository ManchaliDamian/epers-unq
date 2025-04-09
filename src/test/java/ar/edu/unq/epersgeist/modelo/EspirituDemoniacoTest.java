package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EspirituDemoniacoTest {

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
    void espirituDemoniacoRecibeAtaque(){
        espirituAngelical.atacar(espirituDemoniaco);
        assertEquals(10,espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoSeDesconectaDelMediumPorFaltaDeNivelConexion(){
        espirituDemoniaco.setNivelDeConexion(0);
        espirituAngelical.atacar(espirituDemoniaco);
        assertNull(espirituDemoniaco.getMediumConectado());
        assertEquals(0,espirituDemoniaco.getNivelDeConexion());
        assertEquals(0,mediumConectado.getEspiritus().size());
    }

}
