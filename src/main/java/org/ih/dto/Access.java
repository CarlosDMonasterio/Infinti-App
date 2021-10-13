package org.ih.dto;

/**
 * Data transfer object for access permissions
 * Covers both designs and permissions
 *
 * @author Hector Plahar
 */
public class Access implements DataObject {

    private long id;
    private Account account;
    private GroupTransfer group;
    private boolean canWrite;
    private Article article;
    private long articleId;
    private boolean isPrincipalInvestigator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public GroupTransfer getGroup() {
        return group;
    }

    public void setGroup(GroupTransfer group) {
        this.group = group;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public boolean isPrincipalInvestigator() {
        return isPrincipalInvestigator;
    }

    public void setPrincipalInvestigator(boolean principalInvestigator) {
        isPrincipalInvestigator = principalInvestigator;
    }

    public enum Article {
        DESIGN, PROJECT
    }
}
