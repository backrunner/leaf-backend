package top.backrunner.leaf.app.dao;

import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.core.dao.BaseDao;

public interface ApplicationDao extends BaseDao<ApplicationInfo> {
    public boolean bundleIdExists(String bundleId);
    public long getCountByUser(Long userId);
}