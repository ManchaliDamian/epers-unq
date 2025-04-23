package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;


import java.util.List;

/*
public class HibernateUbicacionDAO extends HibernateDAO<Ubicacion> implements UbicacionDAO {
    public HibernateUbicacionDAO() {
        super(Ubicacion.class);
    }



    @Override
    public List<Ubicacion> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();

        String hql = "FROM Ubicacion";
        List<Ubicacion> lista = session.createQuery(hql, Ubicacion.class).getResultList();

        return lista;
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        Session session = HibernateTransactionRunner.getCurrentSession();

        String hql = "FROM Espiritu e WHERE e.ubicacion.id = :idUbicacion";
        List<Espiritu> espiritus = session.createQuery(hql, Espiritu.class)
                .setParameter("idUbicacion", ubicacionId)
                .getResultList();
        return espiritus;
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        Session session = HibernateTransactionRunner.getCurrentSession();

        String hql = "FROM Medium m WHERE m.ubicacion.id = :idUbicacion and size(m.espiritus) = 0";
        List<Medium> mediums = session.createQuery(hql, Medium.class)
                .setParameter("idUbicacion", ubicacionId)
                .getResultList();
        return mediums;
    }
}
*/