package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;


public class HibernateUbicacionDAO extends HibernateDAO<Ubicacion> implements UbicacionDAO {
    public HibernateUbicacionDAO() {
        super(Ubicacion.class);
    }


    @Override
    public void actualizar(Long ubicacionId, String nombreNuevo) {
        Session session = HibernateTransactionRunner.getCurrentSession();

        String hql = "UPDATE Ubicacion u SET u.nombre = :nombreNuevo WHERE u.id = :ubicacionId";
        Query query = session.createQuery(hql);

        query.setParameter("nombreNuevo", nombreNuevo);
        query.setParameter("ubicacionId", ubicacionId);

        query.executeUpdate();
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();

        String hql = "FROM Ubicacion";
        List<Ubicacion> lista = session.createQuery(hql, Ubicacion.class).getResultList();

        return lista;
    }
}
