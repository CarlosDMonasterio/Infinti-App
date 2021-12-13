package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.FileStorage;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FileStorage")
public class FileStorageModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "file_storage_test_id")
    @SequenceGenerator(name = "file_storage_test_id", sequenceName = "file_storage_test_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel account;

    @Column(name = "identifier", unique = true)
    private String identifier;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "data", nullable = false, length = 100000)
    private byte[] data;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }

    @Override
    public FileStorage toDataObject() {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setId(this.id);
        fileStorage.setIdentifier(this.identifier);
        return fileStorage;
    }
}
