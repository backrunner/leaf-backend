package top.backrunner.leaf.system.entity;

import top.backrunner.leaf.core.entity.CoreEntityInfo;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class InviteCodeInfo extends CoreEntityInfo implements Serializable {

    private Long createUid;
    private String code;
    private boolean used;
    private Long usedUid;

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Long getUsedUid() {
        return usedUid;
    }

    public void setUsedUid(Long usedUid) {
        this.usedUid = usedUid;
    }
}
