package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateMediumDAO extends HibernateDAO<Medium> implements MediumDAO {

    public HibernateMediumDAO() {
        super(Medium.class);
    }
    @Override
    public Medium crear(Medium medium) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.persist(medium);
        return medium;
    }

    @Override
    public List<Medium> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "SELECT m FROM Medium m";
        Query<Medium> query = session.createQuery(hql, Medium.class);
        return query.getResultList();
    }

    @Override
    public void eliminar(Long mediumId) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "DELETE FROM Medium m WHERE m.id = :mediumId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("mediumId", mediumId);
        query.executeUpdate();
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "SELECT e FROM Espiritu e WHERE e.mediumConectado.id = :id";
        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
        query.setParameter("id", mediumId);
        return query.getResultList();
    }

    @Override
    public Medium recuperar(Long mediumId){
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "SELECT m FROM Medium m WHERE m.id = :id";
        Query<Medium> query = session.createQuery(hql, Medium.class);
        query.setParameter("id", mediumId);
        return query.getSingleResult();
    }
}
