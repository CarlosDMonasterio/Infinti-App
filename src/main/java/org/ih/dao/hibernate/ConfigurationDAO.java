package org.ih.dao.hibernate;

import org.ih.dao.model.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Hector Plahar
 */
public class ConfigurationDAO extends HibernateRepository<Configuration> {

    public Configuration get(long id) {
        return super.retrieve(Configuration.class, id);
    }

    /**
     * Retrieves configuration by key which are unique
     *
     * @param key unique display name identifier
     * @return Configuration with key specified in the parameter or null if not found
     */
    public Configuration get(String key) {
        CriteriaBuilder cb = getBuilder();
        CriteriaQuery<Configuration> criteria = cb.createQuery(Configuration.class);
        Root<Configuration> root = criteria.from(Configuration.class);
        criteria.select(root).where(cb.equal(root.get("key"), key));
        return currentSession().createQuery(criteria).uniqueResult();
    }

    public List<Configuration> getAll() {
        CriteriaBuilder cb = getBuilder();
        CriteriaQuery<Configuration> criteria = cb.createQuery(Configuration.class);
        Root<Configuration> root = criteria.from(Configuration.class);
        criteria.select(root);
        return currentSession().createQuery(criteria).getResultList();
    }
}
