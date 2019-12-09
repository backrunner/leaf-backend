package top.backrunner.leaf.app.dao;

import top.backrunner.leaf.app.entity.VersionInfo;
import top.backrunner.leaf.core.dao.BaseDao;

public interface VersionDao extends BaseDao<VersionInfo> {
    public boolean exists(Long appId, String platform, String version);
    public VersionInfo getVersion(Long appId, String platform, String version);
    public long getCount(Long appId);
    public long getCountByUser(Long userId);
}
