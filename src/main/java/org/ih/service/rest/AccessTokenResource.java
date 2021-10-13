package org.ih.service.rest;

import org.ih.account.Authenticator;
import org.ih.account.SessionHandler;
import org.ih.common.logging.Logger;
import org.ih.dto.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API for access tokens (also session id for the user interface)
 *
 * @author Hector Plahar
 */
@Path("/accesstoken")
public class AccessTokenResource extends RestResource {

    /**
     * Creates a new access token for the user referenced in the parameter, after
     * the credentials (username and password) are validated. If one already exists, it is
     * invalidated
     *
     * @param transfer wraps username and password
     * @return account information including a valid session id if credentials validate
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Account transfer) {
        if (transfer == null)
            return respond(Response.Status.BAD_REQUEST);

        try {
            Authenticator authenticator = new Authenticator();
            Account info = authenticator.authenticate(transfer.getEmail(), transfer.getPassword());
            if (info == null) {
                String errMsg = "Authentication failed for user '" + transfer.getEmail() + "'";
                Logger.warn(errMsg);
                return respond(Response.Status.UNAUTHORIZED, errMsg);
            }

            if (info.isDisabled()) {
                Logger.warn(transfer.getEmail() + " attempting to login but account disabled");
                return respond(Response.Status.FORBIDDEN);
            }

            Logger.info("User '" + transfer.getEmail() + "' successfully logged in");
            return respond(info);
        } catch (Exception e) {
            Logger.error(e);
            return respond(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Invalidates the specified session information. (equivalent to logging out of application)
     */
    @DELETE
    public Response deleteToken() {
        String userId = getUserIdFromSessionHeader(sessionId);
        return super.respond(SessionHandler.invalidateSession(userId));
    }

    /**
     * Checks if the passed session is valid
     *
     * @return status of "ok" if the session is valid, unauthorized otherwise
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        if (SessionHandler.isValidSession(sessionId))
            return Response.status(Response.Status.OK).build();
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
