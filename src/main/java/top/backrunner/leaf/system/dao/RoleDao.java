package top.backrunner.leaf.system.dao;

import top.backrunner.leaf.core.dao.BaseDao;
import top.backrunner.leaf.system.entity.RoleInfo;

public interface RoleDao extends BaseDao<RoleInfo> {
    public RoleInfo findById(Long id);
    public RoleInfo findByName(String name);
    public boolean exists(String name);
}