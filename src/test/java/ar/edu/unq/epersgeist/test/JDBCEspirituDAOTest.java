package ar.edu.unq.epersgeist.test;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCEspirituDAO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class JDBCEspirituDAOTest {
    private final EspirituDAO dao = new JDBCEspirituDAO();
    private Espiritu espiritu;

    
    @BeforeEach
    void crearModelo() {
        espiritu = new Espiritu(
                "agua",
                3,
                "marc"
        );
    }

    @Test
    void alCrearYLuegoRecuperarSeObtieneObjetosSimilares() {
        Espiritu es = dao.crear(espiritu);

        var otroEspiritu = dao.recuperar(es.getId());
        System.out.println("Espiritu creado: " + espiritu);

        assertEquals(espiritu.getNombre(), otroEspiritu.getNombre());
        assertEquals(espiritu.getTipo(), otroEspiritu.getTipo());
        assertEquals(espiritu.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());

        Assertions.assertNotEquals(espiritu, otroEspiritu);
    }

//    @AfterEach
//    void eliminarModelo() {
//        dao.eliminar(espiritu.getId());
//    }
}