package top.backrunner.leaf.app.service;

import top.backrunner.leaf.app.entity.DownloadKeyInfo;

public interface DownloadService {
    public boolean createDownloadKey(DownloadKeyInfo key);
}
