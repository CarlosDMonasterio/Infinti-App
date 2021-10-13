package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.Account;
import org.ih.dto.Audit;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Hector Plahar
 */
@Entity
@Table(name = "AUDIT")
public class AuditModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "audit_id")
    @SequenceGenerator(name = "audit_id", sequenceName = "audit_id_seq", allocationSize = 1)
    private long id;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date creationTime;

    @OneToOne
    @JoinColumn(name = "account_id")
    private AccountModel accountModel;

    @Column(name = "comment")
    private String comment;

    public AuditModel() {
    }

    public long getId() {
        return id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public AccountModel getAccount() {
        return accountModel;
    }

    public void setAccount(AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Audit toDataObject() {
        Audit audit = new Audit();
        audit.setId(getId());
        audit.setCreationTime(getCreationTime().getTime());

        if (accountModel != null) {
            Account account = new Account();
            account.setEmail(accountModel.getEmail());
            account.setFirstName(accountModel.getFirstName());
            account.setLastName(accountModel.getLastName());
            audit.setUser(account);
        }

        // comment
        audit.setComment(getComment());
        return audit;
    }
}
