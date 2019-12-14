package top.backrunner.leaf.app.dao;

import top.backrunner.leaf.app.entity.VersionInfo;
import top.backrunner.leaf.core.dao.BaseDao;

import java.util.List;

public interface VersionDao extends BaseDao<VersionInfo> {
    public List<VersionInfo> getList(Long appId);
    public List<VersionInfo> getList(Long appId, String platform);
    public List<VersionInfo> getList(Long appId, int page, int pageSize);
    public boolean exists(Long appId, String platform, String version);
    public VersionInfo getVersion(Long appId, String platform, String version);
    public long getCount(Long appId);
    public long getCountByUser(Long userId);
    public boolean deleteById(Long id);
    public boolean deleteByAppId(Long appId);
}
