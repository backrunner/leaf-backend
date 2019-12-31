package top.backrunner.leaf.system.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.leaf.app.service.ApplicationService;
import top.backrunner.leaf.system.entity.RoleInfo;
import top.backrunner.leaf.system.entity.UserInfo;
import top.backrunner.leaf.system.service.UserService;
import top.backrunner.leaf.utils.common.R;
import top.backrunner.leaf.utils.security.AuthUtils;

import javax.annotation.Resource;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private ApplicationService applicationService;

    private final String emailRegex = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
    private final String phoneRegex = "^1[3456789]\\d{9}$";

    @RequestMapping(value = "/fetchUserInfo")
    @ResponseBody
    public R fetchUserInfo(){
        UserInfo info = AuthUtils.getUser();
        HashMap<String, Object> res = new HashMap<>();
        res.put("id", info.getId());
        res.put("username", info.getUsername());
        res.put("email", info.getEmail());
        res.put("phone", info.getPhone());
        RoleInfo role = userService.findRoleById(info.getRoleId());
        res.put("role", role.getName());
        return R.ok(res);
    }

    // 列出登录记录（前5条）
    @RequestMapping(value = "/getLoginLog")
    @ResponseBody
    public R getLoginLog(){
        UserInfo info = AuthUtils.getUser();
        return R.ok(userService.getLatestLoginLogs(info.getId(), 5));
    }

    @RequestMapping(value = "/modInfo")
    @ResponseBody
    public R modInfo(String newEmail, String newPhone){
        if (!ObjectUtils.allNotNull(newEmail, newPhone)){
            return R.badRequest("提交的参数不完整");
        }
        if (!newEmail.matches(emailRegex)){
            return R.badRequest("请填写正确的邮箱地址");
        }
        if (!newPhone.matches(phoneRegex)){
            return R.badRequest("请填写正确的手机号码");
        }
        UserInfo info = AuthUtils.getUser();
        info.setEmail(newEmail);
        info.setPhone(newPhone);
        userService.updateUser(info);
        return R.ok("保存成功");
    }

    @RequestMapping(value = "/modPassword")
    @ResponseBody
    public R modPassword(String oldPassword, String newPassword, String newConfirmPassword){
        if (!ObjectUtils.allNotNull(oldPassword, newPassword, newConfirmPassword)){
            return R.badRequest("提交的参数不完整");
        }
        if (newPassword.length() < 6){
            return R.error("密码不得少于6个字符");
        }
        // 两次输入的密码进行核对
        if (!newPassword.equals(newConfirmPassword)){
            return R.error("两次输入的密码不一致");
        }
        // 校验用户身份和旧密码的正确性
        UserInfo info = AuthUtils.getUser();
        String hashedOldPassword = new SimpleHash("SHA-256", oldPassword, info.getSalt(), 32).toHex();
        if (!info.getPassword().equals(hashedOldPassword)){
            return R.error("旧密码不正确");
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        String hashedNewPassword = new SimpleHash("SHA-256", newPassword, salt, 32).toHex();
        info.setSalt(salt);
        info.setPassword(hashedNewPassword);
        if (userService.updateUser(info)){
            return R.ok("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @RequestMapping(value = "/cancelAccount")
    @ResponseBody
    public R cancelAccount(String password){
        if (!ObjectUtils.allNotNull(password)){
            return R.badRequest("提交的参数不完整");
        }
        UserInfo user = AuthUtils.getUser();
        String hashPassword = new SimpleHash("SHA-256", password, user.getSalt(), 32).toHex();
        if (hashPassword.equals(user.getPassword())){
            // 注销帐号
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            if (!applicationService.deleteApplicationByUser(user.getId())) {
                return R.error("删除关联数据失败");
            }
            if (userService.deleteUser(user.getId())){
                return R.ok("注销成功");
            } else {
                return R.error("注销失败");
            }
        } else {
            return R.unauth("密码不正确");
        }
    }
}
