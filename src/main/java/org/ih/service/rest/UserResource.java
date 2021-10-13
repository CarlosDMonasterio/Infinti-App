package org.ih.service.rest;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.ih.account.AccountAuthorization;
import org.ih.account.AccountRole;
import org.ih.account.Accounts;
import org.ih.account.Users;
import org.ih.common.logging.Logger;
import org.ih.dto.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

/**
 * REST user resource
 *
 * @author Hector Plahar
 */
@Path("/users")
public class UserResource extends RestResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@DefaultValue("false") @QueryParam("notify") boolean notifyUser, Account account) {
        String userId = getUserId();
        Logger.info("Creating new account for " + account.getEmail());
        try {
            Accounts accounts = new Accounts();
            account = accounts.createAccount(userId, account, notifyUser);
        } catch (IllegalArgumentException e) {
            Logger.error(e);
            return super.respond(Response.Status.CONFLICT, "User id in use");
        }
        return super.respond(account);
    }

    /**
     * Retrieves (up to specified limit), the list of users that match the value
     *
     * @param val   text to match against users
     * @param limit upper limit for number of users to return
     * @return list of matching users
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/autocomplete")
    public Response getAutoCompleteForAvailableAccounts(
            @QueryParam("val") String val,
            @DefaultValue("8") @QueryParam("limit") int limit) {
        String userId = getUserId();
        Users users = new Users(userId);
        return super.respond(users.filter(val, limit));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getList(@DefaultValue("15") @QueryParam("limit") int limit,
                            @DefaultValue("0") @QueryParam("start") int start,
                            @DefaultValue("false") @QueryParam("asc") boolean asc,
                            @DefaultValue("id") @QueryParam("sort") String sort,
                            @QueryParam("filterText") String filter) {
        String userId = getUserId();
        new AccountAuthorization().expectAdmin(userId);
        Logger.info(userId + ": retrieving all accounts");
        Accounts accounts = new Accounts();
        return super.respond(accounts.retrieve(userId, start, limit, asc, sort, filter));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("roles/{type}")
    public Response getAccountsByRole(@PathParam("type") AccountRole role) {
        String userId = getUserIdFromSessionHeader(sessionId);
        Logger.info(userId + ": retrieving accounts by role " + role);
        Accounts accounts = new Accounts();
        List<Account> result = accounts.getAccountsByRole(userId, role);
        return respond(result);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        String userId = getUserIdFromSessionHeader(sessionId);
        Logger.info(userId + ": retrieving information for user '" + id + "'");
        Accounts accounts = new Accounts();
        Account result = accounts.get(userId, id);
        return super.respond(result);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{uid}/password")
    public Response resetPassword(@PathParam("uid") String userId, Account transfer) {
        Logger.info("Resetting account password for " + userId);
        Accounts accounts = new Accounts();
        Account result = accounts.resetPassword(userId, transfer);
        return super.respond(result);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response update(@PathParam("id") long id,
                           Account transfer) {
        String userId = getUserIdFromSessionHeader(sessionId);
        Logger.info(userId + ": updating account " + id);
        Accounts accounts = new Accounts();
        boolean success = accounts.update(userId, id, transfer);
        return super.respond(success);
    }

    @POST
    @Path("{id}/roles")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRole(@PathParam("id") long id, AccountRole role) {
        String userId = getUserIdFromSessionHeader(sessionId);
        Logger.info(userId + ": adding role " + role + " to account " + id);
        Accounts accounts = new Accounts(userId);
        boolean success = accounts.addRole(id, role);
        return respond(success);
    }

    @DELETE
    @Path("{id}/roles/{role}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeRole(@PathParam("id") long id,
                               @PathParam("role") AccountRole role) {
        String userId = getUserIdFromSessionHeader(sessionId);
        Logger.info(userId + ": removing role " + role + " from account " + id);
        Accounts accounts = new Accounts(userId);
        boolean success = accounts.removeRole(id, role);
        return respond(success);
    }

    @PUT
    @Path("{id}/active")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addToActiveUsers(@PathParam("id") long id) {
        String userId = getUserId();
        Accounts accounts = new Accounts(userId);
        return super.respond(accounts.setDisabled(id, false));
    }

    @DELETE
    @Path("{id}/active")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeFromActiveUsers(@PathParam("id") long id) {
        String userId = getUserId();
        Accounts accounts = new Accounts(userId);
        return super.respond(accounts.setDisabled(id, true));
    }

    @POST
    @Path("import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importUsers(@DefaultValue("false") @QueryParam("notify") boolean notify,
                                @FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition contentDisposition) {
        String userId = getUserId();
        Users users = new Users(userId);
        users.importFile(fileInputStream, notify);
        return super.respond(true);
    }
}
