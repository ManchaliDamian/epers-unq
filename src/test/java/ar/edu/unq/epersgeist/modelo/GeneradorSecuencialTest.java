package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.generador.GeneradorSecuencial;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class GeneradorSecuencialTest {

    private GeneradorSecuencial generadorSecuencial;
    @BeforeEach
    void setUp() {
        generadorSecuencial = new GeneradorSecuencial(40,35,20,50);
    }

    @Test
    void entre_DevuelveElementosEnOrden_YDevuelveMinimoSiQuedaSinElementos() {
        assertEquals(40,generadorSecuencial.entre(98152,14241232));
        assertEquals(35,generadorSecuencial.entre(98152,14241232));
        assertEquals(20,generadorSecuencial.entre(98152,14241232));
        assertEquals(50,generadorSecuencial.entre(98152,14241232));
        assertEquals(98152,generadorSecuencial.entre(98152,14241232));
    }

    @Test
    void entre_DevuelveElMinimoSiEstaVacioDesdeElPrincipio(){
        GeneradorSecuencial g2 = new GeneradorSecuencial();
        assertEquals(5, g2.entre(5,8));
    }

}
