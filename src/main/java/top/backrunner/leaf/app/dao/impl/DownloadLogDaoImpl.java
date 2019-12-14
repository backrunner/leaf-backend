package top.backrunner.leaf.app.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.leaf.app.dao.DownloadLogDao;
import top.backrunner.leaf.app.entity.DownloadLogInfo;
import top.backrunner.leaf.core.dao.impl.BaseDaoImpl;
import top.backrunner.leaf.utils.misc.GeoIPUtils;

import java.util.Date;

@Repository
public class DownloadLogDaoImpl extends BaseDaoImpl<DownloadLogInfo> implements DownloadLogDao {

    @Override
    public boolean create(Long appId, Long versionId, String ip) {
        DownloadLogInfo log = new DownloadLogInfo();
        log.setCreateTime(new Date());
        log.setAppId(appId);
        log.setVersionId(versionId);
        log.setIpAddress(ip);
        log.setGeo(GeoIPUtils.o.getIPLocation(ip).toString());
        return this.add(log);
    }

    @Override
    public boolean deleteByVersion(Long versionId) {
        return this.removeByHql("delete from DownloadLogInfo where versionId = " + versionId);
    }

    @Override
    public boolean deleteByApp(Long appId) {
        return this.removeByHql("delete from DownloadLogInfo where appId = " + appId);
    }
}
