package top.backrunner.leaf.system.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.leaf.core.dao.impl.BaseDaoImpl;
import top.backrunner.leaf.system.dao.UserLoginLogDao;
import top.backrunner.leaf.system.entity.log.UserLoginLogInfo;
import top.backrunner.leaf.utils.misc.GeoIPUtils;

import java.util.Date;
import java.util.List;

@Repository
public class UserLoginLogDaoImpl extends BaseDaoImpl<UserLoginLogInfo> implements UserLoginLogDao {
    @Override
    public boolean create(Long uid, String ip) {
        UserLoginLogInfo log = new UserLoginLogInfo();
        log.setCreateTime(new Date());
        log.setUid(uid);
        log.setIp(ip);
        log.setGeo(GeoIPUtils.o.getIPLocation(ip).toString());
        return this.add(log);
    }

    @Override
    public List<UserLoginLogInfo> getList(Long uid){
        return this.findByHql("FROM UserLoginLogInfo WHERE uid = "+uid);
    }

    @Override
    public List<UserLoginLogInfo> getLimitedList(Long uid, int limited) {
        Session session = this.getHibernateSession();
        Query<UserLoginLogInfo> query = session.createQuery("FROM UserLoginLogInfo WHERE uid = "+uid+"order by createTime desc");
        query.setMaxResults(limited);
        return query.list();
    }

}