package top.backrunner.leaf.app.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.leaf.app.dao.ApplicationDao;
import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.core.dao.impl.BaseDaoImpl;

import java.util.List;

@Repository
public class ApplicationDaoImpl extends BaseDaoImpl<ApplicationInfo> implements ApplicationDao {
    @Override
    public List<ApplicationInfo> getList(Long userId) {
        return this.findByHql("FROM ApplicationInfo WHERE uid = " + userId);
    }

    @Override
    public boolean bundleIdExists(String bundleId) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("SELECT COUNT(*) FROM ApplicationInfo WHERE bundleId = :bundleId");
        query.setParameter("bundleId", bundleId);
        query.setMaxResults(1);
        return ((Long)query.uniqueResult()) > 0;
    }

    @Override
    public long getCountByUser(Long userId) {
        return this.countByHql("select count(*) from ApplicationInfo WHERE uid = "+userId);
    }
}
