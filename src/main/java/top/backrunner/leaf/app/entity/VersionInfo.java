package top.backrunner.leaf.app.entity;

import top.backrunner.leaf.core.entity.CoreEntityInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "leaf_version")
public class VersionInfo extends CoreEntityInfo {
    // 版本所属App
    private Long appId;
    // 版本号
    @Column(nullable = false)
    private String version;
    // 平台
    private String platform;
    // 是否可用
    private boolean isEnabled;
    // 版本下载量
    private Long downloadCount;

    // 对应的应用文件
    private Long fileId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
