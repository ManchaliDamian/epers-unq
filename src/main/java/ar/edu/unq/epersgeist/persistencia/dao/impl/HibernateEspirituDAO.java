package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.TipoEspiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateEspirituDAO extends HibernateDAO<Espiritu> implements EspirituDAO {

    public HibernateEspirituDAO() {
        super(Espiritu.class);
    }

    @Override
    public List<Espiritu> getEspiritusDemoniacos(){
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from Espiritu e where e.tipo = :tipo " +
                "order by e.nivelDeConexion desc";
        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
        query.setParameter("tipo", TipoEspiritu.DEMONIACO);
        return query.getResultList();
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "FROM Espiritu";
        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
        return query.getResultList();
    }

    @Override
    public List<Espiritu> recuperarPaginados(int page, int pageSize){
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from Espiritu";
       // String hql = "SELECT m FROM Espiritu LIMIT :pageSize OFFSET (:page - 1) * :pageSize";
        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
        query.setFirstResult(pageSize * (page - 1));
        query.setMaxResults(pageSize);
        //query.setParameter("pageSize", pageSize);
        //query.setParameter("page", page);
        return query.getResultList();
    }

}
