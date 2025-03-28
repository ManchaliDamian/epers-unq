package ar.edu.unq.epersgeist.persistencia.dao.jdbc;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBCEspirituDAOTest {

    private JDBCEspirituDAO espirituDAO;

    int getNivelDeEspirituEnBDD(Long unaId){
        return JDBCConnector.getInstance().execute(conn -> {
            try {
                var ps = conn.prepareStatement("SELECT * FROM espiritu WHERE id = ?");
                ps.setLong(1, unaId);
                var rs = ps.executeQuery();
                rs.next();
                return rs.getInt("nivel_de_conexion");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @BeforeEach
    void setUp() throws SQLException {
        JDBCConnector.getInstance().execute(conn -> {
            try {
                return conn.prepareStatement("CREATE TABLE espiritu(id SERIAL, tipo VARCHAR(128), nivel_de_conexion INT, nombre VARCHAR(128))").execute();
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
                return conn.prepareStatement("DROP TABLE espiritu").execute();
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
    void errorRecuperarEspiritu() {

        Espiritu recuperado = espirituDAO.recuperar(1L);

        assertNull(recuperado);
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
        Espiritu espiritu = espirituDAO.crear(new Espiritu("Jantu", 5, "Fidel"));
        Medium unMedium = new Medium("Roberto", 100, 20);

        //verificacion estado inicial
        assertEquals(5, espiritu.getNivelDeConexion());
        int lvlEnBDD = this.getNivelDeEspirituEnBDD(espiritu.getId());
        assertEquals(5, lvlEnBDD);

        //exercise
        espiritu.aumentarConexion(unMedium);
        espirituDAO.actualizar(espiritu);
        //

        //verificacion estado final
        assertEquals(15, espiritu.getNivelDeConexion());
        lvlEnBDD = this.getNivelDeEspirituEnBDD(espiritu.getId());
        assertEquals(15, lvlEnBDD);

        Espiritu actualizado = espirituDAO.recuperar(espiritu.getId());
        assertEquals(espiritu.getNombre(), actualizado.getNombre());
        assertEquals(espiritu.getTipo(), actualizado.getTipo());
        assertEquals(espiritu.getId(), actualizado.getId());
        assertEquals(espiritu.getNivelDeConexion(), actualizado.getNivelDeConexion());
        assertNotEquals(espiritu, actualizado);
    }

    @Test
    void eliminarEspiritu() {
        Espiritu espiritu = espirituDAO.crear(new Espiritu("Fantasma", 5, "Espiritu a Eliminar"));

        espirituDAO.eliminar(espiritu.getId());

        assertNull(espirituDAO.recuperar(espiritu.getId()));
    }
    @Test
    void errorEliminarEspiritu() {
        Espiritu espiritu = espirituDAO.crear(new Espiritu("Fantasma", 5, "Espiritu a Eliminar"));

        espirituDAO.eliminar(2L); //siguiente al id inicial

        assertNotNull(espirituDAO.recuperar(espiritu.getId()));
    }
}
