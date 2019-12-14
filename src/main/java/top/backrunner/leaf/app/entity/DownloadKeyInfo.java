package top.backrunner.leaf.app.entity;

import top.backrunner.leaf.core.entity.CoreEntityInfo;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "leaf_download_key")
public class DownloadKeyInfo extends CoreEntityInfo implements Serializable {
    private long appId;
    private long versionId;
    private String linkKey;
    private long expires;

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String key) {
        this.linkKey = key;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}