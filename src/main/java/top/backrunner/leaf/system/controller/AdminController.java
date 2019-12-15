package top.backrunner.leaf.system.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.app.service.ApplicationService;
import top.backrunner.leaf.system.entity.InviteCodeInfo;
import top.backrunner.leaf.system.entity.UserInfo;
import top.backrunner.leaf.system.exception.CannotBanAdminException;
import top.backrunner.leaf.system.exception.UserNotFoundException;
import top.backrunner.leaf.system.service.InviteCodeService;
import top.backrunner.leaf.system.service.UserService;
import top.backrunner.leaf.utils.common.R;
import top.backrunner.leaf.utils.security.AuthUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    private UserService userService;
    @Resource
    private ApplicationService applicationService;
    @Resource
    private InviteCodeService inviteCodeService;

    // 邀请码

    @RequestMapping("/getInviteCodeList")
    @ResponseBody
    public R getInviteCodeList(int page, int pageSize){
        if (!ObjectUtils.allNotNull(page, pageSize)){
            return R.badRequest("提交的参数不完整");
        }
        List<InviteCodeInfo> list = inviteCodeService.getList(page, pageSize);
        if (list.isEmpty()) {
            return R.error("无查询结果");
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("total", inviteCodeService.getCount());
            res.put("list", list);
            return R.ok(res);
        }
    }

    @RequestMapping("/generateInviteCode")
    @ResponseBody
    public R generateInviteCode(int count, HttpServletRequest request){
        if (!ObjectUtils.allNotNull(count)) {
            return R.badRequest("提交的参数不完整");
        }
        if (count < 1) {
            return R.badRequest("数量错误");
        }
        String prefix = request.getParameter("prefix");
        if (prefix != null && prefix.trim().length() > 0) {
            inviteCodeService.generate(prefix, count);
        } else {
            inviteCodeService.generate(count);
        }
        return R.ok("生成完成");
    }

    @RequestMapping("/addInviteCode")
    @ResponseBody
    public R addInviteCode(String code){
        if (!ObjectUtils.allNotNull(code)) {
            return R.badRequest("提交的参数不完整");
        }
        if (inviteCodeService.codeExist(code.trim())){
            return R.error("该邀请码已存在");
        }
        if (inviteCodeService.addNew(code)){
            return R.ok("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @RequestMapping("/deleteInviteCode")
    @ResponseBody
    public R deleteInviteCode(long id){
        if (!ObjectUtils.allNotNull(id)){
            return R.badRequest("提交的参数不完整");
        }
        if (inviteCodeService.delete(id)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    // 应用

    @RequestMapping("/getAppList")
    @ResponseBody
    public R getAppList(int page, int pageSize){
        if (!ObjectUtils.allNotNull(page, pageSize)){
            return R.badRequest("提交的参数不完整");
        }
        List<ApplicationInfo> apps = applicationService.getApplicationList(page, pageSize);
        if (apps.isEmpty()) {
            return R.error("无查询结果");
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("total", applicationService.getApplicationCount());
            res.put("list", apps);
            return R.ok(res);
        }
    }

    @RequestMapping(value = "/banApp")
    @ResponseBody
    public R banApp(Long appId){
        if (!ObjectUtils.allNotNull(appId)) {
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchApplication(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (!app.isEnabled()){
            return R.error("该应用已经被禁用");
        }
        app.setEnabled(false);
        if (applicationService.updateApplication(app)){
            return R.ok("封禁成功");
        } else {
            return R.error("封禁失败");
        }
    }

    @RequestMapping(value = "/unbanApp")
    @ResponseBody
    public R unbanApp(Long appId){
        if (!ObjectUtils.allNotNull(appId)) {
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchApplication(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (app.isEnabled()){
            return R.error("该应用未被禁用");
        }
        app.setEnabled(true);
        if (applicationService.updateApplication(app)){
            return R.ok("解封成功");
        } else {
            return R.error("解封失败");
        }
    }

    @RequestMapping(value = "/deleteApp")
    @ResponseBody
    public R deleteApp(Long appId){
        if (!ObjectUtils.allNotNull(appId)) {
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchApplication(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (applicationService.deleteApplication(appId)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    // 用户

    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public R getUserList(int page, int pageSize){
        if (!ObjectUtils.allNotNull(page, pageSize)) {
            return R.badRequest("提交的参数不完整");
        }
        List<Map<String, Object>> res = userService.getUserList(page, pageSize);
        if (res.isEmpty()) {
            return R.error("无查询结果");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("total", userService.getUserCount());
            map.put("list", res);
            return R.ok(map);
        }
    }

    @RequestMapping(value = "/banUser")
    @ResponseBody
    public R banUser(Long uid){
        if (!ObjectUtils.allNotNull(uid)) {
            return R.badRequest("提交的参数不完整");
        }
        if (uid.equals(AuthUtils.getUserId())){
            return R.error("不能操作自己");
        }
        try {
            if (userService.banUser(uid)){
                return R.ok("封禁成功");
            } else {
                return R.error("封禁失败");
            }
        } catch (UserNotFoundException e) {
            return R.error("找不到该用户");
        } catch (CannotBanAdminException e) {
            return R.error("无法封禁管理员");
        }
    }

    @RequestMapping(value = "/unbanUser")
    @ResponseBody
    public R unbanUser(Long uid){
        if (!ObjectUtils.allNotNull(uid)) {
            return R.badRequest("提交的参数不完整");
        }
        if (uid.equals(AuthUtils.getUserId())){
            return R.error("不能操作自己");
        }
        try {
            if (userService.unbanUser(uid)){
                return R.ok("解封成功");
            } else {
                return R.error("解封失败");
            }
        } catch (UserNotFoundException e) {
            return R.error("找不到该用户");
        }
    }

    @RequestMapping(value = "/setUserRole")
    @ResponseBody
    public R setUserRole (String role, Long uid){
        if (!ObjectUtils.allNotNull(role, uid)) {
            return R.badRequest("提交的参数不完整");
        }
        if (uid.equals(AuthUtils.getUserId())){
            return R.error("不能操作自己");
        }
        if (!role.equals("admin") && !role.equals("user")){
            return R.badRequest("非法参数");
        }
        try {
            if ("admin".equals(role)) {
                if (userService.setRoleToAdmin(uid)) {
                    return R.ok("设置成功");
                } else {
                    return R.error("设置失败");
                }
            } else if ("user".equals(role)){
                if (userService.setRoleToUser(uid)) {
                    return R.ok("设置成功");
                } else {
                    return R.error("设置失败");
                }
            }
        } catch (UserNotFoundException e) {
            return R.error("找不到该用户");
        }
        return R.error();
    }

    @RequestMapping(value = "/deleteUser")
    @ResponseBody
    public R deleteUser(Long uid){
        if (!ObjectUtils.allNotNull(uid)) {
            return R.badRequest("提交的参数不完整");
        }
        if (uid.equals(AuthUtils.getUserId())){
            return R.error("不能操作自己");
        }
        UserInfo info = userService.findUserById(uid);
        if (info == null){
            return R.error("无此用户");
        }
        if (!applicationService.deleteApplicationByUser(info.getId())) {
            return R.error("删除关联数据失败");
        }
        if (userService.deleteUser(uid)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }
}
