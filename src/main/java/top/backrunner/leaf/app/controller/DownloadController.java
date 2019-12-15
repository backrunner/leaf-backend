package top.backrunner.leaf.app.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.app.entity.DownloadKeyInfo;
import top.backrunner.leaf.app.entity.VersionInfo;
import top.backrunner.leaf.app.service.ApplicationService;
import top.backrunner.leaf.app.service.DownloadService;
import top.backrunner.leaf.utils.common.R;
import top.backrunner.leaf.utils.misc.GeoIPStringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/download")
public class DownloadController {
    @Resource
    private ApplicationService applicationService;
    @Resource
    private DownloadService downloadService;

    @RequestMapping(value = "/getInfo")
    @ResponseBody
    private R getInfo(String key){
        if (!ObjectUtils.allNotNull(key)){
            return R.badRequest("提交的参数不完整");
        }
        if (!downloadService.keyExists(key)){
            return R.error("Key 不存在");
        }
        DownloadKeyInfo keyInfo = downloadService.fetchDownloadKey(key);
        if (keyInfo == null){
            return R.error("Key 不存在");
        }
        ApplicationInfo app = applicationService.fetchApplication(keyInfo.getAppId());
        VersionInfo version = applicationService.fetchVersion(keyInfo.getVersionId());
        Map<String, Object> res = new HashMap<>();
        res.put("name", app.getDisplayName());
        res.put("desc", app.getDescription());
        res.put("iconKey", app.getIconKey());
        res.put("createTime", version.getCreateTime());
        res.put("platform", version.getPlatform());
        res.put("version", version.getVersion());
        res.put("fileKey", version.getFileKey());
        return R.ok(res);
    }

    @RequestMapping(value = "/submitLog")
    @ResponseBody
    public R submitLog(String key, HttpServletRequest request){
        if (!ObjectUtils.allNotNull(key)){
            return R.badRequest("提交的参数不完整");
        }
        if (!downloadService.keyExists(key)){
            return R.error("Key 不存在");
        }
        DownloadKeyInfo keyInfo = downloadService.fetchDownloadKey(key);
        if (keyInfo == null){
            return R.error("Key 不存在");
        }
        ApplicationInfo app = applicationService.fetchApplication(keyInfo.getAppId());
        VersionInfo version = applicationService.fetchVersion(keyInfo.getVersionId());
        app.setDownloadCount(app.getDownloadCount() + 1);
        version.setDownloadCount(version.getDownloadCount() + 1);
        if (downloadService.createLog(keyInfo.getAppId(), keyInfo.getVersionId(), GeoIPStringUtils.getIPAddress(request)) && applicationService.updateApplication(app) && applicationService.updateVersion(version)){
            return R.ok("记录成功");
        } else {
            return R.error("记录失败");
        }
    }
}
