package org.ih.service.rest;

import org.ih.dto.Account;
import org.ih.dto.GroupTransfer;
import org.ih.group.GroupType;
import org.ih.group.Groups;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Rest endpoint for interacting with group resources
 *
 * @author Hector Plahar
 */
@Path("/groups")
public class GroupResource extends RestResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroups(
            @DefaultValue("PRIVATE") @QueryParam("type") String type,
            @QueryParam("offset") int offset,
            @DefaultValue("0") @QueryParam("start") int start,
            @DefaultValue("false") @QueryParam("asc") boolean asc,
            @DefaultValue("id") @QueryParam("sort") String sort,
            @QueryParam("filter") String filter,
            @QueryParam("limit") int limit) {
        String userId = requireUserId();
        Groups groups = new Groups(userId);
        GroupType groupType = GroupType.valueOf(type.toUpperCase());
        return super.respond(groups.get(groupType, offset, limit, filter));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/autocomplete")
    public Response matchGroupNames(@QueryParam("token") String token,
                                    @DefaultValue("8") @QueryParam("limit") int limit) {
        String userId = requireUserId();
        Groups groups = new Groups(userId);
        return super.respond(groups.getMatchingGroups(token, limit));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(GroupTransfer userGroup) {
        String userId = requireUserId();
        log(userId, "creating a new group");
        Groups groups = new Groups(userId);
        return super.respond(groups.add(userGroup));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/members")
    public Response addGroupMember(@PathParam("id") long groupId, Account account) {
        String userId = requireUserId();
        Groups groups = new Groups(userId);
        return super.respond(groups.addUserToGroup(groupId, account.getEmail()));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteUserGroup(@PathParam("id") long groupId) {
        String userIdStr = requireUserId();
        log(userIdStr, "deleting group " + groupId);
        Groups groups = new Groups(userIdStr);
        return super.respond(groups.remove(groupId));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/members/{mid}")
    public Response deleteGroupMember(@PathParam("id") long groupId,
                                      @PathParam("mid") long memberId) {
        String userId = requireUserId();
        Groups groups = new Groups(userId);
        return super.respond(groups.removeMemberFromGroup(groupId, memberId));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/members")
    public Response getGroupMembers(@PathParam("id") long id) {
        String userId = requireUserId();
        Groups groups = new Groups(userId);
        return super.respond(groups.getMembers(id));
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateGroup(@PathParam("id") long id, GroupTransfer group) {
        String userId = requireUserId();
        log(userId, "updating group " + group.getId());
        Groups groups = new Groups(userId);
        return respond(groups.update(id, group));
    }
}
