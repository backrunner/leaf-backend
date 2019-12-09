package top.backrunner.leaf.app.entity;

import top.backrunner.leaf.core.entity.CoreEntityInfo;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "leaf_app")
public class ApplicationInfo extends CoreEntityInfo {
    // 所属用户
    @Column(nullable = false)
    private long uid;
    // 唯一的ID 如com.example.app
    @Column(nullable = false)
    private String bundleId;
    // 名称
    @Column(nullable = false)
    private String displayName;
    // 描述
    private String description;
    // 图标
    private String iconId;
    // 当前版本
    @ElementCollection
    private Map<String, String> currentVersion = new HashMap<>();
    // 是否可用
    public boolean isEnabled;

    // 下载量
    public boolean downloadCount;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public Map<String, String> getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Map<String, String> currentVersion) {
        this.currentVersion = currentVersion;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(boolean downloadCount) {
        this.downloadCount = downloadCount;
    }
}
