package top.backrunner.leaf.app.service;

import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.app.entity.VersionInfo;

import java.util.List;
import java.util.Map;

public interface ApplicationService {
    // apps
    public List<ApplicationInfo> getApplicationList(long uid);
    public List<ApplicationInfo> getApplicationList(int page, int pageSize);
    public long getApplicationCount();
    public long getApplicationCount(long uid);
    public ApplicationInfo fetchApplication(Long appId);
    public boolean addApplication(ApplicationInfo app);
    public boolean updateApplication(ApplicationInfo app);
    public boolean bundleIdExists(String bundleId);
    public boolean deleteApplication(long id);
    public boolean deleteApplicationByUser(Long uid);
    public List<Map<String, Object>> getReport(long uid, int page, int pageSize);
    // version
    public VersionInfo fetchVersion(Long id);
    public long getVersionCount(Long appId);
    public List<VersionInfo> getVersionList(long appId);
    public List<VersionInfo> getVersionList(long appId, String platform);
    public List<VersionInfo> getVersionList(long appId, int page, int pageSize);
    public boolean versionExist(Long appId, String platform, String version);
    public boolean addVersion(VersionInfo v);
    public boolean updateVersion(VersionInfo v);
    public boolean deleteVersion(VersionInfo v);
}
