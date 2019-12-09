package top.backrunner.leaf.system.entity.log;

import top.backrunner.leaf.core.entity.CoreLogInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "leaf_log_login")
public class UserLoginLogInfo extends CoreLogInfo implements Serializable {
    // 用户的id
    private Long uid;
    // IP地址
    private String ip;
    // 地理位置
    private String geo;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }
}
