package top.backrunner.leaf.app.entity;

import top.backrunner.leaf.core.entity.CoreLogInfo;
import top.backrunner.leaf.utils.misc.IPLocation;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "leaf_log_download")
public class DownloadLogInfo extends CoreLogInfo implements Serializable {
    private Long appId;
    private Long versionId;
    private String ipAddress;
    private String geo;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }
}
