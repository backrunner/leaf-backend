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
    // 描述
    private String description;
    // 平台
    private String platform;
    // 是否可用
    private boolean isEnabled;
    // 版本下载量
    private long downloadCount;
    // 对应的应用文件
    private String fileKey;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
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

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
