package top.backrunner.leaf.app.service.impl;

import org.springframework.stereotype.Service;
import top.backrunner.leaf.app.dao.DownloadKeyDao;
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
    @Override
    public boolean createDownloadKey(DownloadKeyInfo key) {
        key.setCreateTime(new Date());
        return downloadKeyDao.add(key);
    }
}
