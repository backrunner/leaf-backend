package top.backrunner.leaf.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
}
