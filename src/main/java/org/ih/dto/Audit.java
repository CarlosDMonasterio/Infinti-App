package org.ih.dto;

/**
 * Data transfer object for user activity.
 *
 * @author Hector Plahar
 */
public class Audit implements DataObject {

    private long id;
    private Account user;  // user carrying out action. null if a system action
    private long creationTime;
    private String comment;

    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
