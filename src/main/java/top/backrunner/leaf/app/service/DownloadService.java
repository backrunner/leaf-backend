package top.backrunner.leaf.app.service;

import top.backrunner.leaf.app.entity.DownloadKeyInfo;

public interface DownloadService {
    public boolean createDownloadKey(DownloadKeyInfo key);
    public boolean createLog(long appId, long versionId, String ip);
    public boolean keyExists(String key);
    public DownloadKeyInfo fetchDownloadKey(String key);
}
