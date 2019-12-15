package top.backrunner.leaf.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.app.entity.DownloadKeyInfo;
import top.backrunner.leaf.app.entity.VersionInfo;
import top.backrunner.leaf.app.service.ApplicationService;
import top.backrunner.leaf.app.service.DownloadService;
import top.backrunner.leaf.utils.common.R;
import top.backrunner.leaf.utils.common.VersionUtils;
import top.backrunner.leaf.utils.network.QiniuUtils;
import top.backrunner.leaf.utils.security.AuthUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/app")
public class ApplicationController {
    @Resource
    private ApplicationService applicationService;
    @Resource
    private DownloadService downloadService;

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

    @RequestMapping(value = "/editApp")
    @ResponseBody
    public R editApp(Long appId, String name, String desc, String iconKey){
        if (!ObjectUtils.allNotNull(appId, name, desc)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo applicationInfo = applicationService.fetchApplication(appId);
        if (applicationInfo == null){
            return R.error("找不到该应用");
        }
        if (applicationInfo.getUid() != AuthUtils.getUserId()) {
            return R.unauth("无权访问");
        }
        applicationInfo.setDisplayName(name);
        applicationInfo.setDescription(desc);
        applicationInfo.setIconKey(iconKey);
        if (applicationService.updateApplication(applicationInfo)){
            return R.ok("编辑成功");
        } else {
            return R.error("编辑失败");
        }
    }

    @RequestMapping(value = "/getInfo")
    @ResponseBody
    public R getInfo(Long appId){
        if (!ObjectUtils.allNotNull(appId)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo applicationInfo = applicationService.fetchApplication(appId);
        if (applicationInfo == null){
            return R.error("找不到该应用");
        }
        if (applicationInfo.getUid() != AuthUtils.getUserId()) {
            return R.unauth("无权访问");
        }
        // 获取该应用的版本数量
        JSONObject appJO = JSON.parseObject(JSON.toJSONString(applicationInfo));
        appJO.put("versionCount", applicationService.getVersionCount(appId));
        return R.ok(appJO);
    }

    @RequestMapping(value = "/getAppList")
    @ResponseBody
    public R getAppList(){
        List<ApplicationInfo> appList = applicationService.getApplicationList(AuthUtils.getUserId());
        if (appList.isEmpty()){
            return R.error("无查询结果");
        } else {
            return R.ok(appList);
        }
    }

    @RequestMapping(value = "/getVersionList")
    @ResponseBody
    public R getVersionList(Long appId, int page, int pageSize){
        if (!ObjectUtils.allNotNull(appId, page, pageSize)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo applicationInfo = applicationService.fetchApplication(appId);
        if (applicationInfo == null){
            return R.error("找不到该应用");
        }
        if (applicationInfo.getUid() != AuthUtils.getUserId()) {
            return R.unauth("无权访问");
        }
        List<VersionInfo> versionList = applicationService.getVersionList(appId, page, pageSize);
        if (versionList.isEmpty()){
            return R.error("无查询结果");
        } else {
            long count = applicationService.getVersionCount(appId);
            HashMap<String, Object> map = new HashMap<>();
            map.put("total", count);
            map.put("list", versionList);
            return R.ok(map);
        }
    }

    @RequestMapping(value = "/addVersion")
    @ResponseBody
    public R addVersion(Long appId, String platform, String version, String fileKey) {
        if (!ObjectUtils.allNotNull(appId, platform, version, fileKey)){
            return R.badRequest("提交的参数不完整");
        }
        // 校验参数
        if (!VersionUtils.validate(version)){
            return R.badRequest("提交的参数不正确");
        }
        if (!("windows".equals(platform) || "linux".equals(platform) || "macos".equals(platform) || "android".equals(platform) || "ios".equals(platform))){
            return R.badRequest("提交的参数不正确");
        }
        ApplicationInfo applicationInfo = applicationService.fetchApplication(appId);
        if (applicationInfo == null){
            return R.error("找不到该应用");
        }
        if (applicationInfo.getUid() != AuthUtils.getUserId()) {
            return R.unauth("无权访问");
        }
        // 检查冲突
        if (applicationService.versionExist(appId, platform, version)){
            return R.error("该版本已存在，无法添加");
        }
        // 创建新实体
        VersionInfo info = new VersionInfo();
        info.setAppId(appId);
        info.setPlatform(platform);
        info.setVersion(version);
        info.setFileKey(fileKey);
        // 添加版本到数据库
        boolean res = applicationService.addVersion(info);
        if (!res) {
            return R.error("添加失败");
        }
        // 获取应用的当前版本，比较
        String v = applicationInfo.getCurrentVersion().get(platform);
        if (v == null){
            Map<String, String> currentVersion = applicationInfo.getCurrentVersion();
            currentVersion.put(platform, version);
            applicationInfo.setCurrentVersion(currentVersion);
            applicationService.updateApplication(applicationInfo);
        } else {
            if (VersionUtils.compare(version, v) > 0){
                Map<String, String> currentVersion = applicationInfo.getCurrentVersion();
                currentVersion.put(platform, version);
                applicationInfo.setCurrentVersion(currentVersion);
                applicationService.updateApplication(applicationInfo);
            }
        }
        return R.ok("添加成功");
    }

    @RequestMapping(value = "/deleteApp")
    @ResponseBody
    public R deleteApp(long appId) {
        if (!ObjectUtils.allNotNull(appId)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo applicationInfo = applicationService.fetchApplication(appId);
        if (applicationInfo == null){
            return R.error("找不到该应用");
        }
        if (applicationInfo.getUid() != AuthUtils.getUserId()) {
            return R.unauth("无权访问");
        }
        if (applicationService.deleteApplication(appId)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @RequestMapping(value = "/deleteVersion")
    @ResponseBody
    public R deleteVersion(long appId, long versionId) {
        if (!ObjectUtils.allNotNull(appId, versionId)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo applicationInfo = applicationService.fetchApplication(appId);
        if (applicationInfo == null){
            return R.error("找不到该应用");
        }
        if (applicationInfo.getUid() != AuthUtils.getUserId()) {
            return R.unauth("无权访问");
        }
        VersionInfo versionInfo = applicationService.fetchVersion(appId);
        if (versionInfo == null){
            return R.error("找不到该版本");
        }
        if (!versionInfo.getAppId().equals(applicationInfo.getId())) {
            return R.error("应用与版本不匹配");
        }
        // 判断是否为当前版本
        String currentVersion = applicationInfo.getCurrentVersion().get(versionInfo.getPlatform());
        if (currentVersion != null && versionInfo.getVersion().equals(currentVersion)){
            Map<String, String> map = applicationInfo.getCurrentVersion();
            // 找到当前版本的下一个版本
            List<VersionInfo> versions = applicationService.getVersionList(appId, versionInfo.getPlatform());
            if (versions.size() > 0){
                map.put(versionInfo.getPlatform(), VersionUtils.max(versions));
            } else {
                map.remove(versionInfo.getPlatform());
            }
            applicationInfo.setCurrentVersion(map);
            applicationService.updateApplication(applicationInfo);
        }
        if (applicationService.deleteVersion(versionInfo)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @RequestMapping(value = "/getDownloadKey")
    @ResponseBody
    public R getDownloadKey(long appId, long versionId, long expires){
        if (!ObjectUtils.allNotNull(appId, versionId)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo applicationInfo = applicationService.fetchApplication(appId);
        if (applicationInfo == null){
            return R.error("找不到该应用");
        }
        if (applicationInfo.getUid() != AuthUtils.getUserId()) {
            return R.unauth("无权访问");
        }
        VersionInfo versionInfo = applicationService.fetchVersion(appId);
        if (versionInfo == null){
            return R.error("找不到该版本");
        }
        if (!versionInfo.getAppId().equals(applicationInfo.getId())) {
            return R.error("应用与版本不匹配");
        }
        // 创建key
        DownloadKeyInfo downloadKeyInfo = new DownloadKeyInfo();
        downloadKeyInfo.setAppId(appId);
        downloadKeyInfo.setVersionId(versionId);
        downloadKeyInfo.setLinkKey(new Sha1Hash(new SecureRandomNumberGenerator().nextBytes().toHex() + System.currentTimeMillis()).toHex());
        downloadKeyInfo.setExpires(expires);
        if (downloadService.createDownloadKey(downloadKeyInfo)){
            return R.ok("链接创建成功", downloadKeyInfo.getLinkKey());
        } else {
            return R.error("链接创建失败");
        }
    }

    @RequestMapping(value = "/getReportList")
    @ResponseBody
    public R getReportList(int page, int pageSize){
        if (!ObjectUtils.allNotNull(page, pageSize)){
            return R.badRequest("提交的参数不完整");
        }
        List<Map<String, Object>> res = applicationService.getReport(AuthUtils.getUserId(), page, pageSize);
        if (res.isEmpty()){
            return R.error("无查询结果");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("total", applicationService.getApplicationCount(AuthUtils.getUserId()));
            map.put("list", res);
            return R.ok(map);
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
            res = QiniuUtils.uploadIcon(file.getInputStream(), new Md5Hash(file.getOriginalFilename() + System.currentTimeMillis()).toHex() + fileExtension);
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

    @RequestMapping(value = "/uploadApp")
    @ResponseBody
    public R uploadApp(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response){
        if (null == file){
            response.setStatus(400);
            return R.badRequest("文件为空");
        }
        String fileExtension = null;
        if (file.getOriginalFilename().endsWith(".exe")){
            fileExtension = ".exe";
        } else if (file.getOriginalFilename().endsWith(".deb")){
            fileExtension = ".deb";
        } else if (file.getOriginalFilename().endsWith(".rpm")){
            fileExtension = ".rpm";
        } else if (file.getOriginalFilename().endsWith(".dmg")){
            fileExtension = ".dmg";
        } else if (file.getOriginalFilename().endsWith(".tar.gz")){
            fileExtension = ".tar.gz";
        } else if (file.getOriginalFilename().endsWith(".apk")){
            fileExtension = ".apk";
        } else if (file.getOriginalFilename().endsWith(".ipa")){
            fileExtension = ".ipa";
        }
        if (fileExtension == null) {
            response.setStatus(400);
            return R.badRequest("文件非法");
        }
        // 拼接一个随机的文件名进行上传
        String res;
        try {
            res = QiniuUtils.uploadApp(file.getInputStream(), new Md5Hash(file.getOriginalFilename() + System.currentTimeMillis()).toHex() + fileExtension);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(500);
            return R.error("处理文件时发生错误");
        }
        if (null == res){
            response.setStatus(500);
            return R.error("上传错误");
        } else {
            JSONObject jobj = JSON.parseObject(res);
            return R.ok("上传成功", jobj);
        }
    }
}
