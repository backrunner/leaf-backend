package top.backrunner.leaf.app.service.impl;

import org.springframework.stereotype.Service;
import top.backrunner.leaf.app.dao.DownloadKeyDao;
import top.backrunner.leaf.app.dao.DownloadLogDao;
import top.backrunner.leaf.app.entity.DownloadKeyInfo;
import top.backrunner.leaf.app.service.DownloadService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class DownloadServiceImpl implements DownloadService {
    @Resource
    DownloadKeyDao downloadKeyDao;
    @Resource
    DownloadLogDao downloadLogDao;

    @Override
    public boolean createDownloadKey(DownloadKeyInfo key) {
        key.setCreateTime(new Date());
        return downloadKeyDao.add(key);
    }

    @Override
    public boolean createLog(long appId, long versionId, String ip) {
        return downloadLogDao.create(appId, versionId, ip);
    }

    @Override
    public boolean keyExists(String key) {
        return downloadKeyDao.exists(key);
    }

    @Override
    public DownloadKeyInfo fetchDownloadKey(String key) {
        return downloadKeyDao.get(key);
    }
}
