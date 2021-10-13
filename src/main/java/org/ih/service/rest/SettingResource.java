package org.ih.service.rest;

import org.ih.account.AccountAuthorization;
import org.ih.common.Results;
import org.ih.common.logging.Logger;
import org.ih.config.Settings;
import org.ih.dto.Setting;
import org.ih.util.StringUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST API for configuring the application
 *
 * @author Hector Plahar
 */
@Path("/settings")
public class SettingResource extends RestResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSettings() {
        String userId = getUserIdFromSessionHeader(sessionId);
        AccountAuthorization authorization = new AccountAuthorization();
        authorization.expectAdmin(userId);

        Logger.info(userId + ": retrieving system settings");
        Results<Setting> result = new Results<>();
        Settings settings = new Settings();
        result.getRequested().addAll(settings.getAll());
        return respond(result);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@Context UriInfo uriInfo, Setting setting) {
        String userId = getUserIdFromSessionHeader(sessionId);
        AccountAuthorization authorization = new AccountAuthorization();
        authorization.expectAdmin(userId); // todo : might have to filter out some settings

        if (StringUtil.isEmpty(setting.getKey()))
            throw new IllegalArgumentException("Cannot create setting with empty key");
        Logger.info(userId + ": updating setting " + setting.toString());
        Settings settings = new Settings();
        return super.respond(settings.update(setting));
    }
}
