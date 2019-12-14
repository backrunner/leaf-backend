package top.backrunner.leaf.app.dao;

import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.core.dao.BaseDao;

import java.util.List;

public interface ApplicationDao extends BaseDao<ApplicationInfo> {
    public List<ApplicationInfo> getList(Long userId);
    public boolean bundleIdExists(String bundleId);
    public long getCountByUser(Long userId);
    public boolean deleteById(Long id);
    public long getCount();
}