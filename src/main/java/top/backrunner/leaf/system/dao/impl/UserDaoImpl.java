package top.backrunner.leaf.system.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.leaf.core.dao.impl.BaseDaoImpl;
import top.backrunner.leaf.system.dao.UserDao;
import top.backrunner.leaf.system.entity.UserInfo;
import top.backrunner.leaf.utils.filter.SQLFilter;

@Repository
public class UserDaoImpl extends BaseDaoImpl<UserInfo> implements UserDao {

    @Override
    public UserInfo findByUserName(String username) {
        return this.getByHql("FROM UserInfo WHERE username = '" + SQLFilter.filter(username) +"'");
    }

    @Override
    public UserInfo findById(Long uid) {
        return this.getById(UserInfo.class, uid);
    }

    @Override
    public boolean usernameExists(String username) throws Exception {
        long res = this.countByHql("SELECT COUNT(*) FROM UserInfo WHERE username = '" + SQLFilter.filter(username) + "'");
        if (res < 0){
            throw new Exception("用户名检查失败");
        }
        return res != 0;
    }

    @Override
    public boolean userExists(Long uid) throws Exception {
        long res = this.countByHql("SELECT COUNT(*) FROM UserInfo WHERE id = " + uid);
        if (res < 0){
            throw new Exception("检查失败");
        }
        return res != 0;
    }

}