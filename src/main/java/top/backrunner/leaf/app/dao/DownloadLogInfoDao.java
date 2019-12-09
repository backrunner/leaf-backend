package top.backrunner.leaf.app.dao;

import top.backrunner.leaf.app.entity.DownloadLogInfo;
import top.backrunner.leaf.core.dao.BaseDao;

public interface DownloadLogInfoDao extends BaseDao<DownloadLogInfo> {
    public boolean create(Long appId, Long versionId, String ip);
}
