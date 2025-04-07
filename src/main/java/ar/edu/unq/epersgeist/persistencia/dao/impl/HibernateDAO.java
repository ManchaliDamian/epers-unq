package ar.edu.unq.epersgeist.persistencia.dao.impl;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;

public class HibernateDAO<T> {

    private final Class<T> entityType;

    public HibernateDAO(Class<T> entityType) {
        this.entityType = entityType;
    }

    public void guardar(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.save(entity);
    }

    public T recuperar(Long id) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        return session.get(entityType, id);
    }

    public void eliminar(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.remove(entity);
    }
    public void eliminarTodo() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.createQuery("delete from " + entityType.getSimpleName()).executeUpdate();
    }

    public void actualizar(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.update(entity);
    }


}
