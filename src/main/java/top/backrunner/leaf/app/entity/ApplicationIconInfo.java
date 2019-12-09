package top.backrunner.leaf.app.entity;

import top.backrunner.leaf.core.entity.CoreEntityInfo;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "leaf_app_icon")
public class ApplicationIconInfo extends CoreEntityInfo implements Serializable {
    // 图标文件 hash
    private String hash;
    // 图标文件 key
    private String fileKey;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
