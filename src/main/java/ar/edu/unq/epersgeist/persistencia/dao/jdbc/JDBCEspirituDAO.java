package ar.edu.unq.epersgeist.persistencia.dao.jdbc;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public record JDBCEspirituDAO() implements EspirituDAO {

    public Espiritu crear(Espiritu espiritu) {
        return JDBCConnector.getInstance().execute( conn -> {
            try {
                var ps = conn.prepareStatement("INSERT INTO espiritu (tipo, nivel_de_conexion, nombre) VALUES (?,?,?)");
                ps.setString(1, espiritu.getTipo());
                ps.setInt(2, espiritu.getNivelDeConexion());
                ps.setString(3, espiritu.getNombre());

                ResultSet rs = ps.executeQuery();
                Long id = rs.getLong("id");
                return new Espiritu(id, espiritu.getTipo(), espiritu.getNivelDeConexion(), espiritu.getNombre());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Espiritu recuperar(Long idDelEspiritu) {
        return JDBCConnector.getInstance().execute(conn -> {
            try {
                var ps = conn.prepareStatement("SELECT * FROM espiritu WHERE id = ?");
                ps.setLong(1, idDelEspiritu);
                var resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    return new Espiritu(
                            resultSet.getLong("id"),
                            resultSet.getString("tipo"),
                            resultSet.getInt("nivel_de_conexion"),
                            resultSet.getString("nombre")
                    );
                } return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public List<Espiritu> recuperarTodos() {
        return JDBCConnector.getInstance().execute(conn -> {
            try {
                var ps = conn.prepareStatement("SELECT * FROM espiritu ORDER BY nombre ASC");
                var resultSet = ps.executeQuery();

                List<Espiritu> espiritus = new ArrayList<Espiritu>();

                while (resultSet.next()) {
                    espiritus.add(new Espiritu(
                            resultSet.getLong("id"),
                            resultSet.getString("tipo"),
                            resultSet.getInt("nivel_de_conexion"),
                            resultSet.getString("nombre")
                    ));
                } return espiritus;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void actualizar(Espiritu espiritu) {
        JDBCConnector.getInstance().execute(conn -> {
            try {
                var ps = conn.prepareStatement("UPDATE espiritu SET tipo = ?, nivel_de_conexion = ?, nombre = ?  WHERE id = ?");
                ps.setString(1, espiritu.getTipo());
                ps.setInt(2, espiritu.getNivelDeConexion());
                ps.setString(3, espiritu.getNombre());
                ps.setLong(4, espiritu.getId());
                return ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void eliminar(Long idDelEspiritu) {
        JDBCConnector.getInstance().execute(conn -> {
            try {
                var ps = conn.prepareStatement("DELETE FROM espiritu WHERE id =  ? ");
                ps.setLong(1, idDelEspiritu);
                return ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public JDBCEspirituDAO() {
        try {
            var uri = getClass().getClassLoader().getResource("createAll.sql").toURI();
            var initializeScript = Files.readString(Paths.get(uri));
            JDBCConnector.getInstance().execute(conn -> {
                try {
                    var ps = conn.prepareStatement(initializeScript);
                    return ps.execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}