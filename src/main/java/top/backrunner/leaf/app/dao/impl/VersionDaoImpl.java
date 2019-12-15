package top.backrunner.leaf.app.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.leaf.app.dao.VersionDao;
import top.backrunner.leaf.app.entity.VersionInfo;
import top.backrunner.leaf.core.dao.impl.BaseDaoImpl;

import java.util.List;

@Repository
public class VersionDaoImpl extends BaseDaoImpl<VersionInfo> implements VersionDao {

    @Override
    public List<VersionInfo> getList(Long appId) {
        Session session = this.getHibernateSession();
        Query<VersionInfo> query = session.createQuery("select count(*) from VersionInfo where appId = :appId");
        query.setParameter("appId", appId);
        return query.list();
    }

    @Override
    public List<VersionInfo> getList(Long appId, String platform) {
        Session session = this.getHibernateSession();
        Query<VersionInfo> query = session.createQuery("select count(*) from VersionInfo where appId = :appId and platform = :platform");
        query.setParameter("appId", appId);
        query.setParameter("platform", platform);
        return query.list();
    }

    @Override
    public List<VersionInfo> getList(Long appId, int page, int pageSize) {
        return this.showPage("FROM VersionInfo WHERE appId = "+appId + "order by createTime desc", page, pageSize);
    }

    @Override
    public boolean exists(Long appId, String platform, String version) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from VersionInfo where appId = :appId and platform = :platform and version = :version");
        query.setParameter("appId", appId);
        query.setParameter("platform", platform);
        query.setParameter("version", version);
        return (Long)query.uniqueResult() > 0;
    }

    @Override
    public VersionInfo getVersion(Long appId, String platform, String version) {
        Session session = this.getHibernateSession();
        Query<VersionInfo> query = session.createQuery("FROM VersionInfo WHERE appId=:appId and platform = :platform and version = :version");
        query.setParameter("appId", appId);
        query.setParameter("platform", platform);
        query.setParameter("version", version);
        return query.uniqueResult();
    }

    @Override
    public long getCount(Long appId) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from VersionInfo where appId = :appId");
        query.setParameter("appId", appId);
        return (Long)query.uniqueResult();
    }

    @Override
    public long getCountByUser(Long userId) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from VersionInfo v inner join ApplicationInfo a on v.appId = a.id where a.uid = :uid");
        query.setParameter("uid", userId);
        query.setMaxResults(1);
        return (Long)query.uniqueResult();
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeByHql("delete from VersionInfo where id = "+id);
    }

    @Override
    public boolean deleteByAppId(Long appId) {
        return this.removeByHql("delete from VersionInfo where appId = "+appId);
    }
}
