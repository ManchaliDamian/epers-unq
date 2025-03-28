package ar.edu.unq.epersgeist.persistencia.dao.jdbc;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBCEspirituDAOTest {

    private JDBCEspirituDAO espirituDAO;

    @BeforeEach
    void setUp() throws SQLException {
        JDBCConnector.getInstance().execute(conn -> {
            try {
                return conn.prepareStatement("CREATE TABLE espiritu").executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        espirituDAO = new JDBCEspirituDAO();
    }

    @AfterEach
    void tearDown() throws SQLException {
        JDBCConnector.getInstance().execute(conn -> {
            try {
                return conn.prepareStatement("DROP TABLE espiritu").executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void crearEspiritu() {
        Espiritu espiritu = new Espiritu("Fantasma", 5, "Pablo Fidel");
        Espiritu creado = espirituDAO.crear(espiritu);

        assertNotNull(creado.getId());
        assertEquals("Fantasma", creado.getTipo());
        assertEquals(1L, creado.getId());
    }

    @Test
    void recuperarEspiritu() {
        Espiritu espiritu = new Espiritu("Espectro", 5, "Warmi");
        Espiritu creado = espirituDAO.crear(espiritu);

        Espiritu recuperado = espirituDAO.recuperar(creado.getId());

        assertNotNull(recuperado);
        assertEquals(creado.getId(), recuperado.getId());
        assertEquals("Warmi", recuperado.getNombre());
    }

    @Test
    void recuperarTodos() {
        espirituDAO.crear(new Espiritu("Oni", 5, "Selene"));
        espirituDAO.crear(new Espiritu("Poltergeist", 8, "Belen"));

        List<Espiritu> espiritus = espirituDAO.recuperarTodos();

        assertEquals(2, espiritus.size());
    }

    @Test
    void actualizarEspiritu() {
        Espiritu espiritu = espirituDAO.crear(new Espiritu("Jantu", 5, "Fidel Viejo"));

        espirituDAO.actualizar(new Espiritu("Jantu", 5, "Fidel Nuevo"));

        Espiritu actualizado = espirituDAO.recuperar(espiritu.getId());
        assertEquals("Fidel Nuevo", actualizado.getNombre());
    }

    @Test
    void eliminarEspiritu() {
        Espiritu espiritu = espirituDAO.crear(new Espiritu("Fantasma", 5, "Espiritu a Eliminar"));

        espirituDAO.eliminar(espiritu.getId());

        assertNull(espirituDAO.recuperar(espiritu.getId()));
    }
}
