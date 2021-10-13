package org.ih.dao.hibernate;

import org.ih.dao.model.AuditModel;

/**
 * Data Accessor Object for audits
 *
 * @author Hector Plahar
 */
public class AuditDAO extends HibernateRepository<AuditModel> {

    @Override
    public AuditModel get(long id) {
        return retrieve(AuditModel.class, id);
    }
}
