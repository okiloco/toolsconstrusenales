/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.facade;

import java.util.List;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;

/**
 *
 * @param <T>
 */
@LocalBean
public abstract class AbstractFacade<T> {
    
    public final int ORGANISMO_BARRANQUILLA = 226;
    public final int ORGANISMO_CHINCHINA = 47;
    public final int ORGANISMO_SOLEDAD = 199;
    public final int ORGANISMO_ARJONA = 14;
    public final int ORGANISMO_DORADA = 106;
    public final int ORGANISMO_MAGDALENA = 11;
    public final int ORGANISMO_TURBACO = 208;
    public final int ORGANISMO_GALAPA = 81;
    public final int ORGANISMO_ITA = 173;
    public final int ORGANISMO_PUERTO = 159;
    public final int ORGANISMO_COROZAL = 57;
    
    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager(int organismo);

    public void create(int organismo,T entity) {
        getEntityManager(organismo).persist(entity);
    }

    public void edit(int organismo,T entity) {
        getEntityManager(organismo).merge(entity);
    }

    public void remove(int organismo,T entity) {
        getEntityManager(organismo).remove(getEntityManager(organismo).merge(entity));
    }

    public T find(int organismo,Object id) {
        return getEntityManager(organismo).find(entityClass, id);
    }

    public List<T> findAll(int organismo) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager(organismo).getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager(organismo).createQuery(cq).getResultList();
    }

    public List<T> findRange(int organismo,int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager(organismo).getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager(organismo).createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count(int organismo) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager(organismo).getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager(organismo).getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager(organismo).createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
