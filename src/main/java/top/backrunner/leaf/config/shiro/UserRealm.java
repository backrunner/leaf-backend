package top.backrunner.leaf.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Lazy;
import top.backrunner.leaf.system.entity.RoleInfo;
import top.backrunner.leaf.system.entity.UserInfo;
import top.backrunner.leaf.system.service.UserService;

import javax.annotation.Resource;

public class UserRealm extends AuthorizingRealm {

    @Resource
    @Lazy
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取session中的用户
        UserInfo user = (UserInfo) principalCollection.getPrimaryPrincipal();
        // 定义返回的info
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 查询数据库
        user = userService.findUserByUsername(user.getUsername());
        RoleInfo role = userService.findRoleById(user.getRoleId());
        info.addRole(role.getName());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 从token获取用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        // 查询数据库
        UserInfo user = userService.findUserByUsername(username);
        if (user != null){
            ByteSource salt = ByteSource.Util.bytes(user.getSalt());
            return new SimpleAuthenticationInfo(user, user.getPassword(), salt, this.getName());
        } else {
            return null;
        }
    }
}