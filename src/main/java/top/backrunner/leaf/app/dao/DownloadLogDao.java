package top.backrunner.leaf.app.dao;

import top.backrunner.leaf.app.entity.DownloadLogInfo;
import top.backrunner.leaf.core.dao.BaseDao;

public interface DownloadLogDao extends BaseDao<DownloadLogInfo> {
    public boolean create(Long appId, Long versionId, String ip);
    public boolean deleteByVersion(Long versionId);
    public boolean deleteByApp(Long appId);
    public long getRecentWeekCount(Long appId);
    public long getRecentMonthCount(Long appId);
}
