package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
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
    public void actualizar(Medium medium) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "UPDATE Medium m SET m.nombre = :nombre, m.mana = :mana, m.manaMax = :manaMax WHERE m.id = :id";
        Query<?> query = session.createQuery(hql);
        query.setParameter("nombre", medium.getNombre());
        query.setParameter("mana", medium.getMana());
        query.setParameter("manaMax", medium.getManaMax());
        query.setParameter("id", medium.getId());
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

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "SELECT e FROM Espiritu e WHERE e.medium.id = :id";
        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
        query.setParameter("id", mediumId);
        return query.getResultList();
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId){
        // Dado un médium y un espíritu, el médium deberá invocar al espíritu
        // a su ubicación generandole un costo de 10 puntos de mana.
        // Si el médium no tiene mana suficiente no hace nada.
        // Si el espíritu no esta libre, lanzar una excepción.
        Session session = HibernateTransactionRunner.getCurrentSession();

        Medium medium = session.get(Medium.class, mediumId);
        Espiritu espiritu = session.get(Espiritu.class, espirituId);

        if (medium.getMana() < 10) return espiritu;
        if (!espiritu.estaLibre()) throw new ExceptionEspirituOcupado(espiritu);

        espiritu.setUbicacion(medium.getUbicacion());
        medium.setMana(medium.getMana() - 10);

        session.persist(medium);
        session.persist(espiritu);

        return espiritu;
    }
}
