package org.ih.dao.model;

import org.ih.account.AccountRole;
import org.ih.dao.DatabaseModel;
import org.ih.dto.Account;

import javax.persistence.*;
import java.util.*;

/**
 * @author Hector Plahar
 */
@Entity
@Table(name = "ACCOUNT")
public class AccountModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "account_id")
    @SequenceGenerator(name = "account_id", sequenceName = "account_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", length = 320, nullable = false, unique = true)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "hashed_password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @ElementCollection(targetClass = AccountRole.class)
    @Enumerated(value = EnumType.STRING)
    private Set<AccountRole> roles = new HashSet<>();

    @Column(name = "description")
    private String description;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date creationTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date lastUpdateTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    private Date lastLoginTime;

    @Column(name = "is_temp_password")
    private Boolean usingTempPassword;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_group", joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private final Set<GroupModel> groups = new LinkedHashSet<>();

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "password_updated")
    private Date passwordUpdatedTime;

    public AccountModel() {
    }

    public long getId() {
        return this.id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Set<AccountRole> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<AccountRole> roles) {
        this.roles = roles;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<GroupModel> getGroups() {
        return groups;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getUsingTempPassword() {
        return usingTempPassword;
    }

    public void setUsingTempPassword(Boolean usingTempPassword) {
        this.usingTempPassword = usingTempPassword;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public Date getPasswordUpdatedTime() {
        return passwordUpdatedTime;
    }

    public void setPasswordUpdatedTime(Date passwordUpdatedTime) {
        this.passwordUpdatedTime = passwordUpdatedTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return email;
    }

    public Account toDataObject() {
        Account transfer = new Account();
        transfer.setId(id);
        if (this.roles.contains(AccountRole.ADMINISTRATOR))
            transfer.setAdministrator(true);

        transfer.setFirstName(firstName);
        transfer.setLastName(lastName);
        transfer.setEmail(email);
        if (lastUpdateTime != null)
            transfer.setLastUpdateTime(lastUpdateTime.getTime());
        if (creationTime != null)
            transfer.setCreationTime(creationTime.getTime());
        if (lastLoginTime != null)
            transfer.setLastLoginTime(lastLoginTime.getTime());
        transfer.setCurrentTime(System.currentTimeMillis());
        transfer.setDescription(description);
        transfer.setPhone(phone);
        transfer.setDisabled(Objects.requireNonNullElse(disabled, false));
        return transfer;
    }
}
