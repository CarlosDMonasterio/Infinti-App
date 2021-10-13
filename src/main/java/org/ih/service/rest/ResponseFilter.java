package org.ih.service.rest;

import org.ih.dao.hibernate.HibernateConfiguration;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Rolls back the transaction if http status is 500
 * otherwise commits transaction if started
 *
 * @author Hector Plahar
 */
@Provider
public class ResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (responseContext.getStatus() == 500) {
            HibernateConfiguration.rollbackTransaction();
        } else {
            HibernateConfiguration.commitTransaction();
        }
    }
}
