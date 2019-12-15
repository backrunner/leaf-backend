package top.backrunner.leaf.app.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.leaf.app.dao.DownloadKeyDao;
import top.backrunner.leaf.app.entity.DownloadKeyInfo;
import top.backrunner.leaf.core.dao.impl.BaseDaoImpl;

@Repository
public class DownloadKeyDaoImpl extends BaseDaoImpl<DownloadKeyInfo> implements DownloadKeyDao {
    @Override
    public boolean exists(String key) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("SELECT COUNT(*) FROM DownloadKeyInfo WHERE linkKey = :key");
        query.setParameter("key", key);
        query.setMaxResults(1);
        return ((Long)query.uniqueResult()) > 0;
    }

    @Override
    public DownloadKeyInfo get(String key) {
        Session session = this.getHibernateSession();
        Query<DownloadKeyInfo> query = session.createQuery("FROM DownloadKeyInfo WHERE linkKey = :key");
        query.setParameter("key", key);
        query.setMaxResults(1);
        return query.uniqueResult();
    }
}
