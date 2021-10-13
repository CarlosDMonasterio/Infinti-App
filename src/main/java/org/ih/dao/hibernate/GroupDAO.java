package org.ih.dao.hibernate;

import org.hibernate.HibernateException;
import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.Group;
import org.ih.group.GroupType;
import org.ih.util.StringUtil;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Data accessor objects for groups
 *
 * @author Hector Plahar
 */
public class GroupDAO extends HibernateRepository<Group> {

    public Group get(long id) {
        return super.retrieve(Group.class, id);
    }

    public boolean isGroupMember(Group group, AccountModel accountModel) {
        try {
            CriteriaQuery<Long> query = getBuilder().createQuery(Long.class);
            Root<Group> from = query.from(Group.class);
            Join<Group, AccountModel> member = from.join("members");
            query.select(getBuilder().countDistinct(from.get("id")));
            query.where(getBuilder().and(
                    getBuilder().equal(from.get("id"), group.getId()),
                    getBuilder().equal(member.get("email"), accountModel.getEmail())));
            return currentSession().createQuery(query).uniqueResult() != 0;
        } catch (HibernateException he) {
            Logger.error(he.getMessage());
            throw new DataAccessException(he);
        }
    }

    public long getMemberCount(long groupId) {
        try {
            CriteriaQuery<Long> query = getBuilder().createQuery(Long.class);
            Root<Group> from = query.from(Group.class);
            Join<Group, AccountModel> member = from.join("members");
            query.select(getBuilder().countDistinct(member.get("id")));
            query.where(getBuilder().equal(from.get("id"), groupId));
            return currentSession().createQuery(query).uniqueResult();
        } catch (Exception e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public List<Group> availableGroups(GroupType type, AccountModel owner, int offset, int limit, String filter) {
        try {
            CriteriaQuery<Group> query = getBuilder().createQuery(Group.class).distinct(true);
            Root<Group> groupRoot = query.from(Group.class);
            Predicate predicate = createPredicate(groupRoot, type, owner, filter);
            query.select(groupRoot).where(predicate);
            query.orderBy(getBuilder().desc(groupRoot.get("id")));
            return currentSession().createQuery(query).setFirstResult(offset).setMaxResults(limit).list();
        } catch (HibernateException he) {
            Logger.error(he.getMessage());
            throw new DataAccessException(he);
        }
    }

    public long availableCount(GroupType type, AccountModel owner, String filter) {
        try {
            CriteriaBuilder cb = getBuilder();
            CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
            Root<Group> groupRoot = criteria.from(Group.class);
            Predicate predicate = createPredicate(groupRoot, type, owner, filter);
            criteria.select(cb.countDistinct(groupRoot)).where(predicate);
            return currentSession().createQuery(criteria).uniqueResult();
        } catch (HibernateException he) {
            Logger.error(he.getMessage());
            throw new DataAccessException(he);
        }
    }

    private Predicate createPredicate(Root<Group> groupRoot, GroupType type, AccountModel owner, String filter) {
        CriteriaBuilder cb = getBuilder();
        Join<Group, AccountModel> accountJoin = groupRoot.join("members", JoinType.LEFT);

        if (StringUtil.isEmpty(filter)) {
            return cb.and(
                    cb.equal(groupRoot.get("type"), type),
                    cb.or(
                            cb.equal(groupRoot.get("owner"), owner),
                            cb.equal(accountJoin.get("email"), owner.getEmail())
                    ));
        } else {
            return cb.and(
                    cb.equal(groupRoot.get("type"), type),
                    cb.or(
                            cb.equal(groupRoot.get("owner"), owner),
                            cb.equal(accountJoin.get("email"), owner.getEmail())
                    ),
                    cb.like(getBuilder().lower(groupRoot.get("label")), "%" + filter.toLowerCase() + "%")
            );
        }
    }
}
