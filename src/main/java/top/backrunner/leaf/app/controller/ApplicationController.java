package top.backrunner.leaf.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.app.service.ApplicationService;
import top.backrunner.leaf.utils.common.R;
import top.backrunner.leaf.utils.misc.QiniuUtils;
import top.backrunner.leaf.utils.security.AuthUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/app")
public class ApplicationController {
    @Resource
    private ApplicationService applicationService;

    @RequestMapping(value = "/addApp")
    @ResponseBody
    public R addApp(String bundleId, String name, String desc, String iconKey){
        if (!ObjectUtils.allNotNull(bundleId, name, desc, iconKey)){
            return R.badRequest("提交的参数不完整");
        }
        // 检查 bundle id
        if (!bundleId.matches("^\\w+\\.\\w+.\\w+$")){
            return R.badRequest("Bundle ID 格式错误");
        }
        if (applicationService.bundleIdExists(bundleId)){
            return R.error("Bundle ID 已存在");
        }
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.setBundleId(bundleId);
        applicationInfo.setDisplayName(name);
        applicationInfo.setDescription(desc);
        applicationInfo.setIconKey(iconKey);
        applicationInfo.setUid(AuthUtils.getUserId());
        if (applicationService.addApplication(applicationInfo)){
            return R.ok("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @RequestMapping(value = "/getAppList")
    @ResponseBody
    public R getAppList(){
        List<ApplicationInfo> appList = applicationService.getApplicationList(AuthUtils.getUserId());
        if (appList.isEmpty()){
            return R.error();
        } else {
            return R.ok(appList);
        }
    }

    @RequestMapping(value = "/uploadIcon")
    @ResponseBody
    public R uploadIcon(@RequestParam(value = "file") MultipartFile file){
        if (null == file){
            return R.badRequest("文件为空");
        }
        String fileExtension = "";
        if ("image/jpeg".equals(file.getContentType())){
            fileExtension = ".jpg";
        }
        if ("image/png".equals(file.getContentType())){
            fileExtension = ".png";
        }
        // 拼接一个随机的文件名进行上传
        String res;
        try {
            res = QiniuUtils.upload(file.getInputStream(), new Md5Hash(file.getName() + System.currentTimeMillis()).toHex() + fileExtension);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("处理文件时发生错误");
        }
        if (null == res){
            return R.error("上传错误");
        } else {
            JSONObject jobj = JSON.parseObject(res);
            return R.ok("上传成功", jobj);
        }
    }
}
