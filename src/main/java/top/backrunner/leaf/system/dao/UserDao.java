package top.backrunner.leaf.system.dao;

import top.backrunner.leaf.core.dao.BaseDao;
import top.backrunner.leaf.system.entity.UserInfo;

import java.util.List;

public interface UserDao extends BaseDao<UserInfo> {
    // 根据用户名找用户信息
    public UserInfo findByUserName(String username);
    // 根据用户ID查找用户信息
    public UserInfo findById(Long uid);
    // 用户名是否存在
    public boolean usernameExists(String username) throws Exception;
    // 用户是否存在
    public boolean userExists(Long uid) throws Exception;
}
