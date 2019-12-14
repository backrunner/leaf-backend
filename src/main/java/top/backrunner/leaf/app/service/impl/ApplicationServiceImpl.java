package top.backrunner.leaf.app.service.impl;

import org.springframework.stereotype.Service;
import top.backrunner.leaf.app.dao.ApplicationDao;
import top.backrunner.leaf.app.dao.DownloadLogDao;
import top.backrunner.leaf.app.dao.VersionDao;
import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.app.entity.VersionInfo;
import top.backrunner.leaf.app.service.ApplicationService;
import top.backrunner.leaf.utils.network.QiniuUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    @Resource
    private ApplicationDao applicationDao;
    @Resource
    private VersionDao versionDao;
    @Resource
    private DownloadLogDao downloadLogDao;

    @Override
    public List<ApplicationInfo> getApplicationList(long uid) {
        return applicationDao.getList(uid);
    }

    @Override
    public List<ApplicationInfo> getApplicationList(int page, int pageSize) {
        return applicationDao.showPage("FROM ApplicationInfo", page, pageSize);
    }

    @Override
    public long getApplicationCount() {
        return applicationDao.getCount();
    }

    @Override
    public boolean addApplication(ApplicationInfo app) {
        // 初始化
        app.setCreateTime(new Date());
        app.setEnabled(true);
        app.setDownloadCount(0);
        return applicationDao.add(app);
    }

    @Override
    public boolean updateApplication(ApplicationInfo app) {
        app.setLastUpdateTime(new Date());
        return applicationDao.updateEntity(app);
    }

    @Override
    public ApplicationInfo fetchApplication(Long appId) {
        return applicationDao.getById(ApplicationInfo.class, appId);
    }

    @Override
    public boolean bundleIdExists(String bundleId) {
        return applicationDao.bundleIdExists(bundleId);
    }

    @Override
    public boolean deleteApplication(long id) {
        List<VersionInfo> versionInfoList = this.getVersionList(id);
        for (VersionInfo version : versionInfoList) {
            QiniuUtils.deleteApp(version.getFileKey());
        }
        downloadLogDao.deleteByApp(id);
        versionDao.deleteByAppId(id);
        return applicationDao.deleteById(id);
    }

    @Override
    public boolean deleteApplicationByUser(Long uid) {
        List<ApplicationInfo> list = this.getApplicationList(uid);
        for (ApplicationInfo info: list){
            if (this.deleteApplication(info.getId())){
                return false;
            }
        }
        return true;
    }

    @Override
    public VersionInfo fetchVersion(Long id) {
        return versionDao.getById(VersionInfo.class, id);
    }

    @Override
    public long getVersionCount(Long appId) {
        return versionDao.getCount(appId);
    }

    @Override
    public List<VersionInfo> getVersionList(long appId) {
        return versionDao.getList(appId);
    }

    @Override
    public List<VersionInfo> getVersionList(long appId, String platform) {
        return versionDao.getList(appId, platform);
    }

    @Override
    public List<VersionInfo> getVersionList(long appId, int page, int pageSize) {
        return versionDao.getList(appId, page, pageSize);
    }

    @Override
    public boolean versionExist(Long appId, String platform, String version) {
        return versionDao.exists(appId, platform, version);
    }

    @Override
    public boolean addVersion(VersionInfo v) {
        v.setCreateTime(new Date());
        v.setEnabled(true);
        v.setDownloadCount(0);
        return versionDao.add(v);
    }

    @Override
    public boolean deleteVersion(VersionInfo v) {
        // 先删除 log
        downloadLogDao.deleteByVersion(v.getId());
        // 删除云端文件
        QiniuUtils.deleteApp(v.getFileKey());
        return versionDao.deleteById(v.getId());
    }
}
