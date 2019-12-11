package top.backrunner.leaf.app.service;

import top.backrunner.leaf.app.entity.ApplicationInfo;

import java.util.List;

public interface ApplicationService {
    // apps
    public List<ApplicationInfo> getApplicationList(long uid);
    public boolean addApplication(ApplicationInfo app);
    public boolean bundleIdExists(String bundleId);
    public boolean deleteApplicationByUser(Long uid);
}
