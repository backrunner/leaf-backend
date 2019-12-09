package top.backrunner.leaf.utils.security;

import org.apache.shiro.SecurityUtils;
import top.backrunner.leaf.system.entity.UserInfo;

public class AuthUtils {
    public static UserInfo getUser(){
        return (UserInfo) SecurityUtils.getSubject().getPrincipal();
    }
    public static Long getUserId(){
        return ((UserInfo) SecurityUtils.getSubject().getPrincipal()).getId();
    }
    public static String getCurrentUsername(){
        return ((UserInfo) SecurityUtils.getSubject().getPrincipal()).getUsername();
    }
}
