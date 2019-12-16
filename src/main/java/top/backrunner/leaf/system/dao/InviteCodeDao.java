package top.backrunner.leaf.system.dao;

import top.backrunner.leaf.core.dao.BaseDao;
import top.backrunner.leaf.system.entity.InviteCodeInfo;

public interface InviteCodeDao extends BaseDao<InviteCodeInfo> {
    public InviteCodeInfo get(String code);
    public boolean checkExists(String code);
    public boolean checkUsed(String code);
}
