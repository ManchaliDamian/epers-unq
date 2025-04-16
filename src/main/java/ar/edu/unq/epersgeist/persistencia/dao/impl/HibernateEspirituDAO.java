package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.*;
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
    public List<Espiritu> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from Espiritu";

        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);

        return query.getResultList();
    }

    @Override
    public List<Espiritu> recuperarDemoniacosPaginados(Direccion direccion, int pagina, int cantidadPorPagina){
        Session session = HibernateTransactionRunner.getCurrentSession();
        String orden = direccion == Direccion.ASCENDENTE ? "asc" : "desc";
        String hql = "from Espiritu e where e.tipo = :tipo order by e.nivelDeConexion " + orden;

        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
        query.setParameter("tipo", TipoEspiritu.DEMONIACO);
        query.setFirstResult(pagina * cantidadPorPagina);
        query.setMaxResults(cantidadPorPagina);

        return query.getResultList();
    }

    @Override
    public List<EspirituAngelical> recuperarAngelesDe(Long mediumId){
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from EspirituAngelical e where e.mediumConectado.id = :mediumId";
        Query<EspirituAngelical> query = session.createQuery(hql, EspirituAngelical.class);
        query.setParameter("mediumId", mediumId);
        return query.getResultList();
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId){
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from EspirituDemoniaco e where e.mediumConectado.id = :mediumId";
        Query<EspirituDemoniaco> query = session.createQuery(hql, EspirituDemoniaco.class);
        query.setParameter("mediumId", mediumId);
        return query.getResultList();
    }
}
