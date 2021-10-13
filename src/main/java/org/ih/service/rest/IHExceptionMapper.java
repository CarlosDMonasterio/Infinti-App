package org.ih.service.rest;

import org.ih.common.access.AuthorizationException;
import org.ih.common.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Mapper for mapping exceptions to REST {@link Response}s
 *
 * @author Hector Plahar
 */
@Provider
public class IHExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Response response;
        if (exception instanceof WebApplicationException webEx) {
            response = webEx.getResponse();
        } else if (exception instanceof AuthorizationException) {
            response = Response.status(Response.Status.FORBIDDEN.getStatusCode(), exception.getMessage()).build();
        } else {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getMessage()).build();
        }

        final int status = response.getStatus();
        String info = "HTTP status " + status + " thrown";

        if (400 <= status && status < 500) {
            Logger.warn(info);
        } else {
            Logger.error(info, exception);
        }

        return response;
    }
}
