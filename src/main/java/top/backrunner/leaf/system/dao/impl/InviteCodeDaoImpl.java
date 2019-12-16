package top.backrunner.leaf.system.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.leaf.core.dao.impl.BaseDaoImpl;
import top.backrunner.leaf.system.dao.InviteCodeDao;
import top.backrunner.leaf.system.entity.InviteCodeInfo;

@Repository
public class InviteCodeDaoImpl extends BaseDaoImpl<InviteCodeInfo> implements InviteCodeDao {
    @Override
    public InviteCodeInfo get(String code) {
        Session session = this.getHibernateSession();
        Query<InviteCodeInfo> query = session.createQuery("from InviteCodeInfo where code = :code");
        query.setParameter("code", code);
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    @Override
    public boolean checkExists(String code) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("from InviteCodeInfo where code = :code");
        query.setParameter("code", code);
        query.setMaxResults(1);
        return (Long) query.uniqueResult() > 0;
    }

    @Override
    public boolean checkUsed(String code) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("from InviteCodeInfo where code = :code and used = true");
        query.setParameter("code", code);
        query.setMaxResults(1);
        return (Long) query.uniqueResult() > 0;
    }
}
