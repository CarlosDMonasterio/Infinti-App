package org.ih.service.rest;

import org.ih.dao.hibernate.HibernateConfiguration;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 * Request filter which begins transaction for request. This is the main filter
 * for all requests from the servlet container. {@see IceResponseFilter} for response filter which also
 * contains transaction close/commit
 *
 * @author Hector Plahar
 */
@Provider
public class RequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        HibernateConfiguration.beginTransaction();
    }
}
