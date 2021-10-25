package org.ih.dao.hibernate;

import org.hibernate.HibernateException;
import org.ih.account.AccountRole;
import org.ih.account.Accounts;
import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.GroupModel;
import org.ih.util.StringUtil;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Data Accessor object for managing {@link AccountModel} objects
 *
 * @author Hector Plahar
 */
public class AccountDAO extends HibernateRepository<AccountModel> {

    /**
     * Retrieves account using a user id
     *
     * @param email unique user id
     * @return account obtaining using the user id, or null if not found
     * @throws IllegalArgumentException if the referenced user id is null
     * @ on error processing the resulting database query
     */
    public AccountModel getByEmail(String email) {
        try {
            if (email == null)
                throw new IllegalArgumentException("Cannot retrieve account with null user id");

            CriteriaQuery<AccountModel> criteriaQuery = getBuilder().createQuery(AccountModel.class);
            Root<AccountModel> from = criteriaQuery.from(AccountModel.class);
            criteriaQuery.select(from).where(getBuilder().equal(getBuilder().lower(from.get("email")), email.toLowerCase()));
            return currentSession().createQuery(criteriaQuery).uniqueResult();
        } catch (HibernateException he) {
            throw new DataAccessException(he);
        }
    }

    public List<AccountModel> getByRole(AccountRole role) {
        CriteriaQuery<AccountModel> criteriaQuery = getBuilder().createQuery(AccountModel.class);
        Root<AccountModel> root = criteriaQuery.from(AccountModel.class);
        criteriaQuery.where(root.join("roles").in(role));
        return currentSession().createQuery(criteriaQuery).getResultList();
    }

    public List<AccountModel> retrieveAccounts(int start, int limit, boolean asc, String property, String filter) {
        CriteriaBuilder cb = getBuilder();
        CriteriaQuery<AccountModel> criteriaQuery = getBuilder().createQuery(AccountModel.class);
        Root<AccountModel> root = criteriaQuery.from(AccountModel.class);
        setWhereQuery(root, criteriaQuery, filter);
        criteriaQuery.orderBy(asc ? cb.asc(root.get(property)) : cb.desc(root.get(property)));
        return currentSession().createQuery(criteriaQuery).setFirstResult(start).setMaxResults(limit).getResultList();
    }

    public long getAccountTotal(String filter) {
        CriteriaQuery<Long> criteriaQuery = getBuilder().createQuery(Long.class);
        Root<AccountModel> root = criteriaQuery.from(AccountModel.class);
        criteriaQuery.select(getBuilder().countDistinct(root));
        setWhereQuery(root, criteriaQuery, filter);
        return currentSession().createQuery(criteriaQuery).uniqueResult();
    }

    private void setWhereQuery(Root<AccountModel> root, CriteriaQuery<?> criteriaQuery, String filter) {
        Predicate p1 = getBuilder().notEqual(root.get("email"), Accounts.DEFAULT_ADMIN_USERID.toLowerCase());
        if (!StringUtil.isEmpty(filter)) {
            filter = filter.toUpperCase();
            Predicate or1 = getBuilder().like(getBuilder().upper(root.get("firstName")), "%" + filter + "%");
            Predicate or2 = getBuilder().like(getBuilder().upper(root.get("lastName")), "%" + filter + "%");
            criteriaQuery.where(getBuilder().and(p1, getBuilder().or(or1, or2)));
        } else {
            criteriaQuery.where(getBuilder().and(p1));
        }
    }

    /**
     * Retrieves accounts whose firstName, lastName, or email fields match the specified
     * token up to the specified limit.
     *
     * @param token filter for the account fields
     * @param limit maximum number of matching accounts to return
     * @return list of matching accounts
     */
    public List<AccountModel> getMatchingAccounts(String token, int limit) {
        try {
            CriteriaQuery<AccountModel> query = getBuilder().createQuery(AccountModel.class);
            Root<AccountModel> from = query.from(AccountModel.class);
            String[] tokens = token.split("\\s+");
            createQuery(from, query, null, tokens);
            return currentSession().createQuery(query).setMaxResults(limit).list();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public List<AccountModel> getGroupMembers(GroupModel group) {
        try {
            CriteriaQuery<AccountModel> query = getBuilder().createQuery(AccountModel.class);
            Root<AccountModel> from = query.from(AccountModel.class);
            Join<AccountModel, GroupModel> groups = from.join("groups");
            query.where(getBuilder().equal(groups, group));
            return currentSession().createQuery(query).list();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public List<AccountModel> getMatchingGroupMembers(Set<GroupModel> matchingGroups, String token, int limit) {
        try {
            CriteriaQuery<AccountModel> query = getBuilder().createQuery(AccountModel.class);
            Root<AccountModel> from = query.from(AccountModel.class);
            Join<AccountModel, GroupModel> groups = from.join("groups");

            String[] tokens = token.split("\\s+");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(groups.in(matchingGroups));
            createQuery(from, query, predicates, tokens);
            return currentSession().createQuery(query).setMaxResults(limit).list();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    private void createQuery(Root<AccountModel> from, CriteriaQuery<AccountModel> query, List<Predicate> predicates, String[] tokens) {
        if (predicates == null)
            predicates = new ArrayList<>();

        for (String tok : tokens) {
            tok = tok.toLowerCase();
            predicates.add(
                    getBuilder().or(
                            getBuilder().like(getBuilder().lower(from.get("firstName")), "%" + tok + "%"),
                            getBuilder().like(getBuilder().lower(from.get("lastName")), "%" + tok + "%"),
                            getBuilder().like(getBuilder().lower(from.get("email")), "%" + tok + "%"))
            );
        }
        query.where(predicates.toArray(new Predicate[0])).distinct(true);
    }

    // remove
    public void removeGroup(GroupModel group, AccountModel accountModel) {
        accountModel.getGroups().remove(group);
        update(accountModel);
    }

    @Override
    public AccountModel get(long id) {
        return super.retrieve(AccountModel.class, id);
    }
}
