package top.backrunner.leaf.system.entity;

import top.backrunner.leaf.core.entity.CoreInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="leaf_role", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class RoleInfo extends CoreInfo implements Serializable {

    // 角色名称
    @Column(nullable = false)
    private String name;

    public RoleInfo(){
        // default
    }

    public RoleInfo(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
