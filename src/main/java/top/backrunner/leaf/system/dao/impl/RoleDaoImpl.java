package top.backrunner.leaf.system.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.leaf.core.dao.impl.BaseDaoImpl;
import top.backrunner.leaf.system.dao.RoleDao;
import top.backrunner.leaf.system.entity.RoleInfo;
import top.backrunner.leaf.utils.filter.SQLFilter;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<RoleInfo> implements RoleDao {
    @Override
    public RoleInfo findById(Long id) {
        return this.getById(RoleInfo.class, id);
    }

    @Override
    public RoleInfo findByName(String name) {
        return this.getByHql("FROM RoleInfo WHERE name='" + SQLFilter.filter(name) + "'");
    }

    @Override
    public boolean exists(String name) {
        return this.countByHql("select count(*) from RoleInfo where name='" + SQLFilter.filter(name) + "'") > 0;
    }
}
