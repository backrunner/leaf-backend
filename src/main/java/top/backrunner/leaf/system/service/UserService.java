package top.backrunner.leaf.system.service;

import top.backrunner.leaf.system.entity.RoleInfo;
import top.backrunner.leaf.system.entity.UserInfo;
import top.backrunner.leaf.system.entity.log.UserLoginLogInfo;
import top.backrunner.leaf.system.exception.CannotBanAdminException;
import top.backrunner.leaf.system.exception.UserNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

public interface UserService {
    // 用户
    public boolean addUser(UserInfo user);
    public UserInfo findUserByUsername(String username);
    public UserInfo findUserById(Long uid);
    public boolean usernameExists(String username);
    public boolean userExists(Long uid);
    public boolean updateUser(UserInfo user);
    public boolean deleteUser(Long id);
    // 权限
    public RoleInfo findRoleByName(String name);
    public RoleInfo findRoleById(Long id);
    public RoleInfo initRole(String name);
    public boolean roleExists(String name);
    // 管理员管理
    public List<Map<String, Object>> getUserList(int page, int pageSize);
    public long getUserCount();
    public boolean banUser(Long id) throws UserNotFoundException, CannotBanAdminException;
    public boolean unbanUser(Long id) throws UserNotFoundException;
    public boolean setRoleToUser(Long id) throws UserNotFoundException;
    public boolean setRoleToAdmin(Long id) throws UserNotFoundException;
    // 日志
    public void createLoginLog(Long uid, String ip);
    public List<UserLoginLogInfo> getLatestLoginLogs(Long uid, int count);
}
