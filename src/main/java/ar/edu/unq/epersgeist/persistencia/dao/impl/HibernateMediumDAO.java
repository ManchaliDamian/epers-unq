package ar.edu.unq.epersgeist.persistencia.dao.impl;

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
    public Medium crear(Medium unMedium) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.persist(unMedium);
        return unMedium;
    }

    @Override
    public List<Medium> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "SELECT m FROM Medium m";
        Query<Medium> query = session.createQuery(hql, Medium.class);
        return query.getResultList();
    }

    @Override
    public void actualizar(Medium unMedium) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "UPDATE Medium m SET m.nombre = :nombre, m.mana = :mana, m.manaMax = :manaMax WHERE m.id = :id";
        Query<?> query = session.createQuery(hql);
        query.setParameter("nombre", unMedium.getNombre());
        query.setParameter("mana", unMedium.getMana());
        query.setParameter("manaMax", unMedium.getManaMax());
        query.setParameter("id", unMedium.getId());
        query.executeUpdate();
    }

    @Override
    public void eliminar(Long mediumId) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "DELETE FROM Medium m WHERE m.id = :mediumId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("mediumId", mediumId);
        query.executeUpdate();
    }
}
