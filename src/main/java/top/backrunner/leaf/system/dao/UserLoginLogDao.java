package top.backrunner.leaf.system.dao;

import top.backrunner.leaf.core.dao.BaseDao;
import top.backrunner.leaf.system.entity.log.UserLoginLogInfo;

import java.util.List;

public interface UserLoginLogDao extends BaseDao<UserLoginLogInfo> {
    public boolean create(Long uid, String ip);
    public List<UserLoginLogInfo> getList(Long uid);
    public List<UserLoginLogInfo> getLimitedList(Long uid, int limited);
}
