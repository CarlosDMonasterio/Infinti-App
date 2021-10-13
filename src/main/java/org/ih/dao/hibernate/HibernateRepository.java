package org.ih.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.dao.DatabaseModel;
import org.ih.dao.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Hibernate Persistence repository implementation
 *
 * @author Hector Plahar
 */

public abstract class HibernateRepository<T extends DatabaseModel> implements Repository<T> {

    /**
     * Obtain the current hibernate {@link Session}.
     *
     * @return {@link Session}
     */
    protected static Session currentSession() {
        return HibernateConfiguration.currentSession();
    }

    protected static CriteriaBuilder getBuilder() {
        return currentSession().getCriteriaBuilder();
    }

    /**
     * Deletes an {@link DatabaseModel} from the database.
     *
     * @param object model to delete
     * @throws DataAccessException on Hibernate Exception or invalid parameter
     */
    @Override
    public void delete(T object) {
        try {
            currentSession().delete(object);
        } catch (HibernateException e) {
            throw new DataAccessException("dbDelete failed!", e);
        }
    }

    /**
     * Updates an existing object in the database
     *
     * @param object Object to update
     * @return updated object
     * @throws DataAccessException on Hibernate Exception or invalid parameter
     */
    public T update(T object) {
        try {
            currentSession().update(object);
        } catch (HibernateException e) {
            throw new DataAccessException("dbUpdate failed!", e);
        }
        return object;
    }

    /**
     * Creates new object in the database
     *
     * @param model {@link DatabaseModel} object to create
     * @return Object created {@link DatabaseModel} object
     * @throws DataAccessException in the event of a problem saving or invalid parameter
     */
    public T create(T model) {
        try {
            currentSession().save(model);
        } catch (HibernateException e) {
            throw new DataAccessException("Database create failed", e);
        }

        return model;
    }

    /**
     * Retrieve an {@link DatabaseModel} from the database by Class and database id.
     *
     * @param clazz class type for {@link DatabaseModel}
     * @param id    unique synthetic identifier for {@link DatabaseModel}
     * @return {@link DatabaseModel} obtained from the database.
     * @throws DataAccessException on Hibernate Exception
     */
    protected T retrieve(Class<T> clazz, long id) {
        try {
            return currentSession().get(clazz, id);
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException("Could not retrieve " + clazz.getSimpleName() + " with id \"" + id + "\"", e);
        }
    }

    protected List<T> list(Class<T> clazz, String sort, boolean asc, int start, int limit) {
        try {
            CriteriaQuery<T> query = getBuilder().createQuery(clazz);
            Root<T> from = query.from(clazz);
            query.orderBy(asc ? getBuilder().asc(from.get(sort)) : getBuilder().desc(from.get(sort)));
            return currentSession().createQuery(query).setMaxResults(limit).setFirstResult(start).list();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    protected long listCount(Class<T> clazz) {
        try {
            CriteriaQuery<Long> query = getBuilder().createQuery(Long.class);
            Root<T> from = query.from(clazz);
            query.select(getBuilder().countDistinct(from.get("id")));
            return currentSession().createQuery(query).uniqueResult();
        } catch (Exception e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    protected CriteriaQuery<T> getQuery(Class<T> tClass) {
        return getBuilder().createQuery(tClass);
    }
}
