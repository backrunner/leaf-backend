package top.backrunner.leaf.app.service.impl;

import org.springframework.stereotype.Service;
import top.backrunner.leaf.app.dao.ApplicationDao;
import top.backrunner.leaf.app.entity.ApplicationInfo;
import top.backrunner.leaf.app.service.ApplicationService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    @Resource
    private ApplicationDao applicationDao;

    @Override
    public List<ApplicationInfo> getApplicationList(long uid) {
        return applicationDao.getList(uid);
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
    public boolean bundleIdExists(String bundleId) {
        return applicationDao.bundleIdExists(bundleId);
    }

    @Override
    public boolean deleteApplicationByUser(Long uid) {
        return false;
    }
}