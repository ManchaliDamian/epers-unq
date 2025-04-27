package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class EspirituAngelicalTest {
    private Espiritu espirituDemoniaco;
    private Espiritu espirituAngelical;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Medium mediumConectado;

    @BeforeEach
    void setUp(){
        santuario = new Santuario("Quilmes", 70);
        cementerio = new Cementerio("Bernal",60);

        espirituAngelical = new EspirituAngelical("EspirituAngelical", santuario);
        espirituDemoniaco = new EspirituDemoniaco( "EspirituDemoniaco", cementerio);
        mediumConectado = new Medium("Mago",100,50,santuario);

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

        Generador.setEstrategia(new GeneradorSecuencial(5,100));
        espirituAngelical.atacar(espirituDemoniaco);

        assertEquals(5, espirituAngelical.getNivelDeConexion());
        assertEquals(20, espirituDemoniaco.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeSantuario_AumentaNivelDeConexion() {
        Santuario santuario = new Santuario("Test", 30);
        espirituAngelical.setNivelDeConexion(50);

        espirituAngelical.recibirEfectoDe(santuario);

        assertEquals(80, espirituAngelical.getNivelDeConexion());
    }

    @Test
    void recibirEfectoDeSantuario_NoExcedeMaximo() {
        Santuario santuario = new Santuario("Test", 60);
        espirituAngelical.setNivelDeConexion(50);

        espirituAngelical.recibirEfectoDe(santuario);

        assertEquals(100, espirituAngelical.getNivelDeConexion());
    }

    @Test
    void puedeSerInvocadoEnSantuario_RetornaTrue() {
        assertTrue(espirituAngelical.puedeSerInvocadoEnSantuario());
    }

}
