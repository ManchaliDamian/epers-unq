package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCEspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private final JDBCEspirituDAO jDBCEspirituDAO;

    public EspirituServiceImpl(JDBCEspirituDAO jDBCEspirituDAO) {
        this.jDBCEspirituDAO = jDBCEspirituDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return jDBCEspirituDAO.crear(espiritu);
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return jDBCEspirituDAO.recuperar(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return jDBCEspirituDAO.recuperarTodos();
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        jDBCEspirituDAO.actualizar(espiritu);
    }

    @Override
    public void eliminar(Long espirituId) {
        jDBCEspirituDAO.eliminar(espirituId);
    }

    @Override
    public Medium conectar(Long espirituId, Medium medium) {
        Espiritu currentEspiritu = jDBCEspirituDAO.recuperar(espirituId);
        medium.conectarseAEspiritu(currentEspiritu);
        currentEspiritu.aumentarConexion(medium);

        jDBCEspirituDAO.actualizar(currentEspiritu);

        return medium;
    }

}
