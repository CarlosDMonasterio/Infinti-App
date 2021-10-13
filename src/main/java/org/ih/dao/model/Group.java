package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.GroupTransfer;
import org.ih.group.GroupType;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Object for Groups
 *
 * @author Hector Plahar
 */
@Entity
@Table(name = "\"group\"")
public class Group implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "group_id")
    @SequenceGenerator(name = "group_id", sequenceName = "group_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "label", length = 124, nullable = false)
    private String label;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private GroupType type;

    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Column(name = "modification_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationTime;

    @ManyToOne
    private AccountModel owner;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<AccountModel> members = new HashSet<>();

    public Group() {
    }

    public long getId() {
        return this.id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public Date getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Set<AccountModel> getMembers() {
        return members;
    }

    public AccountModel getOwner() {
        return owner;
    }

    public void setOwner(AccountModel owner) {
        this.owner = owner;
    }

    public GroupTransfer toDataObject() {
        GroupTransfer transfer = new GroupTransfer();
        transfer.setId(id);
        transfer.setDisplay(label);
        transfer.setDescription(this.description);
        if (this.creationTime != null)
            transfer.setCreated(this.creationTime.getTime());
        return transfer;
    }
}
