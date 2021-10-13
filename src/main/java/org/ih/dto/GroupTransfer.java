package org.ih.dto;

import org.ih.account.AccountRole;
import org.ih.group.GroupType;

import java.util.List;

/**
 * @author Hector Plahar
 */
public class GroupTransfer implements DataObject {

    private long id;
    private String display;
    private String description;
    private long created;
    private Account owner;
    private GroupType type;
    private long memberCount;
    private List<Account> members;
    private List<AccountRole> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getCreated() {
        return this.created;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(long memberCount) {
        this.memberCount = memberCount;
    }

    public List<Account> getMembers() {
        return members;
    }
}
