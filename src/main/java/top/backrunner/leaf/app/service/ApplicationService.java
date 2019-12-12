package top.backrunner.leaf.app.service;

import top.backrunner.leaf.app.entity.ApplicationInfo;

import java.util.List;

public interface ApplicationService {
    // apps
    public List<ApplicationInfo> getApplicationList(long uid);
    public ApplicationInfo fetchApplication(Long appId);
    public boolean addApplication(ApplicationInfo app);
    public boolean updateApplication(ApplicationInfo app);
    public boolean bundleIdExists(String bundleId);
    public boolean deleteApplicationByUser(Long uid);
    // version
    public long getVersionCount(Long appId);
}
