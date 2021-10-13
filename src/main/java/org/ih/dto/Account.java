package org.ih.dto;

/**
 * DTO for accounts
 *
 * @author Hector Plahar
 */
public class Account implements DataObject {

    private long id;
    private String firstName;
    private String lastName;
    private String newPassword;
    private String password;
    private String email;
    private long creationTime;
    private long lastUpdateTime;
    private long lastLoginTime;
    private long currentTime;
    private String sessionId;
    private boolean allowedToChangePassword;
    private boolean usingTemporaryPassword;
    private boolean disabled;
    private String description;
    private boolean isAdministrator;
    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isAllowedToChangePassword() {
        return allowedToChangePassword;
    }

    public void setAllowedToChangePassword(boolean allowedToChangePassword) {
        this.allowedToChangePassword = allowedToChangePassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isUsingTemporaryPassword() {
        return usingTemporaryPassword;
    }

    public void setUsingTemporaryPassword(boolean usingTemporaryPassword) {
        this.usingTemporaryPassword = usingTemporaryPassword;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
