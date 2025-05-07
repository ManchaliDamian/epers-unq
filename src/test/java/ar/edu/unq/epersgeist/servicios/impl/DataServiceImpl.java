package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataServiceImpl implements DataService {
    private final UbicacionDAO ubicacionDAO;
    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;

    public DataServiceImpl(UbicacionDAO ubicacionDAO, MediumDAO mediumDAO, EspirituDAO espirituDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.mediumDAO = mediumDAO;
        this.espirituDAO = espirituDAO;
    }

    public void eliminarTodo() {
        espirituDAO.deleteAll();
        mediumDAO.deleteAll();
        ubicacionDAO.deleteAll();
    }
}
